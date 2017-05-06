package lt.welovedotnot.ktu_ais_app.db

import android.os.Handler
import android.os.Looper
import android.support.annotation.UiThread
import io.realm.Realm
import io.realm.RealmList
import lt.welovedotnot.ktu_ais_app.api.Api
import lt.welovedotnot.ktu_ais_app.api.models.GetGradesResponse
import lt.welovedotnot.ktu_ais_app.api.models.LoginRequest
import lt.welovedotnot.ktu_ais_app.api.models.ModulesRequest
import lt.welovedotnot.ktu_ais_app.api.models.UserModel

/**
 * Created by simonas on 4/30/17.
 */
object User {
    private var rl: Realm = Realm.getDefaultInstance()

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
                val moduleReq = ModulesRequest()
                val module = userModel?.semesterList!![1]
                moduleReq.year = module.year?.toInt()
                moduleReq.studId = module.id?.toInt()
                Api.grades(moduleReq, userModel!!.cookie!!) { list: List<GetGradesResponse>? ->
                    userModel.gradesList = RealmList()
                    userModel.gradesList?.addAll(list!!)

                    rl.executeTransactionAsync {
                        it.copyToRealmOrUpdate(userModel)
                        runUI {
                            callback.invoke(true)
                        }
                    }
                }
            }
        }
    }

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

    fun isLoggedIn(callback: (Boolean) -> Unit) {
        User.get { model ->
            callback(model!=null)
        }
    }

    fun update(callback: (Boolean) -> (Unit)) {
        User.get { userModel ->
            if(userModel!!.username != null && userModel!!.password != null) {
                User.login(userModel?.username!!, userModel.password!!) { isSuccess ->
                    callback.invoke(isSuccess)
                }
            }
            else {
                throw RuntimeException("Username or password is null")
            }
        }
    }

    fun logout(callback:(Boolean)->(Unit)) {
        rl.executeTransactionAsync {
            var del = it.where(UserModel::class.java).findAll().deleteAllFromRealm()
            runUI {
                callback.invoke(del)
            }
        }
    }

    fun runUI(run: (Unit)->(Unit)) {
        Handler(Looper.getMainLooper()).post {
            run.invoke(Unit)
        }
    }
}