package com.valhalla.studiac.utility.common;


public class Student {
    private String mName;
    private int mImageResId;
    private String mUniversityName;
    private String mDepartmentName;
    private int mTotalSemester;
    private int mCurrentSemester;

    public Student() {
    }


    public Student(String name, int imageResId, String universityName, String departmentName, int totalSemester, int currentSemester) {
        mName = name;
        mImageResId = imageResId;
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

    public int getImageResId() {
        return mImageResId;
    }

    public void setImageResId(int imageResId) {
        mImageResId = imageResId;
    }


}