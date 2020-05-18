package com.valhalla.studiac.models;


import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class Course implements Parcelable  {

    private String mName;
    private String mCode;
    private Double mCredit;
    private Instructor mInstructor;
    private ArrayList<String> mDays;

    public Course() {

    }



    public Course(String name, String code, Double credit, ArrayList<String> days) {
        mName = name;
        mCode = code;
        mCredit = credit;
        mDays = days;
        mInstructor = new Instructor("N/A", "N/A", "N/A");
    }


    protected Course(Parcel in) {
        mName = in.readString();
        mCode = in.readString();
        if (in.readByte() == 0) {
            mCredit = null;
        } else {
            mCredit = in.readDouble();
        }
        mInstructor = in.readParcelable(Instructor.class.getClassLoader());
        mDays = in.createStringArrayList();
    }

    public static final Creator<Course> CREATOR = new Creator<Course>() {
        @Override
        public Course createFromParcel(Parcel in) {
            return new Course(in);
        }

        @Override
        public Course[] newArray(int size) {
            return new Course[size];
        }
    };

    public ArrayList<String> getDays() {
        return mDays;
    }

    public void setDays(ArrayList<String> days) {
        mDays = days;
    }





    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public String getCode() {
        return mCode;
    }

    public void setCode(String code) {
        mCode = code;
    }

    public Double getCredit() {
        return mCredit;
    }

    public void setCredit(Double credit) {
        mCredit = credit;
    }

    public Instructor getInstructor() {
        return mInstructor;
    }

    public void setInstructor(Instructor instructor) {
        mInstructor = instructor;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mName);
        dest.writeString(mCode);
        if (mCredit == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeDouble(mCredit);
        }
        dest.writeParcelable(mInstructor, flags);
        dest.writeStringList(mDays);
    }
}
