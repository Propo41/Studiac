package com.valhalla.studiac.utility.common;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class Instructor implements Parcelable {
    private String mName;
    private String mEmail;
    private String mRoom;
    private ArrayList<Schedule> mCounsellingTime;

    Instructor(String name, String email, String room) {
        mName = name;
        mEmail = email;
        mRoom = room;
        mCounsellingTime = new ArrayList<>();
    }

    protected Instructor(Parcel in) {
        mName = in.readString();
        mEmail = in.readString();
        mRoom = in.readString();
        mCounsellingTime = in.createTypedArrayList(Schedule.CREATOR);
    }

    public static final Creator<Instructor> CREATOR = new Creator<Instructor>() {
        @Override
        public Instructor createFromParcel(Parcel in) {
            return new Instructor(in);
        }

        @Override
        public Instructor[] newArray(int size) {
            return new Instructor[size];
        }
    };

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mName);
        dest.writeString(mEmail);
        dest.writeString(mRoom);
        dest.writeTypedList(mCounsellingTime);
    }
}
