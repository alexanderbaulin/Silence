package com.example.alex.silence;


import android.util.Log;

import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;

public class Logger {
    public static String LOG_DATABASE = "test2";
    private static String LOG = "myLogs1";

    public static void log(String tag, String message) {
        Log.d(tag, message);
    }

    static void log(String s) {
        Log.d(LOG, s);
    }

    static void log(long time, String message) {
        Date date = new Date(time);
        DateFormat df = DateFormat.getDateInstance(DateFormat.SHORT, Locale.UK);
        DateFormat tf = DateFormat.getTimeInstance(DateFormat.SHORT, Locale.UK);
        String formattedDate = df.format(date);
        String formattedTime = tf.format(date);
        Logger.log(message + " " + formattedDate + " " + formattedTime);
    }
}
