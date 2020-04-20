package com.example.project.utility.todo;

import java.util.ArrayList;

public class TodoTasks {
    private ArrayList<Task> mCurrentTasks;
    private ArrayList<Day> mCurrentWeek;
    private Day mUpcoming;
    private boolean initialized = false;

    public void initialize(){
        mCurrentTasks = new ArrayList<>();
        mCurrentTasks.add(null); // the first object is null due to the header. It has only 1 header
        mCurrentWeek = new ArrayList<>();
        // initializing all objects
        for(int i=0; i<7; i++){
            Day day = new Day();
            mCurrentWeek.add(day);
        }
        mUpcoming = new Day();
        initialized = true;



    }

    public boolean isInitialized() {
        return initialized;
    }

    public void setInitialized(boolean initialized) {
        this.initialized = initialized;
    }

    public ArrayList<Day> getCurrentWeek() {
        return mCurrentWeek;
    }

    public void setCurrentWeek(ArrayList<Day> currentWeek) {
        mCurrentWeek = currentWeek;
    }

    public ArrayList<Task> getCurrentTasks() {
        return mCurrentTasks;
    }

    public void setCurrentTasks(ArrayList<Task> currentTasks) {
        mCurrentTasks = currentTasks;
    }

    public Day getUpcoming() {
        return mUpcoming;
    }

    public void setUpcoming(Day upcoming) {
        mUpcoming = upcoming;
    }
}
