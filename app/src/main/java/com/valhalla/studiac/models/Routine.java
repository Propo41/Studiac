package com.valhalla.studiac.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class Routine {
    private ArrayList<Schedule> mItems;
    private String mDay;


    public Routine() {

    }

    public Routine(String day) {
        mDay = day;
        mItems = new ArrayList<>();
    }

    public String getDay() {
        return mDay;
    }

    public void setDay(String day) {
        mDay = day;
    }

    public ArrayList<Schedule> getItems() {
        return mItems;
    }

    public void setItems(ArrayList<Schedule> items) {
        mItems = items;
    }

}
