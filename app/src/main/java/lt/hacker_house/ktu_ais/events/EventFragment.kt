package lt.hacker_house.ktu_ais.events

import android.app.Fragment
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe

/**
 * Created by simonas on 5/21/17.
 */

open class EventFragment : Fragment() {

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