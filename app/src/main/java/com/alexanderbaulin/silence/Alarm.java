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

package com.alexanderbaulin.silence;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
//import android.util.Log;



//import java.util.Arrays;
import com.alexanderbaulin.silence.receivers.AlarmReceiver;

import java.util.Calendar;

import static android.content.Context.ALARM_SERVICE;


public class Alarm {

    private AlarmManager manager;
    private AudioManager am;
    private final long WEEK_INTERVAL = AlarmManager.INTERVAL_DAY * 7;
    private int requestCode;

    public Alarm() {
        manager = (AlarmManager) MyApp.getAppContext().getSystemService(ALARM_SERVICE);
        am = (AudioManager) MyApp.getAppContext().getSystemService(Context.AUDIO_SERVICE);
    }

    public void setAlarm(Data dataItem, int index) {
        //Toast.makeText(MyApp.getAppContext(), "startAlarm", Toast.LENGTH_SHORT).show();
        int daysInWeek = 7;
        int requestCodesInDataItem = daysInWeek * 2;
        requestCode = index * requestCodesInDataItem;
        long timeStart = getStartTime(dataItem);
        long timeEnd = getEndTime(dataItem);
        int startHour = dataItem.timeBegin[0];
        int endHour2 = dataItem.timeEnd[0];
        if (startHour > endHour2) {
            //Log.d("myLogs1", "startHour > endHour");
            timeEnd = timeEnd + AlarmManager.INTERVAL_DAY;
        }
        long timeNow = getTime();

        //Logger.log(timeNow, "time now");
        //Logger.log(timeStart, "time start");
        //Logger.log(timeEnd, "time end");

        //Logger.log("todayIndex " + getTodayDayIndex());

        boolean[] checkedDays = Data.getCheckedDaysFromToday(dataItem.checkedDays, getTodayDayIndex());
        boolean isTodayChecked = checkedDays[0];
        boolean isYesterdayChecked = checkedDays[6];

        int beginHour = dataItem.timeBegin[0];
        int endHour = dataItem.timeEnd[0];

        int dayOfWeekStartIndex = 0;
        int dayOfWeekEndIndex = 7;

        if ((isYesterdayChecked) && (beginHour > endHour) && (timeNow < getEndTime(dataItem))) {
            //Log.d("myLogs1", "(isYesterdayChecked) && (beginHour > endHour) && (timeNow < getEndTime(dataItem)");
            long testTimeEnd = getTime(dataItem.timeEnd[0], dataItem.timeEnd[1]);
            long testTimeStart = getStartTime(dataItem) - AlarmManager.INTERVAL_DAY;
            setSoundMode(dataItem.isVibrationAllowed);
            setAlarm(testTimeStart + WEEK_INTERVAL, getStartModeIntent(dataItem));
            setAlarm(testTimeEnd, getEndModeIntent(dataItem));

            dayOfWeekStartIndex = 0;
            dayOfWeekEndIndex = 6;
        } else if (isTodayChecked) {
            if ((timeStart < timeNow) && (timeNow < timeEnd)) {
                //Logger.log("timeStart < timeNow < timeEnd");
                setSoundMode(dataItem.isVibrationAllowed);
                setAlarm(timeStart + WEEK_INTERVAL, getStartModeIntent(dataItem));
                setAlarm(timeEnd, getEndModeIntent(dataItem));
            } else if (timeEnd < timeNow) {
                //Logger.log("timeEnd < timeNow");
                setAlarm(timeStart + WEEK_INTERVAL, getStartModeIntent(dataItem));
                setAlarm(timeEnd + WEEK_INTERVAL, getEndModeIntent(dataItem));
            } else if (timeNow < timeStart) {
                //Logger.log("timeNow < timeStart");
                setAlarm(timeStart, getStartModeIntent(dataItem));
                setAlarm(timeEnd, getEndModeIntent(dataItem));
            }
            timeStart += AlarmManager.INTERVAL_DAY;
            timeEnd += AlarmManager.INTERVAL_DAY;

            dayOfWeekStartIndex = 1;
            dayOfWeekEndIndex = 7;
        }
        for (int i = dayOfWeekStartIndex; i < dayOfWeekEndIndex; i++) {
            boolean isDayOfWeekChecked = checkedDays[i];
            //Logger.log("checkedDay " + isDayOfWeekChecked);
            if (isDayOfWeekChecked) {
                setAlarm(timeStart, getStartModeIntent(dataItem));
                setAlarm(timeEnd, getEndModeIntent(dataItem));
            }
            timeStart += AlarmManager.INTERVAL_DAY;
            timeEnd += AlarmManager.INTERVAL_DAY;
        }
        requestCode = 0;
        //Log.d("myLogs", "days = " + Arrays.toString(checkedDays));
    }

    private void setSoundMode(boolean isVibrationAllowed) {
        if (isVibrationAllowed) {
            setVibrationMode();
        } else {
            setSilentMode();
        }
    }

    public void setSilentMode() {
        am.setRingerMode(AudioManager.RINGER_MODE_SILENT);
        //Log.d("myLogs1", "noSound");
    }

    public void setVibrationMode() {
        am.setRingerMode(AudioManager.RINGER_MODE_VIBRATE);
        //Log.d("myLogs1", "vibrateMode");
    }

    public void setNormalMode() {
        am.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
    }

    public void repeatAlarm(Intent i) {
        int requestCode = i.getIntExtra("code", 0);
        int hour = i.getIntExtra("hour", 0);
        int minute = i.getIntExtra("minute", 0);
        long currentTime = getTime(hour, minute);

        //Log.d("myLogs1", "repeatAlarm request code = " + requestCode);
        setAlarm(currentTime + WEEK_INTERVAL, i);
    }

    public void cancel(Data dataItem, int index) {
        int requestCodesInDataItem = 7 * 2;
        int requestCode = index * requestCodesInDataItem;
        int daysInWeek = 7;
        long timeStart = getStartTime(dataItem);
        long timeEnd = getEndTime(dataItem);
        long timeNow = getTime();
        int startHour = dataItem.timeBegin[0];
        int endHour = dataItem.timeEnd[0];
        if (startHour > endHour) {
            //Log.d("myLogs1", "startHour > endHour");
            timeEnd = timeEnd + AlarmManager.INTERVAL_DAY;
        }
        boolean[] checkedDays = Data.getCheckedDaysFromToday(dataItem.checkedDays, getTodayDayIndex());
        boolean isTodayChecked = checkedDays[0];
        boolean isYesterdayChecked = checkedDays[6];
        if ((isYesterdayChecked) && (startHour > endHour) && (timeNow < getEndTime(dataItem))) {
            setNormalMode();
        } else if ((isTodayChecked) && (timeStart < timeNow) && (timeNow < timeEnd))
            setNormalMode();

        for (int i = 0; i < daysInWeek; i++) {
            manager.cancel(getPendingIntent(++requestCode, getStartModeIntent(dataItem)));
            //Log.d("myLogs1", "cancel pi, requestCode = " + requestCode);
            manager.cancel(getPendingIntent(++requestCode, getEndModeIntent(dataItem)));
            //Log.d("myLogs1", "cancel pi, requestCode = " + requestCode);
        }
    }

    private void setAlarm(long time, Intent intent) {
        manager.setExactAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP,
                time,
                getPendingIntent(intent));
        //Logger.log(time, "start, requestCode = " + intent.getIntExtra("code", 0));
    }


    private long getEndTime(Data dataItem) {
        return getTime(dataItem.timeEnd[0], dataItem.timeEnd[1]);
    }

    private long getStartTime(Data dataItem) {
        return getTime(dataItem.timeBegin[0], dataItem.timeBegin[1]);
    }

    private Intent getStartModeIntent(Data dataItem) {
        Intent i = new Intent(MyApp.getAppContext(), AlarmReceiver.class);
        if (dataItem.isVibrationAllowed) {
            i.setAction("vibration");
        } else {
            i.setAction("noSound");
        }
        i.putExtra("code", ++requestCode);
        i.putExtra("hour", dataItem.timeBegin[0]);
        i.putExtra("minute", dataItem.timeBegin[1]);
        return i;
    }

    private Intent getEndModeIntent(Data dataItem) {
        Intent i = new Intent(MyApp.getAppContext(), AlarmReceiver.class);
        i.setAction("normalMode");
        i.putExtra("code", ++requestCode);
        i.putExtra("hour", dataItem.timeEnd[0]);
        i.putExtra("minute", dataItem.timeEnd[1]);
        return i;
    }

    private long getTime(int hour, int minute) {
        Calendar now = Calendar.getInstance();
        now.set(Calendar.HOUR_OF_DAY, hour);
        now.set(Calendar.MINUTE, minute);
        now.set(Calendar.SECOND, 0);
        now.set(Calendar.MILLISECOND, 0);
        return now.getTimeInMillis();
    }

    private long getTime() {
        Calendar now = Calendar.getInstance();
        return now.getTimeInMillis();
    }

    private int getTodayDayIndex() {
        Calendar now = Calendar.getInstance();
        int today = now.get(Calendar.DAY_OF_WEEK);
        return getDayIndex(today);
    }

    private int getDayIndex(int day) {
        if (day == Calendar.SUNDAY) return 6;
        else return day - 2;
    }

    private PendingIntent getPendingIntent(Intent i) {
        return PendingIntent.getBroadcast(MyApp.getAppContext(), requestCode, i, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_ONE_SHOT);
    }

    private PendingIntent getPendingIntent(int requestCode, Intent i) {
        return PendingIntent.getBroadcast(MyApp.getAppContext(), requestCode, i, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_ONE_SHOT);
    }

}
