package lt.hacker_house.ktu_ais.events

import android.support.v7.app.AppCompatActivity
import lt.hacker_house.ktu_ais.events.UpdateEvent
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe

/**
 * Created by simonas on 5/21/17.
 */
open class EventActivity : AppCompatActivity() {

    override fun onStart() {
        super.onStart()
        EventBus.getDefault().register(this)
    }

    override fun onStop() {
        EventBus.getDefault().unregister(this)
        super.onStop()
    }

    @Subscribe
    open fun onUpdateEvent(event: UpdateEvent) {

    }
}