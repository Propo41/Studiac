package com.valhalla.studiac.models;

import android.content.LocusId;

import androidx.annotation.ArrayRes;

import com.valhalla.studiac.holders.HeaderItem;
import com.valhalla.studiac.holders.ListItem;
import com.valhalla.studiac.utility.Common;

import java.util.ArrayList;
import java.util.Calendar;

public class TodoTasks {
    private ArrayList<ListItem> mCurrentTasks;
    private ArrayList<TasksUtil> mCurrentWeek;
    private TasksUtil mUpcoming;
    private final int DAYS = 7;

    public TodoTasks() {

    }

    public TodoTasks(String task) {

        mCurrentTasks = new ArrayList<>();
        mCurrentTasks.add(new HeaderItem("header")); // the first object is null due to the header. It has only 1 header
        if (!task.equals("deleteAll")) {
            String dateCreated = Common.convertToDateFormat(Calendar.getInstance().getTime());
            mCurrentTasks.add(new Task("Go on. Get to work!", null, dateCreated)); // a prompt, example task
        }
        mCurrentWeek = new ArrayList<>();
        // initializing all objects
        for (int i = 0; i < DAYS; i++) {
            //  TasksUtil tasksUtil = new TasksUtil();
            mCurrentWeek.add(new TasksUtil(Common.GET_DAY_FROM_INDEX[i]));
        }

        mUpcoming = new TasksUtil("upcoming");
    }

    public TodoTasks(ArrayList<ListItem> current, ArrayList<TasksUtil> week, TasksUtil upcoming) {
        mCurrentTasks = current;
        mCurrentWeek = week;
        mUpcoming = upcoming;
    }


    public ArrayList<TasksUtil> getCurrentWeek() {
        return mCurrentWeek;
    }

    public void setCurrentWeek(ArrayList<TasksUtil> currentWeek) {
        mCurrentWeek = currentWeek;
    }

    public ArrayList<ListItem> getCurrentTasks() {
        return mCurrentTasks;
    }

    public void setCurrentTasks(ArrayList<ListItem> currentTasks) {
        mCurrentTasks = currentTasks;
    }

    public TasksUtil getUpcoming() {
        return mUpcoming;
    }

    public void setUpcoming(TasksUtil upcoming) {
        mUpcoming = upcoming;
    }

    /*
     * converts the list of ListItems into List of Objects and turns it into an instance of
     * TodoTasks so that it can be imported to the database
     */
  /*  private TodoTasks toList() {
        ArrayList<Object> currentList = toListUtil(mCurrentTasks);

        ArrayList<Object> upcomingList = toListUtil(mUpcoming.getTodoTasks());
        TasksUtil upcomingTask = new TasksUtil(null, mUpcoming.getName(), upcomingList, mUpcoming.getVisited());


    }*/


/*    private ArrayList<Object> toListUtil(ArrayList<ListItem> listItems) {
        ArrayList<Object> list = new ArrayList<>();
        for (ListItem listItem : listItems) {
            if (listItem instanceof HeaderItem) {
                list.add(listItem);
            } else {
                list.add((Task) listItem);
            }
        }
        return list;

    }*/
}
