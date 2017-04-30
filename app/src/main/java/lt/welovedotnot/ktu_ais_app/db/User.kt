package lt.welovedotnot.ktu_ais_app.db

import io.realm.Realm

/**
 * Created by simonas on 4/30/17.
 */
object User {
    private var rl: Realm = Realm.getDefaultInstance()

    fun login(model: UserModel) {
        rl.executeTransaction {
            it.where(UserModel::class.java).findAll().deleteAllFromRealm()
            it.copyToRealm(model)
        }
    }

    fun get(): UserModel? {
        var findFirst: UserModel? = null
        rl.executeTransaction {
           findFirst = it.where(UserModel::class.java).findFirst()
        }
        return findFirst
    }

    fun logout() {
        rl.executeTransaction {
            it.where(UserModel::class.java).findAll().deleteAllFromRealm()
        }
    }
}