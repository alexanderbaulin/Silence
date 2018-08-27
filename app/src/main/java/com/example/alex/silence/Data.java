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


import android.os.Parcel;
import android.os.Parcelable;

import java.util.Arrays;

public class Data implements Parcelable {
    public long id;
    public String description;
    public int[] timeBegin;
    public int[] timeEnd;
    public boolean[] checkedDays;
    public boolean isVibrationAllowed;
    public boolean isAlarmOn;
    public boolean isSelected;

    public Data() {
        checkedDays = new boolean[7];
        timeBegin = new int[2];
        timeEnd = new int[2];
        isAlarmOn = true;
    }

    Data(int[] from, int[] until, boolean[] daysOfWeek, boolean isActivated) {
        this.isAlarmOn = isActivated;
        checkedDays = daysOfWeek;
        timeBegin = from;
        timeEnd = until;
    }

    public static final Parcelable.Creator<Data> CREATOR = new Parcelable.Creator<Data>() {
        @Override
        public Data createFromParcel(Parcel in) {
            return new Data(in);
        }

        @Override
        public Data[] newArray(int size) {
            return new Data[size];
        }
    };

    @Override
    public String toString() {
        return  id + " " +
                description + " " +
                timeBegin[0] + ":" + timeBegin[1] + " " +
                timeEnd[0] + ":" + timeEnd[1] + " " +
                Arrays.toString(checkedDays) + " " +
                isAlarmOn + " " +
                isVibrationAllowed;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(description);
        dest.writeByte((byte) (isSelected ? 1 : 0));
        dest.writeByte((byte) (isAlarmOn ? 1 : 0));
        dest.writeBooleanArray(checkedDays);
        dest.writeByte((byte) (isVibrationAllowed ? 1 : 0));
        dest.writeIntArray(timeBegin);
        dest.writeIntArray(timeEnd);
    }

    static boolean[] getCheckedDaysFromToday(boolean[] checkedDays, int todayIndex) {
        boolean[] result = new boolean[7];
        int index = 0;
        for(int i = todayIndex; i < checkedDays.length; ++i) {
            result[index++] = checkedDays[i];
        }
        for(int i = 0; i < todayIndex; ++i) {
            result[index++] = checkedDays[i];
        }
        return result;
    }

    private Data(Parcel in) {
        description = in.readString();
        isSelected = in.readByte() != 0;
        isAlarmOn = in.readByte() != 0;
        checkedDays = in.createBooleanArray();
        isVibrationAllowed = in.readByte() != 0;
        timeBegin = in.createIntArray();
        timeEnd = in.createIntArray();
    }
}
