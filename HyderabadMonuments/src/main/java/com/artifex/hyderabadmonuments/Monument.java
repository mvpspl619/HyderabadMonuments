package com.artifex.hyderabadmonuments;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by Home on 14/12/13.
 */
public class Monument implements Parcelable
{
    public String name;
    public int drawable;
    public String description;

    Monument(Parcel in){
        readFromParcel(in);
    }

    public Monument(String name, int drawable, String description){
        this.name = name;
        this.drawable = drawable;
        this.description = description;
    }

    public String getName()
    {
        return name;
    }

    public int getDrawable()
    {
        return drawable;
    }

    public String getDescription()
    {
        return description;
    }

    private void readFromParcel(Parcel in){
        name = in.readString();
        drawable = in.readInt();
        description = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(name);
        parcel.writeInt(drawable);
        parcel.writeString(description);
    }

    public static Creator<Monument> CREATOR = new Creator<Monument>() {
        @Override
        public Monument createFromParcel(Parcel parcel) {
            return new Monument(parcel);
        }

        @Override
        public Monument[] newArray(int i) {
            return new Monument[0];
        }
    };
}

