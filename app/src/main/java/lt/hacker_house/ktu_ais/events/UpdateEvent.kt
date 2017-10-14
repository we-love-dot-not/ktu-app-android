package lt.hacker_house.ktu_ais.events

import lt.hacker_house.ktu_ais.models.RlUserModel
import org.greenrobot.eventbus.EventBus

/**
 * Created by simonas on 5/21/17.
 */

class UpdateEvent(val userModel: RlUserModel) {

    companion object {
        fun send(userModel: RlUserModel) {
            EventBus.getDefault().post(UpdateEvent(userModel))
        }
    }
}