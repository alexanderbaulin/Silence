package com.example.alex.recycleviewmultitouchtutorial;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.SparseBooleanArray;


public class Data implements Parcelable {
    boolean isSelected;
    boolean isChecked;
    String time, days;
    boolean[] checkedDays;

    Data(String periodOfTime, boolean[] daysOfWeek, boolean b) {
        time = periodOfTime;
        //days = daysOfWeek;
        isChecked = b;
        checkedDays = daysOfWeek;

    }

    private Data(Parcel in) {
        //isSelected = in.readByte() != 0;
        time = in.readString();
        days = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(time);
        dest.writeString(days);

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
}
