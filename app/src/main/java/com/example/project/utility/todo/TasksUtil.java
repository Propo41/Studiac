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

    public void showMap() {
        System.out.println("*****SHOWING HASH MAP*******");
        for (Map.Entry<String, Pair<Integer, Integer>> itr : mVisited.entrySet()) {
            Integer index = itr.getValue().first;
            Integer itemCount = itr.getValue().second;
            System.out.println("KEY: " + itr.getKey() + "  ,  VALUE: < " + index + ", " + itemCount + " >");
        }
    }

    public void showList() {
        System.out.println("*****SHOWING LIST*******");
        int i = 0;
        for (Object itr : mTodoTasks) {
            if (itr instanceof Task) {
                System.out.println("task ---> " + i);
            } else {
                System.out.println("HEADER ---> " + i);
            }
            i++;
        }
    }


    public int removeTask(int pos) {
        int count;

        Task task = (Task) mTodoTasks.get(pos);
        String category = task.getCategory();
        Pair<Integer, Integer> ref = isVisited(category);
        Integer headerIndex = ref.first;

        System.out.println("REMOVE TASK, POS ---> " + pos);
        System.out.println("REMOVE TASK, headerIndex ---> " + headerIndex);

        if ((pos - 1) == headerIndex) {
            // if header is located at pos-1, then remove both header and task
            mVisited.remove(category);
            System.out.println("remove pos");
            mTodoTasks.remove(pos);
            System.out.println("remove pos - 1");

            mTodoTasks.remove(pos - 1);
            count = 2;

            // decrement by 2 all the header indexes that are  > headerIndex
            for (Map.Entry<String, Pair<Integer, Integer>> itr : mVisited.entrySet()) {
                Integer index = itr.getValue().first;
                Integer itemCount = itr.getValue().second;
                if (index > headerIndex) {
                    itr.setValue(new Pair<>(index - 2, itemCount));
                }
            }
        } else {
            // if header is not located at pos-1, then simply remove the task and decrement
            // item count of associated header by 1 and decrement all header indexes below it by 1
            mTodoTasks.remove(pos);
            mVisited.put(category, new Pair<>(ref.first, ref.second - 1));
            // decrement by 1 all the header indexes that are  > headerIndex
            for (Map.Entry<String, Pair<Integer, Integer>> itr : mVisited.entrySet()) {
                Integer index = itr.getValue().first;
                Integer itemCount = itr.getValue().second;

                if (index > headerIndex) {
                    itr.setValue(new Pair<>(index - 1, itemCount));
                }
            }

            count = 1;

        }

        return count;
    }


    /*
     * inserts the course object into the list and saves the index of it in the list.
     */
    public void insertTask(Task task, Integer headerIndex, Integer count) {
        Log.i("index: ", headerIndex + "");
        mTodoTasks.add(headerIndex + count + 1, task);
        if (task.getSchedule() != null) {
            mVisited.put(Common.parseDate(task.getSchedule()), new Pair<>(headerIndex, count + 1));
        } else {
            mVisited.put(task.getCategory(), new Pair<>(headerIndex, count + 1));
        }

        // increment the index of all headers that are below the current header
        for (Map.Entry<String, Pair<Integer, Integer>> itr : mVisited.entrySet()) {
            Integer index = itr.getValue().first;
            Integer itemCount = itr.getValue().second;
            if (index > headerIndex) {
                itr.setValue(new Pair<>(index + 1, itemCount));
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
            mTodoTasks.add(header);
            mVisited.put(header, new Pair<>(mTodoTasks.size() - 1, 1));
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
