package lt.welovedotnot.ktu_ais_app.db

import android.os.Handler
import android.os.Looper
import android.support.annotation.UiThread
import io.realm.Realm
import lt.welovedotnot.ktu_ais_app.api.Api
import lt.welovedotnot.ktu_ais_app.api.models.LoginRequest
import lt.welovedotnot.ktu_ais_app.api.models.UserModel

/**
 * Created by simonas on 4/30/17.
 */
object User {
    private var rl: Realm = Realm.getDefaultInstance()

    @UiThread
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
                rl.executeTransactionAsync {
                    it.copyToRealmOrUpdate(userModel)
                    runUI {
                        callback.invoke(true)
                    }
                }
            }
        }
    }

    @UiThread
    fun get(callback: (UserModel?)->(Unit)) {
        rl.executeTransactionAsync {
            var model = it.where(UserModel::class.java).findFirst()
            model = it.copyFromRealm(model)
            runUI {
                callback.invoke(model)
            }
        }
    }

    @UiThread
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