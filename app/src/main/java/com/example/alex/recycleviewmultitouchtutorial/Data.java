package com.example.alex.recycleviewmultitouchtutorial;


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
    boolean isSelected;

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

    private Data(Parcel in) {
        description = in.readString();
        isSelected = in.readByte() != 0;
        isAlarmOn = in.readByte() != 0;
        checkedDays = in.createBooleanArray();
        isVibrationAllowed = in.readByte() != 0;
        timeBegin = in.createIntArray();
        timeEnd = in.createIntArray();
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
}
