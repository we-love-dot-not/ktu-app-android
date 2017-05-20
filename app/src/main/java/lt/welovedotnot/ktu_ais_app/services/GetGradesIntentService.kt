package lt.welovedotnot.ktu_ais_app.services

import android.app.AlarmManager
import android.app.IntentService
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Handler
import android.os.Looper
import com.mcxiaoke.koi.ext.getAlarmManager
import com.mcxiaoke.koi.log.logd

import lt.welovedotnot.ktu_ais_app.db.User
import lt.welovedotnot.ktu_ais_app.utils.Prefs
import lt.welovedotnot.ktu_ais_app.views.notifications.KTUNotificationsK
import java.util.*

/**
 * Created by Mindaugas on 5/6/2017.
 */

class GetGradesIntentService : IntentService("GetGradesIntentService") {

    companion object {
        fun startBackgroundService(context: Context) {
            val cal = Calendar.getInstance()
            cal.add(Calendar.SECOND, 10)

            val RUN_IN_MINUTES = Prefs.getRefreshInterval()

            val pIntent = getServiceIntent(context)
            val alarm = context.getAlarmManager()
            alarm.setRepeating(AlarmManager.RTC_WAKEUP, cal.timeInMillis,
                    (RUN_IN_MINUTES * 60 * 1000).toLong(), pIntent)
        }

        fun cancel(context: Context) {
            context.getAlarmManager().cancel(getServiceIntent(context))
        }

        fun getServiceIntent(context: Context): PendingIntent {
            val intent = Intent(context, GetGradesIntentService::class.java)
            val pintent = PendingIntent.getService(context, 0, intent, 0)
            return pintent
        }
    }

    override fun onHandleIntent(intent: Intent?) {
        // Gets data from the incoming Intent
        Handler(Looper.getMainLooper()).post {
            User.isLoggedIn { isLoggedIn ->
                if (isLoggedIn) {
                    User.update { _, updatedGrades ->
                        if (Prefs.areNotificationEnabled()) {
                            updatedGrades.forEach { updatedGrade ->
                                KTUNotificationsK.notifyGradeUpdated(this, updatedGrade)
                            }
                        }
                    }
                } else {
                    getAlarmManager().cancel(getServiceIntent(this))
                }
            }
        }
    }
}
