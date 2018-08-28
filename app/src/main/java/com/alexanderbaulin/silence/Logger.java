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
