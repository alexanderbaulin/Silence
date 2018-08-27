package com.example.alex.silence.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Log;

import com.example.alex.silence.Alarm;
import com.example.alex.silence.Data;
import com.example.alex.silence.MyApp;
import com.example.alex.silence.database.Base;

import java.util.List;


public class AlarmReceiver extends BroadcastReceiver {
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (action == null) return;
        Alarm alarm = new Alarm();
        switch (action) {
            case Intent.ACTION_BOOT_COMPLETED:
                Log.d("myLogs1", "boot completed");
                Base base = new Base(MyApp.getAppContext());
                List<Data> data = base.select();
                for (Data dataItem : data) {
                    if (dataItem.isAlarmOn) {
                        alarm.setAlarm(dataItem, data.indexOf(dataItem));
                    }
                }
                break;
            case "vibration":
                Log.d("myLogs1", "vibration");
                alarm.setVibrationMode();
                alarm.repeatAlarm(intent);
                break;
            case "noSound":
                Log.d("myLogs1", "noSound");
                alarm.setSilentMode();
                alarm.repeatAlarm(intent);
                break;
            case "normalMode":
                Log.d("myLogs1", "normalMode");
                alarm.setNormalMode();
                alarm.repeatAlarm(intent);
                break;
        }
    }
}