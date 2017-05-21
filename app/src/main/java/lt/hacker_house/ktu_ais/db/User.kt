package lt.hacker_house.ktu_ais.db

import android.os.Handler
import android.os.Looper
import io.realm.Realm
import lt.hacker_house.ktu_ais.App
import lt.hacker_house.ktu_ais.api.Api
import lt.hacker_house.ktu_ais.utils.diff
import lt.hacker_house.ktu_ais.utils.filterSemester
import lt.hacker_house.ktu_ais.models.*
import lt.hacker_house.ktu_ais.services.GetGradesIntentService
import lt.hacker_house.ktu_ais.utils.Prefs
import lt.hacker_house.ktu_ais.utils.toWeekList
import java.util.*

/**
 * Created by simonas on 4/30/17.
 */
object User {
    private var rl: Realm = Realm.getDefaultInstance()

    /**
     * Login and get all user data.
     */
    fun login(username: String, password:String, callback: (Boolean) -> (Unit)) {
        val loginReq = LoginRequest()
        loginReq.username = username
        loginReq.password = password

        Api.login(loginReq) { userModel ->
            if (userModel == null) {
                runUI {
                    callback.invoke(false)
                }
            } else {
                userModel.yearList.sortWith(compareBy { it.year })
                getCurrentYear { year, semesterAdder ->
                    val yearModel = userModel.yearList.find { it.year == year.toString() }
                    val indexOf = userModel.yearList.indexOf(yearModel)
                    val semester = indexOf * 2 + semesterAdder
                    userModel.defaultSemester = SemesterInfoModel(
                            year = year,
                            semesterString = semester.toKtuSemesterString()
                    )
                }

                updateGrades(userModel, callback)
            }
        }
    }

    /**
     * Loads new grades on existing user.
     */
    private fun updateGrades(userModel: UserModel, callback: (Boolean) -> (Unit)) {
        val moduleReq = ModulesRequest()
        val selectedSemester = Prefs.getCurrentSemester(userModel)
        val module = userModel.yearList.findLast{ it.year == selectedSemester.year.toString()}!!
        moduleReq.year = module.year?.toInt()
        moduleReq.studId = module.id?.toInt()

        Api.grades(moduleReq, userModel.cookie!!) { gradeList: List<GetGradesResponse>? ->
            val currentSemester = Prefs.getCurrentSemester(userModel).semesterString
            val weekList = gradeList?.toWeekList(currentSemester)

            userModel.timestamp = System.currentTimeMillis()
            userModel.weekList.clear()
            userModel.gradeList.clear()

            weekList?.also {
                userModel.weekList.addAll(it.toList())
                userModel.gradeList.addAll(gradeList)

                rl.executeTransactionAsync {
                    it.copyToRealmOrUpdate(userModel)
                    runUI {
                        callback.invoke(true)
                    }
                }
            }
        }
    }

    /**
     * Gets cached user data.
     */
    fun get(callback: (UserModel?)->(Unit)) {
        rl.executeTransactionAsync {
            var model = it.where(UserModel::class.java).findFirst()
            if (model != null) {
                model = it.copyFromRealm(model)
                runUI {
                    callback.invoke(model)
                }
            } else {
                runUI {
                    callback.invoke(null)
                }
            }
        }
    }

    fun getSync(): UserModel? {
        var model: UserModel? = null
        rl.executeTransaction {
            val managedModel = it.where(UserModel::class.java).findFirst()
            if (managedModel != null) {
                model = it.copyFromRealm(managedModel)
            }
        }
        return model
    }

    /**
     * Checks if cached user data exists.
     */
    fun isLoggedIn(callback: (Boolean) -> Unit) {
        User.get { model ->
            callback(model!=null)
        }
    }

    /**
     * Full relogin
     */
    fun update(callback: (Boolean, Collection<GradeUpdateModel>) -> (Unit)) {
        User.get { userModel ->
            val semesterString = Prefs.getCurrentSemester(userModel!!).semesterString
            val oldGrades = userModel.gradeList.filterSemester(semesterString)
            User.login(userModel.username!!, userModel.password!!) { isSuccess ->
                User.get { freshUser ->
                    val freshGrades = freshUser!!.gradeList.filterSemester(semesterString)
                    val diff = oldGrades.diff(freshGrades)
                    callback.invoke(isSuccess, diff)
                }
            }
        }
    }

    fun getSemesters(callback: (UserModel, Array<String>, Array<String>)->(Unit)) {
        User.get { userModel ->
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
                semesterEntries.add("$year Rudens. $autumNoStr")
                semesterValues.add(year + "-" + autumNoStr)

                semesterEntries.add("$year Pavasario. $springNoStr")
                semesterValues.add(year + "-" + springNoStr)

                callback.invoke(userModel, semesterEntries.toTypedArray(), semesterValues.toTypedArray())
            }
        }
    }

    /**
     * Get Sorted User semesters
     */
    fun getSemesters(callback: (Array<String>, Array<String>)->(Unit)) {
        getSemesters { _, semesterEntries, semesterValues ->
            callback.invoke(semesterEntries, semesterValues)
        }
    }

    /**
     * Clear user data cache
     */
    fun logout(callback:(Boolean)->(Unit)) {
        rl.executeTransactionAsync {
            Prefs.clear()
            GetGradesIntentService.cancel(App.context)
            val del = it.where(UserModel::class.java).findAll().deleteAllFromRealm()
            runUI {
                callback.invoke(del)
            }
        }
    }

    private fun runUI(run: (Unit)->(Unit)) {
        Handler(Looper.getMainLooper()).post {
            run.invoke(Unit)
        }
    }

    /**
     * Utils method used to calculate current semester.
     * @param callback First param -- year; Second -- 0 if its first semester of the year, 1 if second.
     */
    private fun getCurrentYear(callback: (Int, Int) -> Unit) {
        val time = Calendar.getInstance()
        val year = time.get(Calendar.YEAR)
        val month = time.get(Calendar.MONTH)
        if (month in 2..8) {
            return callback(year-1, 2)
        } else {
            return callback(year,1)
        }
    }

    private fun Int.toKtuSemesterString()
            = String.format("%02d", this)
}