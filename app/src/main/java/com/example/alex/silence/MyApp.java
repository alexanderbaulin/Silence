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
