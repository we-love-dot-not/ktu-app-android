package lt.welovedotnot.ktu_ais_app

import android.support.multidex.MultiDexApplication
import lt.welovedotnot.ktu_ais_app.db.RealmUtils

/**
 * Created by simonas on 4/30/17.
 */

class App: MultiDexApplication() {

    override fun onCreate() {
        super.onCreate()

        RealmUtils.init(this)
    }
}
