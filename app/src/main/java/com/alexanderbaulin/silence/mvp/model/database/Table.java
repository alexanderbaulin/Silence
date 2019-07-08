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

package com.alexanderbaulin.silence.mvp.model.database;


class Table {
    static final String TABLE_NAME = "dataItems";

    static final String COLUMN_ID = "_id";
    static final String COLUMN_DESCRIPTION = "description";
    static final String COLUMN_TIME_BEGIN_HOUR = "timeBeginHour";
    static final String COLUMN_TIME_BEGIN_MINUTE = "timeBeginMinute";
    static final String COLUMN_TIME_END_HOUR = "timeEndHour";
    static final String COLUMN_TIME_END_MINUTE = "timeEndMinute";
    static final String COLUMN_CHECKED_DAYS = "checkedDays";
    static final String COLUMN_IS_ALARM_ON = "isAlarmOn";
    static final String COLUMN_IS_VIBRATION_ALLOWED = "isVibration";

    static final String TABLE_CREATE =
            "CREATE TABLE " + TABLE_NAME + " ("
                    + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + COLUMN_DESCRIPTION + " TEXT, "
                    + COLUMN_TIME_BEGIN_HOUR + " INTEGER, "
                    + COLUMN_TIME_BEGIN_MINUTE + " INTEGER, "
                    + COLUMN_TIME_END_HOUR + " INTEGER, "
                    + COLUMN_TIME_END_MINUTE + " INTEGER, "
                    + COLUMN_CHECKED_DAYS + " TEXT, "
                    + COLUMN_IS_VIBRATION_ALLOWED + " INTEGER, "
                    + COLUMN_IS_ALARM_ON + " INTEGER)";

    static final String TABLE_DROP = "DROP TABLE IF EXISTS " + TABLE_NAME + ";";
    static final String TABLE_SELECT = "SELECT * FROM " + TABLE_NAME;
}
