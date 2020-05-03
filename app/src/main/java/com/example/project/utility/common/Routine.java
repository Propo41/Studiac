package com.example.project.utility.common;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

public class Routine implements Parcelable{
    // private Map<String, String> mItems;
    private ArrayList<Schedule> mItems;

    public Routine() {
        mItems = new ArrayList<>();
    }

    protected Routine(Parcel in) {
        mItems = new ArrayList<>();
      //  in.readTypedList(mItems,Schedule.CREATOR);
        in.readList(mItems, Schedule.class.getClassLoader());

    }

    public static final Creator<Routine> CREATOR = new Creator<Routine>() {
        @Override
        public Routine createFromParcel(Parcel in) {
            return new Routine(in);
        }

        @Override
        public Routine[] newArray(int size) {
            return new Routine[size];
        }
    };

    public ArrayList<Schedule> getItems() {
        return mItems;
    }

    public void setItems(ArrayList<Schedule> items) {
        mItems = items;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
       // dest.writeTypedList(mItems);
        dest.writeList(mItems);

    }
}
