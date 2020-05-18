package com.valhalla.studiac.models;


public class Student {
    private String mName;
    private String mImageResName;
    private String mUniversityName;
    private String mDepartmentName;
    private int mTotalSemester;
    private int mCurrentSemester;

    public Student() {
    }


    public Student(String name, String imageResName, String universityName, String departmentName, int totalSemester, int currentSemester) {
        mName = name;
        mImageResName = imageResName;
        mUniversityName = universityName;
        mDepartmentName = departmentName;
        mTotalSemester = totalSemester;
        mCurrentSemester = currentSemester;
    }


    public String getUniversityName() {
        return mUniversityName;
    }

    public void setUniversityName(String universityName) {
        mUniversityName = universityName;
    }

    public String getDepartmentName() {
        return mDepartmentName;
    }

    public void setDepartmentName(String departmentName) {
        mDepartmentName = departmentName;
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


    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }


    public String getImageResName() {
        return mImageResName;
    }

    public void setImageResName(String imageResName) {
        mImageResName = imageResName;
    }
}