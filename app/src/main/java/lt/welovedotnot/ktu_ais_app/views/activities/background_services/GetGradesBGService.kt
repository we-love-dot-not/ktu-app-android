package lt.welovedotnot.ktu_ais_app.views.activities.background_services

import android.app.AlarmManager
import android.app.IntentService
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Handler
import android.os.Looper

import lt.welovedotnot.ktu_ais_app.db.User
import java.util.*

/**
 * Created by Mindaugas on 5/6/2017.
 */

class GetGradesBGService : IntentService("GetGradesBGService") {

    companion object {
        val RUN_IN_HOURS = 1
        fun startBackgroundService(context: Context) {
            val cal = Calendar.getInstance()
            cal.add(Calendar.SECOND, 10)

            val intent = Intent(context, GetGradesBGService::class.java)

            val pintent = PendingIntent.getService(context, 0, intent, 0)

            val alarm = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
            alarm.setRepeating(AlarmManager.RTC_WAKEUP, cal.timeInMillis,
                    (RUN_IN_HOURS * 60 * 60 * 1000).toLong(), pintent)
        }
    }
    override fun onHandleIntent(intent: Intent?) {
        // Gets data from the incoming Intent
        Handler(Looper.getMainLooper()).post {
            val dataString = intent!!.dataString
            User.update { isSuccess ->  }
        }
    }


}
