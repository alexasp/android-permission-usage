package com.aspis.permissionsmonitor;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.hardware.Camera;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

/**
 * Created by Alexander on 10/27/2014.
 */
public class MonitorAlarmReceiver extends BroadcastReceiver {

    private static final int NOTIFICATION_TAG = 12312;


    @Override
    public void onReceive(Context context, Intent intent) {

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        Log.d(Main.TAG, "MonitorIntent is running check.");

        Camera camera = null;
        boolean busyCamera = false;
        try {
            camera = Camera.open();
            camera.unlock();
        } catch (Exception e) {
            busyCamera = true;
        }

        if (camera != null)
            camera.release();

        if (busyCamera){
            Log.d(Main.TAG, "Camera is busy.");
            NotificationCompat.Builder notificationBuilder = createNotificationBuilder(context);
            notificationManager.notify(NOTIFICATION_TAG, notificationBuilder.build());
        }
        else
        {
            Log.d(Main.TAG, "Camera is available.");
            notificationManager.cancel(NOTIFICATION_TAG);
        }
    }

    private NotificationCompat.Builder createNotificationBuilder(Context context) {
        return new NotificationCompat.Builder(context)
                .setSmallIcon(R.drawable.camera)
                .setContentTitle("Permission monitor")
                .setContentText("The device camera is active.");
    }
}
