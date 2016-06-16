package br.edu.fa7.pomodorofa7.broadcast;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import br.edu.fa7.pomodorofa7.R;
import br.edu.fa7.pomodorofa7.activity.MainActivity;

/**
 * Created by MF on 14/06/16.
 */
public class PomodoroBroadcastReceiver extends BroadcastReceiver {

    private static final int NOTIFICATION_ID = 1;
    private static final int NOTIFICATION_PENDING_INTENT = 1;

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();

        if(("POMODORO_ALARM").equals(action)) {

            Notification.Builder builder = new Notification.Builder(context)
                    .setSmallIcon(R.drawable.app_icon_pomodoro)
                    .setContentTitle("Pomodoro Notification")
                    .setContentText("Seu pomodoro acabou!!!")
                    .setAutoCancel(true);

            Intent it = new Intent(context, MainActivity.class);

            TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
            stackBuilder.addParentStack(MainActivity.class);
            stackBuilder.addNextIntent(it);

            PendingIntent pendingIntent = stackBuilder.getPendingIntent(NOTIFICATION_PENDING_INTENT, PendingIntent.FLAG_UPDATE_CURRENT);

            builder.setContentIntent(pendingIntent);

            NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.notify(NOTIFICATION_ID, builder.build());

        }
    }
}
