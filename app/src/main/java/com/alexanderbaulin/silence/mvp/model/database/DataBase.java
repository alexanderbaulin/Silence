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


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


import com.alexanderbaulin.silence.mvp.model.DataItem;

import java.util.LinkedList;


public class DataBase extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "data.db";
    private static final int DATABASE_VERSION = 1;

    public DataBase(Context context) {
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

    public long insert(DataItem dataItem) {
        SQLiteDatabase dp = getWritableDatabase();
        ContentValues cv = getValues(dataItem);
        long key = dp.insert(Table.TABLE_NAME, null, cv);
        dp.close();
        return key;
    }

    private ContentValues getValues(DataItem dataItem) {
        ContentValues cv = new ContentValues();
        cv.put(Table.COLUMN_DESCRIPTION, dataItem.description);
        cv.put(Table.COLUMN_TIME_BEGIN_HOUR, dataItem.timeBegin[0]);
        cv.put(Table.COLUMN_TIME_BEGIN_MINUTE, dataItem.timeBegin[1]);
        cv.put(Table.COLUMN_TIME_END_HOUR, dataItem.timeEnd[0]);
        cv.put(Table.COLUMN_TIME_END_MINUTE, dataItem.timeEnd[1]);
        cv.put(Table.COLUMN_CHECKED_DAYS, boolArrayToString(dataItem.checkedDays));
        cv.put(Table.COLUMN_IS_ALARM_ON, boolToInt(dataItem.isAlarmOn));
        cv.put(Table.COLUMN_IS_VIBRATION_ALLOWED, boolToInt(dataItem.isVibrationAllowed));
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

    public LinkedList<DataItem> select() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor c = db.rawQuery(Table.TABLE_SELECT, null);
        LinkedList<DataItem> data = getDataFrom(c);
        db.close();
        return data;
    }

    private LinkedList<DataItem> getDataFrom(Cursor cursor) {
        LinkedList<DataItem> data = new LinkedList<>();
        if (!cursor.moveToFirst()) return data;
        do {
            DataItem dataItem = getDataItem(cursor);
            data.add(dataItem);
        } while (cursor.moveToNext());
        cursor.close();
        return data;
    }

    private DataItem getDataItem(Cursor cursor) {
        DataItem dataItem = new DataItem();
        dataItem.id = cursor.getLong(cursor.getColumnIndex(Table.COLUMN_ID));
        dataItem.description = cursor.getString(cursor.getColumnIndex(Table.COLUMN_DESCRIPTION));
        dataItem.timeBegin[0] = cursor.getInt(cursor.getColumnIndex(Table.COLUMN_TIME_BEGIN_HOUR));
        dataItem.timeBegin[1] = cursor.getInt(cursor.getColumnIndex(Table.COLUMN_TIME_BEGIN_MINUTE));
        dataItem.timeEnd[0] = cursor.getInt(cursor.getColumnIndex(Table.COLUMN_TIME_END_HOUR));
        dataItem.timeEnd[1] = cursor.getInt(cursor.getColumnIndex(Table.COLUMN_TIME_END_MINUTE));
        String string = cursor.getString(cursor.getColumnIndex(Table.COLUMN_CHECKED_DAYS));
        dataItem.checkedDays = stringToBoolArray(string);
        dataItem.isAlarmOn = intToBool(cursor.getInt(cursor.getColumnIndex(Table.COLUMN_IS_ALARM_ON)));
        dataItem.isVibrationAllowed = intToBool(cursor.getInt(cursor.getColumnIndex(Table.COLUMN_IS_VIBRATION_ALLOWED)));
        return dataItem;
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

    public int update(long id, DataItem item) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = getValues(item);
        int updatedRowsCount = db.update(Table.TABLE_NAME, cv, "_id = ?", new String[]{String.valueOf(id)});
        db.close();
        return updatedRowsCount;
    }
}