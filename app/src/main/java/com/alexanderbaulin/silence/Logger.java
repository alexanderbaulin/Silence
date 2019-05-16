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

import com.alexanderbaulin.silence.silence.BuildConfig;

import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;

public class Logger {
    static private boolean LOG = BuildConfig.DEBUG;

    public static void i(String tag, String string) {
        if (LOG) Log.i(tag, string);
    }

    public static void e(String tag, String string) {
        if (LOG) Log.e(tag, string);
    }

    public static void d(String tag, String string) {
        if (LOG) Log.d(tag, string);
    }

    public static void v(String tag, String string) {
        if (LOG) Log.v(tag, string);
    }

    public static void w(String tag, String string) {
        if (LOG) Log.w(tag, string);
    }

    static String createMessage(long time, String message) {
        Date date = new Date(time);
        DateFormat df = DateFormat.getDateInstance(DateFormat.SHORT, Locale.UK);
        DateFormat tf = DateFormat.getTimeInstance(DateFormat.SHORT, Locale.UK);
        String formattedDate = df.format(date);
        String formattedTime = tf.format(date);
        return message + " " + formattedDate + " " + formattedTime;
    }
}
