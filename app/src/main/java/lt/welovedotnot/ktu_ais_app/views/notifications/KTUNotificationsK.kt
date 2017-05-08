package lt.welovedotnot.ktu_ais_app.views.notifications

import android.content.Context
import lt.welovedotnot.ktu_ais_app.models.GradeModel
import lt.welovedotnot.ktu_ais_app.models.GradeUpdateModel
import lt.welovedotnot.ktu_ais_app.models.WeekModel

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