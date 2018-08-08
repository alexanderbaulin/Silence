package com.example.alex.recycleviewmultitouchtutorial.receivers;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Log;

import com.example.alex.recycleviewmultitouchtutorial.Alarm;
import com.example.alex.recycleviewmultitouchtutorial.Data;
import com.example.alex.recycleviewmultitouchtutorial.MyApp;
import com.example.alex.recycleviewmultitouchtutorial.database.Base;

import java.util.List;

/**
 * Created by Alex on 07.08.2018.
 */

public class AlarmReceiver extends BroadcastReceiver {
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onReceive(Context context, Intent intent) {
        if(intent.getAction() == null) return;

        //  Log.d("myLogs", "on Receive");
        switch (intent.getAction()) {
            case Intent.ACTION_BOOT_COMPLETED:
                Log.d( "myLogs1", "boot completed");
                Alarm alarm = new Alarm();
                Base base = new Base(MyApp.getAppContext());
                List<Data> data = base.select();
                for(Data dataItem: data) {
                    alarm.setAlarm(dataItem, data.indexOf(dataItem));
                }
        }

        String action = intent.getAction();
        if(action != null) {
            if(action.equals("vibration")) {
                Log.d("myLogs1", "vibration");
                Alarm alarm = new Alarm();
                alarm.setVibrationMode();
                alarm.repeatAlarm(intent);
                }
            else if(action.equals("noSound")) {
                Log.d("myLogs1", "noSound");
                Alarm alarm = new Alarm();
                alarm.setSilentMode();
                alarm.repeatAlarm(intent);
            } else if(action.equals("normalMode")) {
                Log.d("myLogs1", "normalMode");
                Alarm alarm = new Alarm();
                alarm.setNormalMode();
                alarm.repeatAlarm(intent);
            } else if(action.equals("android.intent.action.TIME_SET")) {
                Log.d("myLogs1", "time changed");
            } else if(action.equals("android.intent.action.TIMEZONE_CHANGED")) {
                Log.d("myLogs1", "timezone changed");
            } else if(action.equals("android.intent.action.DATE_CHANGED")) {
                Log.d("myLogs1", "date changed");
            }
        }

    }

    private Data getData() {
        Data data = new Data();
        data.id = 1;
        data.description = "description";
        data.checkedDays = new boolean[]{true, true, true, true, true, true, true};
        data.timeBegin = new int[] { 11, 53 };
        data.timeEnd = new int[] { 12, 0 };
        data.isAlarmOn = true;
        data.isVibrationAllowed = false;
        return data;
    }
}
