package com.example.project.utility.todo;

import java.util.ArrayList;

public class Days {
    private ArrayList<TaskItems> mTaskItems;
    private String mName;

    public Days(ArrayList<TaskItems> taskItems) {
        mTaskItems = taskItems;
    }

    public Days(ArrayList<TaskItems> taskItems, String name) {
        mTaskItems = taskItems;
        mName = name;
    }

    public ArrayList<TaskItems> getTaskItems() {
        return mTaskItems;
    }

}
