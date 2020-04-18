package com.example.project.utility.common;

import java.util.ArrayList;

public class Instructor {
    private String mName;
    private String mEmail;
    private String mRoom;
    private ArrayList<Schedule> mCounsellingTime;

    public Instructor(String name, String email, String room, ArrayList<Schedule> counsellingTime) {
        mName = name;
        mEmail = email;
        mRoom = room;
        mCounsellingTime = counsellingTime;
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
