package lt.welovedotnot.ktu_ais_app.db

import android.os.Handler
import android.os.Looper
import io.realm.Realm
import lt.welovedotnot.ktu_ais_app.AppConf
import lt.welovedotnot.ktu_ais_app.api.Api
import lt.welovedotnot.ktu_ais_app.utils.diff
import lt.welovedotnot.ktu_ais_app.utils.filterSemester
import lt.welovedotnot.ktu_ais_app.models.*
import lt.welovedotnot.ktu_ais_app.utils.toWeekList

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
                updateGrades(userModel, callback)
            }
        }
    }

    /**
     * Loads new grades on existing user.
     */
    private fun updateGrades(userModel: UserModel, callback: (Boolean) -> (Unit)) {
        val moduleReq = ModulesRequest()
        val module = userModel.semesterList[1]
        moduleReq.year = module.year?.toInt()
        moduleReq.studId = module.id?.toInt()

        Api.grades(moduleReq, userModel.cookie!!) { gradeList: List<GetGradesResponse>? ->
            val weekList = gradeList?.toWeekList(AppConf.CURRENT_SEMESTER)

            userModel.timestamp = System.currentTimeMillis()
            userModel.weekList.clear()
            userModel.gradeList.clear()

            userModel.weekList.addAll(weekList!!.toList())
            userModel.gradeList.addAll(gradeList)

            rl.executeTransactionAsync {
                it.copyToRealmOrUpdate(userModel)
                runUI {
                    callback.invoke(true)
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
            val oldGrades = userModel!!.gradeList.filterSemester(AppConf.CURRENT_SEMESTER)
            User.login(userModel.username!!, userModel.password!!) { isSuccess ->
                User.get { freshUser ->
                    val freshGrades = freshUser!!.gradeList.filterSemester(AppConf.CURRENT_SEMESTER)
                    val diff = oldGrades.diff(freshGrades)
                    callback.invoke(isSuccess, diff)
                }
            }
        }
    }

    /**
     * Clear user data cache
     */
    fun logout(callback:(Boolean)->(Unit)) {
        rl.executeTransactionAsync {
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
}