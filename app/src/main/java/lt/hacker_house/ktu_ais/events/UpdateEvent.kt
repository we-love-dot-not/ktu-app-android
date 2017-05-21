package lt.hacker_house.ktu_ais.events

import lt.hacker_house.ktu_ais.models.UserModel
import org.greenrobot.eventbus.EventBus

/**
 * Created by simonas on 5/21/17.
 */

class UpdateEvent(val userModel: UserModel) {

    companion object {
        fun send(userModel: UserModel) {
            EventBus.getDefault().post(UpdateEvent(userModel))
        }
    }
}