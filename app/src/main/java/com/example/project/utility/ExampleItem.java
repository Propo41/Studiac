package com.example.project.utility;

import android.widget.TextView;

public class ExampleItem {


    private String mCourseName;
    private String mCourseCredit;
    private String mCourseCode;

    public ExampleItem(String courseName, String courseCredit, String courseCode) {
        mCourseName = courseName;
        mCourseCredit = courseCredit;
        mCourseCode = courseCode;
    }

    public String getCourseName() {
        return mCourseName;
    }

    public void setCourseName(String courseName) {
        mCourseName = courseName;
    }

    public String getCourseCredit() {
        return mCourseCredit;
    }

    public void setCourseCredit(String courseCredit) {
        mCourseCredit = courseCredit;
    }

    public String getCourseCode() {
        return mCourseCode;
    }

    public void setCourseCode(String courseCode) {
        mCourseCode = courseCode;
    }
}
