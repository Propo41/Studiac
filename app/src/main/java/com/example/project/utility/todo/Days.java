package com.example.project.utility.todo;

import java.util.ArrayList;

public class Days {
    private ArrayList<CurrentWeekItems> mCurrentWeekItems;

    public Days(ArrayList<CurrentWeekItems> currentWeekItems) {
        mCurrentWeekItems = currentWeekItems;
    }

    public ArrayList<CurrentWeekItems> getCurrentWeekItems() {
        return mCurrentWeekItems;
    }

    public void setCurrentWeekItems(ArrayList<CurrentWeekItems> currentWeekItems) {
        mCurrentWeekItems = currentWeekItems;
    }
}
