package com.example.project.utility.todo;

import java.util.ArrayList;

public class TodoTasks {
    private ArrayList<Task> mCurrentTasks;
    private ArrayList<TasksUtil> mCurrentWeek;
    private TasksUtil mUpcoming;

    public TodoTasks() {
        mCurrentTasks = new ArrayList<>();
        mCurrentTasks.add(null); // the first object is null due to the header. It has only 1 header
        mCurrentWeek = new ArrayList<>();
        // initializing all objects
        for (int i = 0; i < 7; i++) {
            TasksUtil tasksUtil = new TasksUtil();
            mCurrentWeek.add(tasksUtil);
        }
        mUpcoming = new TasksUtil();
    }


    public ArrayList<TasksUtil> getCurrentWeek() {
        return mCurrentWeek;
    }

    public void setCurrentWeek(ArrayList<TasksUtil> currentWeek) {
        mCurrentWeek = currentWeek;
    }

    public ArrayList<Task> getCurrentTasks() {
        return mCurrentTasks;
    }

    public void setCurrentTasks(ArrayList<Task> currentTasks) {
        mCurrentTasks = currentTasks;
    }

    public TasksUtil getUpcoming() {
        return mUpcoming;
    }

    public void setUpcoming(TasksUtil upcoming) {
        mUpcoming = upcoming;
    }
}
