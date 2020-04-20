package com.example.project.utility.common;

import com.example.project.utility.todo.Others;
import com.example.project.utility.todo.SelfStudy;
import com.example.project.utility.todo.TodoTasks;


public class Student {
    private String mFullName;
    private String mEmail;
    private String mPassword;
    private int mImageResId;
    private University mUniversity;
    private TodoTasks mTodoTasks;
    private boolean registered;

    public Student(String fullName, String email, University university, String password, int imageResId) {
        mFullName = fullName;
        mEmail = email;
        mPassword = password;
        mImageResId = imageResId;
        mUniversity = university;
        mTodoTasks = new TodoTasks();


    }

    public Student(String fullName, String email, String password, int imageResId, University university, TodoTasks todoTasks, boolean registered) {
        mFullName = fullName;
        mEmail = email;
        mPassword = password;
        mImageResId = imageResId;
        mUniversity = university;
        mTodoTasks = todoTasks;
        this.registered = registered;
        mTodoTasks = new TodoTasks();

    }

    // for debug
    public Student() {
         mTodoTasks = new TodoTasks();
    }

    public University getUniversity() {
        return mUniversity;
    }

    public void setUniversity(University university) {
        mUniversity = university;
    }

    public TodoTasks getTodoTasks() {
        return mTodoTasks;
    }

    public void setTodoTasks(TodoTasks todoTasks) {
        mTodoTasks = todoTasks;
    }

    public boolean isRegistered() {
        return registered;
    }

    public void setRegistered(boolean registered) {
        this.registered = registered;
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


}