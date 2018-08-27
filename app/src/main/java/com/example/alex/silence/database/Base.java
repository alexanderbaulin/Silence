package com.example.alex.silence.database;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


import com.example.alex.silence.Data;

import java.util.LinkedList;


public class Base extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "data.db";
    private static final int DATABASE_VERSION = 1;

    public Base(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(Table.TABLE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(Table.TABLE_DROP);
        db.execSQL(Table.TABLE_CREATE);
    }

    private SQLiteDatabase getWritableBase() {
        return this.getWritableDatabase();
    }

    public void create() {
        getWritableBase().execSQL(
                Table.TABLE_CREATE
        );
    }

    public void drop() {
        getWritableBase().execSQL(
                Table.TABLE_DROP
        );
    }

    public long insert(Data data) {
        SQLiteDatabase dp = getWritableDatabase();
        ContentValues cv = getValues(data);
        long key = dp.insert(Table.TABLE_NAME, null, cv);
        dp.close();
        return key;
    }

    private ContentValues getValues(Data data) {
        ContentValues cv = new ContentValues();
        cv.put(Table.COLUMN_DESCRIPTION, data.description);
        cv.put(Table.COLUMN_TIME_BEGIN_HOUR, data.timeBegin[0]);
        cv.put(Table.COLUMN_TIME_BEGIN_MINUTE, data.timeBegin[1]);
        cv.put(Table.COLUMN_TIME_END_HOUR, data.timeEnd[0]);
        cv.put(Table.COLUMN_TIME_END_MINUTE, data.timeEnd[1]);
        cv.put(Table.COLUMN_CHECKED_DAYS, boolArrayToString(data.checkedDays));
        cv.put(Table.COLUMN_IS_ALARM_ON, boolToInt(data.isAlarmOn));
        cv.put(Table.COLUMN_IS_VIBRATION_ALLOWED, boolToInt(data.isVibrationAllowed));
        return cv;
    }

    private String boolArrayToString(boolean[] checkedDays) {
        StringBuilder builder = new StringBuilder();
        for (boolean isCheckedDay : checkedDays) {
            builder.append(boolToString(isCheckedDay));
        }
        return builder.toString();
    }

    private char boolToString(boolean isDayChecked) {
        return isDayChecked ? '1' : '0';
    }

    private int boolToInt(boolean value) {
        return value ? 1 : 0;
    }

    public LinkedList<Data> select() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor c = db.rawQuery(Table.TABLE_SELECT, null);
        LinkedList<Data> data = getDataFrom(c);
        db.close();
        return data;
    }

    private LinkedList<Data> getDataFrom(Cursor cursor) {
        LinkedList<Data> data = new LinkedList<>();
        if (!cursor.moveToFirst()) return data;
        do {
            Data dataItem = getDataItem(cursor);
            data.add(dataItem);
        } while (cursor.moveToNext());
        cursor.close();
        return data;
    }

    private Data getDataItem(Cursor cursor) {
        Data data = new Data();
        data.id = cursor.getLong(cursor.getColumnIndex(Table.COLUMN_ID));
        data.description = cursor.getString(cursor.getColumnIndex(Table.COLUMN_DESCRIPTION));
        data.timeBegin[0] = cursor.getInt(cursor.getColumnIndex(Table.COLUMN_TIME_BEGIN_HOUR));
        data.timeBegin[1] = cursor.getInt(cursor.getColumnIndex(Table.COLUMN_TIME_BEGIN_MINUTE));
        data.timeEnd[0] = cursor.getInt(cursor.getColumnIndex(Table.COLUMN_TIME_END_HOUR));
        data.timeEnd[1] = cursor.getInt(cursor.getColumnIndex(Table.COLUMN_TIME_END_MINUTE));
        String string = cursor.getString(cursor.getColumnIndex(Table.COLUMN_CHECKED_DAYS));
        data.checkedDays = stringToBoolArray(string);
        data.isAlarmOn = intToBool(cursor.getInt(cursor.getColumnIndex(Table.COLUMN_IS_ALARM_ON)));
        data.isVibrationAllowed = intToBool(cursor.getInt(cursor.getColumnIndex(Table.COLUMN_IS_VIBRATION_ALLOWED)));
        return data;
    }

    private boolean intToBool(int value) {
        return (value == 1);
    }

    private boolean[] stringToBoolArray(String str) {
        boolean[] result = new boolean[7];
        for (int index = 0; index < result.length; index++) {
            result[index] = charToBool(str.charAt(index));
        }
        return result;
    }

    private boolean charToBool(char c) {
        return (c == '1');
    }

    public int delete(long id) {
        SQLiteDatabase db = getWritableDatabase();
        int deletedRowsCount = db.delete(Table.TABLE_NAME, "_id = " + id, null);
        db.close();
        return deletedRowsCount;
    }

    public int update(long id, Data item) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = getValues(item);
        int updatedRowsCount = db.update(Table.TABLE_NAME, cv, "_id = ?", new String[]{String.valueOf(id)});
        db.close();
        return updatedRowsCount;
    }
}