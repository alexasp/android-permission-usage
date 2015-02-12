package com.aspis.permissionsmonitor;

import android.app.IntentService;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.hardware.Camera;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Alexander on 8/8/14.
 */
public class MonitorService extends IntentService {
    private static final int NOTIFICATION_TAG = 12312;

    private final NotificationCompat.Builder _notificationBuilder;
    private NotificationManager _notificationManager;
    private TimerTask _timedTask;
    private Timer _timer;

    @Override
    public void onCreate() {
        super.onCreate();
        _notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
    }

    public MonitorService() {
        super("Permission monitor service");
        _timer = new Timer();

        _notificationBuilder = createNotificationBuilder();

        _timedTask = new TimerTask() {
            @Override
            public void run() {

                Log.d(Main.TAG, "TimerTask is running check.");

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
                    _notificationManager.notify(NOTIFICATION_TAG, _notificationBuilder.build());
                }
                else
                {
                    Log.d(Main.TAG, "Camera is available.");
                    _notificationManager.cancel(NOTIFICATION_TAG);
                }

            }
        };
    }

    private NotificationCompat.Builder createNotificationBuilder() {
        return new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.camera)
                .setContentTitle("Permission monitor")
                .setContentText("The device camera is active.");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.d(Main.TAG, "Permission monitor started.");
        _timer.scheduleAtFixedRate(_timedTask, 0, 3000);
        while(true);
    }

    @Override
    public void onDestroy() {
        _timer.cancel();
        super.onDestroy();
    }
}
