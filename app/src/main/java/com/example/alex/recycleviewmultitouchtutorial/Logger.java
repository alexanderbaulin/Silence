package com.example.alex.recycleviewmultitouchtutorial;


import android.util.Log;

public class Logger {
    public static String LOG_DATABASE = "test2";

    public static void log(String tag, String message) {
        Log.d(tag, message);
    }
}
