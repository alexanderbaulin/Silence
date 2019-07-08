/*
* Silence for Android OS
* Copyright 2018 Alexander Baulin
* Contacts: alexander.baulin.github@yandex.ru
*
* This program is free software: you can redistribute it and/or modify
* it under the terms of the GNU General Public License as published by
* the Free Software Foundation, either version 3 of the License, or
* (at your option) any later version.
*
* This program is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
* GNU General Public License for more details.
*
* You should have received a copy of the GNU General Public License
* along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/

package com.alexanderbaulin.silence.mvp.presenter.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.alexanderbaulin.silence.mvp.model.Alarm;
import com.alexanderbaulin.silence.mvp.model.DataItem;
import com.alexanderbaulin.silence.MyApp;
import com.alexanderbaulin.silence.mvp.model.database.Base;

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
                List<DataItem> data = base.select();
                for (DataItem dataItem : data) {
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