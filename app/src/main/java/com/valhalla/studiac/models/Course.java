package com.valhalla.studiac.models;


import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class Course   {

    private String mName;
    private String mCode;
    private Double mCredit;
    private Instructor mInstructor;
    private ArrayList<String> mDays;
    private String mType;

    public Course() {

    }



    public Course(String name, String code, Double credit, ArrayList<String> days) {
        mName = name;
        mCode = code;
        mCredit = credit;
        mDays = days;
        mInstructor = new Instructor("null", "null", "null");
    }


    public Course(String name, String code, Double credit, ArrayList<String> days, String type) {
        mName = name;
        mCode = code;
        mCredit = credit;
        mDays = days;
        mType = type;
        mInstructor = new Instructor("null", "null", "null");
    }

    public Course(String name, String code, Double credit) {
        mName = name;
        mCode = code;
        mCredit = credit;
        mInstructor = new Instructor("null", "null", "null");
    }


    public String getType() {
        return mType;
    }

    public void setType(String type) {
        mType = type;
    }

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

}
