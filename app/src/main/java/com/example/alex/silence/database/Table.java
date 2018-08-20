package com.example.alex.silence.database;


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
