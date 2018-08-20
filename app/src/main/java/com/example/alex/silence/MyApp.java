package com.example.alex.silence;

import android.app.ActivityManager;
import android.content.Context;



public class MyApp extends android.app.Application {
    private static MyApp instance;
    private static ActivityManager am;

    @Override
    public void onCreate() {
        instance = this;
        am = (ActivityManager) instance.getSystemService(ACTIVITY_SERVICE);
        super.onCreate();
    }

    public static Context getAppContext() {
        return instance.getApplicationContext();
    }
}
