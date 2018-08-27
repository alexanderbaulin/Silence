package com.example.alex.silence.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.example.alex.silence.Alarm;
import com.example.alex.silence.Data;
import com.example.alex.silence.MyApp;
import com.example.alex.silence.database.Base;

import java.util.List;


public class AlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (action == null) return;
        Alarm alarm = new Alarm();
        switch (action) {
            case Intent.ACTION_BOOT_COMPLETED:
                Base base = new Base(MyApp.getAppContext());
                List<Data> data = base.select();
                for (Data dataItem : data) {
                    if (dataItem.isAlarmOn) {
                        alarm.setAlarm(dataItem, data.indexOf(dataItem));
                    }
                }
                break;
            case "vibration":
                alarm.setVibrationMode();
                alarm.repeatAlarm(intent);
                break;
            case "noSound":
                alarm.setSilentMode();
                alarm.repeatAlarm(intent);
                break;
            case "normalMode":
                alarm.setNormalMode();
                alarm.repeatAlarm(intent);
                break;
        }
    }
}