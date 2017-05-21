package lt.hacker_house.ktu_ais.views.notifications

import android.content.Context
import lt.hacker_house.ktu_ais.models.GradeUpdateModel
import lt.hacker_house.ktu_ais.models.WeekModel

/**
 * Created by simonas on 5/8/17.
 */

object KTUNotificationsK {
    val notificationUtil = KTUNotification()

    fun notifyGradeUpdated(context: Context, model: GradeUpdateModel) {
        notificationUtil.sendGradeNotification(model, context)
    }

    fun notifyUpcomingWeek(context: Context, model: WeekModel) {
        notificationUtil.sendUpcomingTestNotification(model, context)
    }
}