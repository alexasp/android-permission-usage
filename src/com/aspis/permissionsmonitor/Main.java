package com.aspis.permissionsmonitor;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class Main extends Activity {
    public   static final String TAG = "Permission Monitor";
    private Button _toggleButton;
    private boolean _monitorOn;
    private Intent _intent;
    private AlarmManager _alarmManager;
    private PendingIntent _alarmIntent;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        _monitorOn = false;

        _intent = new Intent(this, MonitorService.class);

        _toggleButton = (Button) findViewById(R.id.monitor_toggle_button);
        _toggleButton.setOnClickListener(new MonitorToggleButtonListener());
    }

    private AlarmManager setUpAlarmManager() {
        AlarmManager manager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, MonitorAlarmReceiver.class);
        _alarmIntent = PendingIntent.getBroadcast(this, 0, intent, 0);
        manager.setExact(AlarmManager.ELAPSED_REALTIME, 5*1000, _alarmIntent);
        return manager;
    }

    private class MonitorToggleButtonListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            if(_monitorOn){
                _monitorOn = false;
                _toggleButton.setText("Monitor off.");
                if(_alarmManager != null)
                    _alarmManager.cancel(_alarmIntent);
                //stopService(_intent);
            }
            else{
                _monitorOn = true;
                Log.d(TAG, "Starting monitoring service.");
                _toggleButton.setText("Monitor on.");
                _alarmManager = setUpAlarmManager();
                //startService(_intent);
            }
        }
    }
}
