package lt.hacker_house.ktu_ais.db

import io.realm.Realm
import lt.hacker_house.ktu_ais.App
import lt.hacker_house.ktu_ais.R
import lt.hacker_house.ktu_ais.events.UpdateEvent
import lt.hacker_house.ktu_ais.models.GradeUpdateModel
import lt.hacker_house.ktu_ais.models.RlGradesResponse
import lt.hacker_house.ktu_ais.models.RlSemesterInfoModel
import lt.hacker_house.ktu_ais.models.RlUserModel
import lt.hacker_house.ktu_ais.services.GetGradesIntentService
import lt.hacker_house.ktu_ais.utils.Prefs
import lt.hacker_house.ktu_ais.utils.diff
import lt.hacker_house.ktu_ais.utils.filterSemester
import lt.hacker_house.ktu_ais.utils.toWeekList
import lt.welovedotnot.ktu_ais_api.KtuApiClient
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import java.util.*

/**
 * Created by simonas on 4/30/17.
 */
object User {
    fun login(username: String, password:String, callback: (Boolean)->Unit) {
        doAsync {
            try {
                val loginResp = loginSync(username, password)
                uiThread {
                    callback.invoke(loginResp)
                }
            } catch (e: Exception) {
                e.printStackTrace()
                uiThread {
                    callback.invoke(false)
                }
            }
        }
    }

    /**
     * Login and get all user data.
     */
    private fun loginSync(username: String, password:String): Boolean {
        val loginModel = KtuApiClient.login(username, password)
        val rlLoginModel = RlUserModel.from(loginModel)

        if (rlLoginModel == null) {
            return false
        } else {
            rlLoginModel.username = username
            rlLoginModel.password = password
            rlLoginModel.yearList.sortWith(compareBy { it.year })
            val pair = getCurrentYear()
            val year = pair.first
            val semesterAdder = pair.second
            val yearModel = rlLoginModel.yearList.find { it.year == year.toString() }
            val indexOf = rlLoginModel.yearList.indexOf(yearModel)
            val semester = indexOf * 2 + semesterAdder
            rlLoginModel.defaultSemester = RlSemesterInfoModel(
                    year = year,
                    semesterString = semester.toKtuSemesterString()
            )
            updateGrades(rlLoginModel)
        }
        return true
    }

    /**
     * Loads new grades on existing user.
     */
    private fun updateGrades(userModel: RlUserModel) {
        val selectedSemester = Prefs.getCurrentSemester(userModel)
        val module = userModel.yearList.findLast {
            val yearL = it.year!!
            val yearS = selectedSemester.year.toString()
            val isEq = yearL == yearS
            isEq
        } ?: userModel.yearList.last()
        val gradeList = KtuApiClient.getGrades(userModel.cookie!!, module.year!!, module.id!!)
        val rlGradesList = RlGradesResponse.from(gradeList)
        val currentSemester = Prefs.getCurrentSemester(userModel).semesterString
        val weekList = rlGradesList.toWeekList(currentSemester)

        userModel.timestamp = System.currentTimeMillis()
        userModel.weekList.clear()
        userModel.gradeList.clear()
        userModel.weekList.addAll(weekList.toList())
        userModel.gradeList.addAll(rlGradesList)
        execTrans {
            copyToRealmOrUpdate(userModel)
        }
    }

    fun get(): RlUserModel? {
        exec {
            where(RlUserModel::class.java).findFirst()?.let {
                return copyFromRealm(it)
            }
        }
        return null
    }

    /**
     * Checks if cached user data exists.
     */
    fun isLoggedIn(): Boolean
        = User.get() != null

    /**
     * Full relogin
     */
    fun update(callback: (Boolean, Collection<GradeUpdateModel>) -> (Unit)) {
        val userModel = User.get()
        val semesterString = Prefs.getCurrentSemester(userModel!!).semesterString
        val oldGrades = userModel.gradeList.filterSemester(semesterString)
        User.login(userModel.username!!, userModel.password!!) { isSuccess ->
            val freshUser = User.get()
            val freshGrades = freshUser!!.gradeList.filterSemester(semesterString)
            val diff = oldGrades.diff(freshGrades)
            callback.invoke(isSuccess, diff)
            UpdateEvent.send(freshUser) // Notify about updated data.
        }
    }

    fun getSemesters(callback: (RlUserModel, Array<String>, Array<String>)->(Unit)) {
        val userModel = User.get()
        val semesterEntries = mutableSetOf<String>()
        val semesterValues = mutableSetOf<String>()
        val semesterList = userModel?.yearList?.sortedWith(compareBy { it.year })

        var semesterNo = 1
        semesterList?.forEach { yearModel ->
            val autumnNo = semesterNo
            semesterNo++
            val springNo = semesterNo
            semesterNo++
            val autumNoStr = autumnNo.toKtuSemesterString()
            val springNoStr = springNo.toKtuSemesterString()
            val year = yearModel.year.toString()
            semesterEntries.add("$year ${App.context.getString(R.string.autumn)}. $autumNoStr")
            semesterValues.add(year + "-" + autumNoStr)

            semesterEntries.add("$year ${App.context.getString(R.string.spring)}. $springNoStr")
            semesterValues.add(year + "-" + springNoStr)

            callback.invoke(userModel, semesterEntries.toTypedArray(), semesterValues.toTypedArray())
        }
    }

    /**
     * Clear user data cache
     */
    fun logout(): Boolean {
        var isSuccessful = false
        execTrans {
            Prefs.clear()
            GetGradesIntentService.cancel(App.context)
            val userModels = where(RlUserModel::class.java).findAll()
            isSuccessful = userModels.deleteAllFromRealm()
        }
        return isSuccessful
    }

    /**
     * Utils method used to calculate current semester.
     * @param callback First param -- year; Second -- 0 if its first semester of the year, 1 if second.
     */
    private fun getCurrentYear(): Pair<Int, Int> {
        val time = Calendar.getInstance()
        val year = time.get(Calendar.YEAR)
        val month = time.get(Calendar.MONTH)
        return if (month in Calendar.FEBRUARY..Calendar.AUGUST) {
            year-1 to 2
        } else {
            year to 1
        }
    }

    private fun Int.toKtuSemesterString()
            = String.format("%02d", this)

    private inline fun exec(task: Realm.()->Unit) {
        val rl = Realm.getDefaultInstance()
        try {
            task.invoke(rl)
        } finally {
            rl.close()
        }
    }

    private inline fun execTrans(task: Realm.()->Unit) {
        val rl = Realm.getDefaultInstance()
        rl.beginTransaction()
        try {
            task.invoke(rl)
            rl.commitTransaction()
        } finally {
            rl.close()
        }
    }
}