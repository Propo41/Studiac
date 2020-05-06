package com.valhalla.studiac.utility.common;

import android.os.Parcel;
import android.os.Parcelable;

public class Schedule implements Parcelable {
    private String mName; // name can be either day or course name as required. It's generic
    private String mStartTime;
    private String mEndTime;

    public Schedule(String name, String startTime, String endTime) {
        mName = name;
        mStartTime = startTime;
        mEndTime = endTime;
    }

    protected Schedule(Parcel in) {
        mName = in.readString();
        mStartTime = in.readString();
        mEndTime = in.readString();
    }

    public static final Creator<Schedule> CREATOR = new Creator<Schedule>() {
        @Override
        public Schedule createFromParcel(Parcel in) {
            return new Schedule(in);
        }

        @Override
        public Schedule[] newArray(int size) {
            return new Schedule[size];
        }
    };

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public String getStartTime() {
        return mStartTime;
    }

    public void setStartTime(String startTime) {
        mStartTime = startTime;
    }

    public String getEndTime() {
        return mEndTime;
    }

    public void setEndTime(String endTime) {
        mEndTime = endTime;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mName);
        dest.writeString(mStartTime);
        dest.writeString(mEndTime);
    }
}
