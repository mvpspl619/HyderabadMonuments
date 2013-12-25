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
    public double latitude;
    public double longitude;

    Monument(Parcel in){
        readFromParcel(in);
    }

    public Monument(String name, int drawable, String description, double latitude, double longitude){
        this.name = name;
        this.drawable = drawable;
        this.description = description;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getName()
    {
        return name;
    }

    public double getLatitude() { return latitude; }

    public double getLongitude() { return longitude; }
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
        latitude = in.readDouble();
        longitude = in.readDouble();
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
        parcel.writeDouble(latitude);
        parcel.writeDouble(longitude);
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

