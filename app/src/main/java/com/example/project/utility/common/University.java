package com.example.project.utility.common;

import java.util.ArrayList;

public class University {

    private String mName;
    private String mDepartment;
    private int mTotalSemester;
    private int mCurrentSemester;
    private ArrayList<Course> mCourses;

    public University(String name, String department, int totalSemester, int currentSemester, ArrayList<Course> courses) {
        mName = name;
        mDepartment = department;
        mTotalSemester = totalSemester;
        mCurrentSemester = currentSemester;
        mCourses = courses;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public String getDepartment() {
        return mDepartment;
    }

    public void setDepartment(String department) {
        mDepartment = department;
    }

    public int getTotalSemester() {
        return mTotalSemester;
    }

    public void setTotalSemester(int totalSemester) {
        mTotalSemester = totalSemester;
    }

    public int getCurrentSemester() {
        return mCurrentSemester;
    }

    public void setCurrentSemester(int currentSemester) {
        mCurrentSemester = currentSemester;
    }

    public ArrayList<Course> getCourses() {
        return mCourses;
    }

    public void setCourses(ArrayList<Course> courses) {
        mCourses = courses;
    }
}
