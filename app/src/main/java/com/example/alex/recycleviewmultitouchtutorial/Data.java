package com.example.alex.recycleviewmultitouchtutorial;

import android.os.Parcel;
import android.os.Parcelable;


public class Data implements Parcelable {
    String description;
    boolean isSelected;
    boolean isChecked;
    boolean[] checkedDays;
    boolean isVibrationAllowed;
    int[] timeFrom;
    int[] timeUntil;

    Data() {
        checkedDays = new boolean[7];
        timeFrom = new int[2];
        timeUntil = new int[2];
        isChecked = true;
    }

    Data(int[] from, int[] until, boolean[] daysOfWeek, boolean b) {
        isChecked = b;
        checkedDays = daysOfWeek;
        timeFrom = from;
        timeUntil = until;
    }

    private Data(Parcel in) {
        description = in.readString();
        isSelected = in.readByte() != 0;
        isChecked = in.readByte() != 0;
        checkedDays = in.createBooleanArray();
        isVibrationAllowed = in.readByte() != 0;
        timeFrom = in.createIntArray();
        timeUntil = in.createIntArray();
    }

    public static final Creator<Data> CREATOR = new Creator<Data>() {
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
        return "timeFrom hour = " + timeFrom[0] + " timeFrom = " + timeFrom[1];
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(description);
        dest.writeByte((byte) (isSelected ? 1 : 0));
        dest.writeByte((byte) (isChecked ? 1 : 0));
        dest.writeBooleanArray(checkedDays);
        dest.writeByte((byte) (isVibrationAllowed ? 1 : 0));
        dest.writeIntArray(timeFrom);
        dest.writeIntArray(timeUntil);
    }
}
