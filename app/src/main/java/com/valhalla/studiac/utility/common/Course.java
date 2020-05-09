package com.valhalla.studiac.utility.common;



public class Course {

    private String mName;
    private String mCode;
    private Double mCredit;
    private Instructor mInstructor;

    public Course() {

    }

    public Course(String name, String code, Double credit) {
        mName = name;
        mCode = code;
        mCredit = credit;
        mInstructor = new Instructor("N/A", "N/A", "N/A");
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
