package lt.welovedotnot.ktu_ais_app.db

import android.content.Context
import io.realm.Realm
import io.realm.Realm.setDefaultConfiguration
import io.realm.RealmConfiguration



/**
 * Created by simonas on 4/30/17.
 */

object RealmUtils {
    fun init(context: Context) {
        Realm.init(context)

        val config = RealmConfiguration.Builder()
                .name("db.realm")
                .build()

        Realm.setDefaultConfiguration(config)
    }
}