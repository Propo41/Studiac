package com.example.project.utility.common;

public class Schedule {
    private String mDay;
    private String mStartTime;
    private String mEndTime;

    public Schedule(String day, String startTime, String endTime) {
        mDay = day;
        mStartTime = startTime;
        mEndTime = endTime;
    }

    public String getDay() {
        return mDay;
    }

    public void setDay(String day) {
        mDay = day;
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
}
