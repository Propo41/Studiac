package com.example.project.utility.todo;

import android.util.Log;

import androidx.core.util.Pair;

import com.example.project.utility.common.Common;
import com.example.project.utility.common.Course;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class TasksUtil {
    @SerializedName("todo_tasks")
    private ArrayList<Object> mTodoTasks; // may contain headers (objects of Course, Others, SelfStudy) or Task items
    @SerializedName("visited")
    private HashMap<String, Pair<Integer, Integer>> mVisited = new HashMap<>(); // header name, pair <Header index, Items under the header>

    TasksUtil() {
        mTodoTasks = new ArrayList<>();
    }

    /*
     * return NULL if header is not present, else returns the index of the position of where the
     * header lies.
     */
    public Pair<Integer, Integer> isVisited(Object object){
        String className = object.getClass().getSimpleName();
        switch (className) {
            case "Course":
                Course course = (Course) object;
                return mVisited.get(course.getCode());
            case "Others":
                return mVisited.get("Others");
            case "SelfStudy":
                return mVisited.get("Self Study");
            default:
                // parse the date and check if the date already exists or not
                return mVisited.get(parseDate(object));
        }
    }

    /*
     * inserts the course object into the list and saves the index of it in the list.
     */
    public void insertTask(Task task, Integer headerIndex, Integer count){
        Log.i("index: ", headerIndex + "");
        mTodoTasks.add(headerIndex+count+1, task);

        // increment the index of all headers that are below the current header
        for(Map.Entry<String, Pair<Integer, Integer>> itr  : mVisited.entrySet()){
            Integer index = itr.getValue().first;
            Integer itemCount = itr.getValue().second;
            if(!headerIndex.equals(index)){
                itr.setValue(new Pair<>(index + 1, itemCount));
               // Log.i("val changed to: ", itr.getValue().first + "");
            }else{
                itr.setValue(new Pair<>(index, itemCount+1));

            }
        }
    }

    /*
     * inserts the header name as well as the task at the end of the todoTasks list
     * also marks the header visited
     */
    public Integer insertNewTask(Task task, Object object){
        // for upcoming tab, where we only have 1 header type: String
        if(task.getSchedule()!=null){
            String date = parseDate(object);
            mTodoTasks.add(date);
            mVisited.put(date, new Pair<>(mTodoTasks.size() - 1, 1));
            mTodoTasks.add(task);
        }else {
            // for current week tab, where we have 3 header types: Course, Others, SelfStudy
            if (task.getCategory().first.equals("Course")) {
                Course course = (Course) object;
                mTodoTasks.add(course);
                // mark it visited
                mVisited.put(course.getCode(), new Pair<>(mTodoTasks.size() - 1, 1)); // storing the header index, count of item the new header. which is always 1 here
                mTodoTasks.add(task);

            } else if (task.getCategory().first.equals("Others")) {
                Others others = (Others) object;
                mTodoTasks.add(others);
                mVisited.put("Others", new Pair<>(mTodoTasks.size() - 1, 1));
                mTodoTasks.add(task);
            } else if (task.getCategory().first.equals("Self Study")) {
                SelfStudy selfStudy = (SelfStudy) object;
                mTodoTasks.add(selfStudy);
                mVisited.put("Self Study", new Pair<>(mTodoTasks.size() - 1, 1));
                mTodoTasks.add(task);
            }

        }
        return mTodoTasks.size() - 2;
    }

    /*
     * returns a string which corresponds to the month + year, for example: 11.2.20
     * will be converted into February 20
     */
    private String parseDate(Object object){
        String date = (String) object;
        String [] dateContents  = date.split("-");
        for (String a : dateContents)
            Log.i("dateContents: ", a);
        return Common.MONTHS[Integer.parseInt(dateContents[1])-1] + " " + dateContents[2];
    }


    public ArrayList<Object> getTodoTasks() {
        return mTodoTasks;
    }


}
