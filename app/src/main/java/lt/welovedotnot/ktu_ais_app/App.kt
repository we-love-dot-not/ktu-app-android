package lt.welovedotnot.ktu_ais_app

import android.content.Context
import android.support.multidex.MultiDexApplication
import lt.welovedotnot.ktu_ais_app.db.RealmUtils

/**
 * Created by simonas on 4/30/17.
 */

class App: MultiDexApplication() {
    companion object {
        lateinit var context: Context
    }

    override fun onCreate() {
        super.onCreate()
        context = this
        RealmUtils.init(this)
    }
}
