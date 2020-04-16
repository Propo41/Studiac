package com.example.project.utility.todo;


/*
 * helper class for storing the items in CurrentWeek tab.
 * it contains the section names along with the task
 */
public class TaskItems {

    private String mTask;
    private String mCourseName;
    private boolean mSection;

    public TaskItems(String task) {
        mSection = false;
        mTask = task;
    }

    public TaskItems(String task, String courseName, boolean section) {
        mCourseName = courseName;
        mSection = section;
        mTask = task;
    }

    public boolean isSection() {
        return mSection;
    }

    public String getTask() {
        return mTask;
    }

    public String getCourseName() {
        return mCourseName;
    }
}
