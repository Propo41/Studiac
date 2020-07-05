package com.valhalla.studiac.models;

import android.os.Parcel;
import android.os.Parcelable;

public class Schedule implements Parcelable {
    private String mName; // name can be either day or course name as required. It's generic
    private String mStartTime;
    private String mEndTime;
    private String mType; // could be lecture or practical
    private String mDescription; // can be the full course name

    public Schedule() {

    }

    /**
     * @param name      name can be either day or course code
     * @param startTime in 12 hour format, "hh:mmAM"
     * @param endTime   in 12 hr format, "hh:mmAM"
     */
    public Schedule(String name, String startTime, String endTime) {
        mName = name;
        mStartTime = startTime;
        mEndTime = endTime;
    }

    public Schedule(String name, String startTime, String endTime, String type, String description) {
        mName = name;
        mStartTime = startTime;
        mEndTime = endTime;
        mType = type;
        mDescription = description;
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

    public String getType() {
        return mType;
    }

    public void setType(String type) {
        mType = type;
    }

    protected Schedule(Parcel in) {
        mName = in.readString();
        mStartTime = in.readString();
        mEndTime = in.readString();
    }


    public String getDescription() {
        return mDescription;
    }

    public void setDescription(String description) {
        mDescription = description;
    }

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
        dest.writeString(mType);
    }
}
