package lt.welovedotnot.ktu_ais_app.views.notifications;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;

import lt.welovedotnot.ktu_ais_app.R;
import lt.welovedotnot.ktu_ais_app.api.models.GradeModel;
import lt.welovedotnot.ktu_ais_app.api.models.WeekModel;
import lt.welovedotnot.ktu_ais_app.views.activities.LoginActivity;

import static android.content.Context.NOTIFICATION_SERVICE;

/**
 * Created by Vartotojas on 2017-05-06.
 */

public class KTUNotification {

    public void sendGradeNotification(GradeModel model, Context context) {
        String title = "Gavai " + model.getMark();
        String text = model.getName() + ", " + model.getTypeId();
        sendNotification(title, text, context);
    }

    public void sendUpcomingTestNotification(WeekModel model, Context context) {
        String title = "Kitos savaitės atsiskaitymai";
        String text = "";
        StringBuilder mStringBuilder = new StringBuilder();

        for (int i = 0; i < model.getGrades().size(); i++) {
            text = mStringBuilder.append(model.getGrades().get(i).getName()).append(", ").append(model.getGrades().get(i).
                    getTypeId()).append(System.getProperty("line.separator")).toString();
        }

        sendNotification(title, text, context);
    }

    private void sendNotification(String title, String text, Context context) {
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(context)
                        .setSmallIcon(R.drawable.ic_priority_high_black_48dp)
                        .setContentTitle(title)
                        .setContentText(text);
        Intent resultIntent = new Intent(context, LoginActivity.class);

        // The stack builder object will contain an artificial back stack for the
        // started Activity.
        // This ensures that navigating backward from the Activity leads out of
        // your application to the Home screen.
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        // Adds the back stack for the Intent (but not the Intent itself)
        stackBuilder.addParentStack(LoginActivity.class);
        // Adds the Intent that starts the Activity to the top of the stack
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(
                        0,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );
        mBuilder.setContentIntent(resultPendingIntent);
        NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
        // mId allows you to update the notification later on.
        mNotificationManager.notify(1, mBuilder.build());
    }
}
