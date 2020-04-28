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

    private ArrayList<Object> mTodoTasks; // may contain headers (String instances of CourseCode, Others, Self Study) or Task items
    private HashMap<String, Pair<Integer, Integer>> mVisited = new HashMap<>(); // header name, pair <Header index, Items under the header>

    TasksUtil() {
        mTodoTasks = new ArrayList<>();
    }

    /*
     * return NULL if header is not present, else returns the index of the position of where the
     * header lies.
     */
    public Pair<Integer, Integer> isVisited(String header) {
        return mVisited.get(header);
        // parse the date and check if the date already exists or not
        //  return mVisited.get(parseDate(object));

    }

    /*
     * inserts the course object into the list and saves the index of it in the list.
     */
    public void insertTask(Task task, Integer headerIndex, Integer count) {
        Log.i("index: ", headerIndex + "");
        mTodoTasks.add(headerIndex + count + 1, task);

        // increment the index of all headers that are below the current header
        for (Map.Entry<String, Pair<Integer, Integer>> itr : mVisited.entrySet()) {
            Integer index = itr.getValue().first;
            Integer itemCount = itr.getValue().second;
            if (!headerIndex.equals(index)) {
                itr.setValue(new Pair<>(index + 1, itemCount));
                // Log.i("val changed to: ", itr.getValue().first + "");
            } else {
                itr.setValue(new Pair<>(index, itemCount + 1));

            }
        }
    }

    /*
     * inserts the header name as well as the task at the end of the todoTasks list
     * also marks the header visited
     * possible header types are: courseCode, Others, Self Study, date(format: dd.mm.yy)
     */
    public Integer insertNewTask(Task task, String header) {
        // for upcoming tab, where we only have 1 header type: String
        if (task.getSchedule() != null) {
            String date = Common.parseDate(header);
            mTodoTasks.add(date);
            mVisited.put(date, new Pair<>(mTodoTasks.size() - 1, 1));
            mTodoTasks.add(task);
        } else {
            // for current week tab and upcoming
            mTodoTasks.add(header);
            // mark it visited
            mVisited.put(header, new Pair<>(mTodoTasks.size() - 1, 1)); // storing the header index, count of item the new header. which is always 1 here
            mTodoTasks.add(task);
        }

        return mTodoTasks.size() - 2;
    }


    public ArrayList<Object> getTodoTasks() {
        return mTodoTasks;
    }

    public void setTodoTasks(ArrayList<Object> todoTasks) {
        mTodoTasks = todoTasks;
    }

    public HashMap<String, Pair<Integer, Integer>> getVisited() {
        return mVisited;
    }

    public void setVisited(HashMap<String, Pair<Integer, Integer>> visited) {
        mVisited = visited;
    }
}
