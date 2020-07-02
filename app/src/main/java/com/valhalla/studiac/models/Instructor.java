package com.valhalla.studiac.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class Instructor  {
    private String mName;
    private String mEmail;
    private String mRoom;
    private ArrayList<Schedule> mCounsellingTime;

    public Instructor() {

    }

    Instructor(String name, String email, String room) {
        mName = name;
        mEmail = email;
        mRoom = room;
        mCounsellingTime = new ArrayList<>();
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public String getEmail() {
        return mEmail;
    }

    public void setEmail(String email) {
        mEmail = email;
    }

    public String getRoom() {
        return mRoom;
    }

    public void setRoom(String room) {
        mRoom = room;
    }

    public ArrayList<Schedule> getCounsellingTime() {
        return mCounsellingTime;
    }

    public void setCounsellingTime(ArrayList<Schedule> counsellingTime) {
        mCounsellingTime = counsellingTime;
    }


}
