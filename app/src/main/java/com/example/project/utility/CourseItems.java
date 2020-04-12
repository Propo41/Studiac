package com.example.project.utility;

public class CourseItems {


    private String mCourseName;
    private String mCourseCredit;
    private String mCourseCode;

    public CourseItems(String courseName, String courseCredit, String courseCode) {
        mCourseName = courseName;
        mCourseCredit = courseCredit;
        mCourseCode = courseCode;
    }

    public String getCourseName() {
        return mCourseName;
    }

    public String getCourseCredit() {
        return mCourseCredit;
    }

    public String getCourseCode() {
        return mCourseCode;
    }


}
