package com.example.project.utility.common;

import android.os.Parcel;
import android.os.Parcelable;

import com.example.project.utility.todo.Task;

import java.util.ArrayList;

public class Course implements Parcelable{

    private String mName;
    private String mCode;
    private Double mCredit;
    private Instructor mInstructor;

    public Course(String name, String code, Double credit) {
        mName = name;
        mCode = code;
        mCredit = credit;
        mInstructor = new Instructor("N/A","N/A", "N/A" );
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
    }
}
