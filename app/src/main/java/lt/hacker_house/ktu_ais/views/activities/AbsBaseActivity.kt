package lt.hacker_house.ktu_ais.views.activities

import android.support.v7.app.AppCompatActivity
import org.greenrobot.eventbus.EventBus

/**
 * Created by simonas on 5/21/17.
 */
open class AbsBaseActivity: AppCompatActivity() {

    override fun onStart() {
        super.onStart()
        EventBus.getDefault().register(this)
    }

    override fun onStop() {
        EventBus.getDefault().unregister(this)
        super.onStart()
    }
}