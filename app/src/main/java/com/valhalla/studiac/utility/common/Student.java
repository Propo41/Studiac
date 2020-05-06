package com.valhalla.studiac.utility.common;


public class Student {
    private String mName;
    private String mEmail;
    private String mPassword;
    private int mImageResId;
    private University mUniversity;
  //  private TodoTasks mTodoTasks;
 //   private boolean registered = false;

    public Student(String name, String email, String password, int imageResId, University university) {
        mName = name;
        mEmail = email;
        mPassword = password;
        mImageResId = imageResId;
        mUniversity = university;
       // mTodoTasks = new TodoTasks();


    }

    // for debug
  /*  public Student() {
         mTodoTasks = new TodoTasks();
    }*/

    public University getUniversity() {
        return mUniversity;
    }

    public void setUniversity(University university) {
        mUniversity = university;
    }

  /*  public TodoTasks getTodoTasks() {
        return mTodoTasks;
    }

    public void setTodoTasks(TodoTasks todoTasks) {
        mTodoTasks = todoTasks;
    }*/

   /* public boolean isRegistered() {
        return registered;
    }

    public void setRegistered(boolean registered) {
        this.registered = registered;
    }*/

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
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