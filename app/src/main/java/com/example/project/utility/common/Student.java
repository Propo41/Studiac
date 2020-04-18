package com.example.project.utility.common;

import com.example.project.utility.todo.Others;
import com.example.project.utility.todo.SelfStudy;

public class Student {
    private String mFullName;
    private String mEmail;
    private String mPassword;
    private int mImageResId;
    private Others mOthersTasks;
    private SelfStudy mSelfStudyTasks;
    private University mUniversity;

    public Student(String fullName, String email, University university, String password, int imageResId) {
        mFullName = fullName;
        mEmail = email;
        mPassword = password;
        mImageResId = imageResId;
        mUniversity = university;

    }


    public String getFullName() {
        return mFullName;
    }

    public void setFullName(String fullName) {
        mFullName = fullName;
    }

    public String getEmail() {
        return mEmail;
    }

    public void setEmail(String email) {
        mEmail = email;
    }

    public String getPassword() {
        return mPassword;
    }

    public void setPassword(String password) {
        mPassword = password;
    }

    public int getImageResId() {
        return mImageResId;
    }

    public void setImageResId(int imageResId) {
        mImageResId = imageResId;
    }

    public Others getOthersTasks() {
        return mOthersTasks;
    }

    public void setOthersTasks(Others othersTasks) {
        mOthersTasks = othersTasks;
    }

    public SelfStudy getSelfStudyTasks() {
        return mSelfStudyTasks;
    }

    public void setSelfStudyTasks(SelfStudy selfStudyTasks) {
        mSelfStudyTasks = selfStudyTasks;
    }
}
