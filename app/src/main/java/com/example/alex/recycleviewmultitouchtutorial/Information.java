package com.example.alex.recycleviewmultitouchtutorial;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Alex on 23.04.2018.
 */

public class Information implements Parcelable {
    boolean isSelected;
    int image;
    String text;
    Information(int image, String text) {
        this.image = image;
        this.text = text;
    }

    private Information(Parcel in) {
       // isSelected = in.readByte() != 0;
        image = in.readInt();
        text = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(image);
        dest.writeString(text);
    }

    public static final Creator<Information> CREATOR = new Creator<Information>() {
        @Override
        public Information createFromParcel(Parcel in) {
            return new Information(in);
        }

        @Override
        public Information[] newArray(int size) {
            return new Information[size];
        }
    };
}
