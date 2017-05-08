package lt.welovedotnot.ktu_ais_app.services

import android.app.AlarmManager
import android.app.IntentService
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Handler
import android.os.Looper
import com.mcxiaoke.koi.ext.getAlarmManager

import lt.welovedotnot.ktu_ais_app.db.User
import java.util.*

/**
 * Created by Mindaugas on 5/6/2017.
 */

class GetGradesIntentService : IntentService("GetGradesIntentService") {

    companion object {
        val RUN_IN_HOURS = 1
        fun startBackgroundService(context: Context) {
            val cal = Calendar.getInstance()
            cal.add(Calendar.SECOND, 10)

            val pintent = getServiceIntent(context)
            val alarm = context.getAlarmManager()
            alarm.setRepeating(AlarmManager.RTC_WAKEUP, cal.timeInMillis,
                    (RUN_IN_HOURS * 60 * 60 * 1000).toLong(), pintent)
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
                        // Show notification
                    }
                } else {
                    getAlarmManager().cancel(getServiceIntent(this))
                }
            }
        }
    }
}
