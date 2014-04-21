
package com.xiaomi.alertsystem.utils;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Vibrator;

import com.xiaomi.alertsystem.R;

public class NotificationUtils {
    private static final int NOTIFICATION_SOUND_INTERVAL_MSEC = 5 * 1000; // 5秒钟内不要再重响

    private static long mLastSoundTime = 0;

    public static void doNotify(Intent notificationIntent, Context context,
                                CharSequence tickerText, CharSequence contentTitle, CharSequence contentText,
                                boolean notificationSound, boolean vibrate, int buddyId, int id) {
        doNotify(notificationIntent, context,
                tickerText, contentTitle, contentText,
                notificationSound, vibrate, buddyId, id, id);
    }


    public static void doNotify(Intent notificationIntent, Context context,
                                CharSequence tickerText, CharSequence contentTitle, CharSequence contentText,
                                boolean notificationSound, boolean vibrate, int buddyId, int id, int requestCode) {
        PendingIntent contentIntent = PendingIntent.getActivity(context, requestCode, notificationIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        Notification notification = new Notification(R.drawable.smiley_tab_mao_pressed, tickerText,
                System.currentTimeMillis());

        notification.setLatestEventInfo(context, contentTitle, contentText, contentIntent);
        notification.flags |= Notification.FLAG_AUTO_CANCEL;

        long now = System.currentTimeMillis();
        if (now - mLastSoundTime > NOTIFICATION_SOUND_INTERVAL_MSEC) {

            if (notificationSound) {
                notification.defaults |= Notification.DEFAULT_SOUND;
            }

            if (vibrate) {
                Vibrator vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
                if (vibrator != null) {
                    vibrator.vibrate(500);
                }
            }
            notification.defaults |= Notification.DEFAULT_LIGHTS;
            notification.flags |= Notification.FLAG_SHOW_LIGHTS;
            notification.ledARGB = Color.GREEN;
            notification.ledOnMS = 7;
            notification.ledOffMS = 15 * 1000;

            mLastSoundTime = now;
        }

        NotificationManager notifManager = (NotificationManager) context
                .getSystemService(Context.NOTIFICATION_SERVICE);
        notifManager.notify(id, notification);
    }
}
