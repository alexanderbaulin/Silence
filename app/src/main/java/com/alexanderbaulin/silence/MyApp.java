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

import android.app.ActivityManager;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.alexanderbaulin.silence.silence.R;


public class MyApp extends android.app.Application {
    private static MyApp instance;
    private static ActivityManager am;
    private static NotificationManager notificationManager;

    @Override
    public void onCreate() {
        instance = this;
        am = (ActivityManager) instance.getSystemService(ACTIVITY_SERVICE);
        notificationManager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
        super.onCreate();
    }

    public static Context getAppContext() {
        return instance.getApplicationContext();
    }

    public static boolean isNotificationPolicyAccessGranted() {
        return notificationManager.isNotificationPolicyAccessGranted();
    }

    public static void requestNotificationAccess() {
        Toast.makeText(instance, instance.getResources().getString(R.string.ask_permission), Toast.LENGTH_SHORT).show();
        Intent settingAccessPolicy = new Intent(android.provider.Settings.ACTION_NOTIFICATION_POLICY_ACCESS_SETTINGS);
        instance.startActivity(settingAccessPolicy);
    }

}
