package com.valhalla.studiac.models;

import android.util.Log;

import com.google.firebase.database.Exclude;
import com.valhalla.studiac.holders.HeaderItem;
import com.valhalla.studiac.holders.ListItem;
import com.valhalla.studiac.utility.Common;
import com.valhalla.studiac.utility.CustomPair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class TasksUtil {

    private String mName;
    private ArrayList<ListItem> mTodoTasks; // may contain headers (String instances of CourseCode, Others, Self Study) or Task items
    private HashMap<String, CustomPair> mVisited = new HashMap<>(); // header name, pair <Header index, Items under the header>

    public TasksUtil() {

    }

    public TasksUtil(String name) {
        mName = name;
        mTodoTasks = new ArrayList<>();
        // mTodoTasks.add("NULL"); // this is used to populate the database, since db doesn't allow empty entities
    }

/*    public ArrayList<Object> getFirebaseTodoTasks() {
        return mFirebaseTodoTasks;
    }

    public void setFirebaseTodoTasks(ArrayList<Object> firebaseTodoTasks) {
        mFirebaseTodoTasks = firebaseTodoTasks;
    }*/

    public TasksUtil(String name, ArrayList<ListItem> listItems, HashMap<String, CustomPair> visitedMap) {
        mName = name;
        mVisited = visitedMap;
        mTodoTasks = listItems;
    }


/*
    public TasksUtil(String type, String name, ArrayList<Object> listItems, HashMap<String, CustomPair> visitedMap) {
        mName = name;
        mVisited = visitedMap;
        mFirebaseTodoTasks = listItems;
    }
*/

    /*
     * return NULL if header is not present, else returns the index of the position of where the
     * header lies.
     */
    @Exclude
    public CustomPair isVisited(String header) {
        return mVisited.get(header);
        // parse the date and check if the date already exists or not
        //  return mVisited.get(parseDate(object));

    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    @Exclude
    public void showMap() {
        System.out.println("*****SHOWING HASH MAP*******");
        for (Map.Entry<String, CustomPair> itr : mVisited.entrySet()) {
            Integer index = itr.getValue().first;
            Integer itemCount = itr.getValue().second;
            System.out.println("KEY: " + itr.getKey() + "  ,  VALUE: < " + index + ", " + itemCount + " >");
        }
    }

    @Exclude
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


    @Exclude
    public int removeTask(int pos) {
        int count;

        Task task = (Task) mTodoTasks.get(pos);
        // check if task contains a schedule. If it does, then it must've been called from
        // upcoming fragment.
        String category;
        // if called from currentWeek fragment
        if (task.getSchedule() == null) {
            category = task.getCategory();
        } else {
            // if called from upcoming
            category = Common.parseDate(task.getSchedule());
        }
        CustomPair ref = isVisited(category);
        Integer headerIndex = ref.first;

        // System.out.println("REMOVE TASK, POS ---> " + pos);
        // System.out.println("REMOVE TASK, headerIndex ---> " + headerIndex);

        if ((pos - 1) == headerIndex) {
            // check if there are more than 1 item associated with this header
            // if there is, then don't remove the header, else remove the header
            int size = ref.second;
            if (size == 1) {
                // if header is located at pos-1, then remove both header and task
                mVisited.remove(category);
                mTodoTasks.remove(pos); // remove the task
                mTodoTasks.remove(pos - 1);
                count = 2;

            } else {
                // if there are moe items associated with this header, then simply remove the task
                // and decrement item count of associated header by 1 and decrement all
                // header indexes below it by 1
                count = 1;
                mTodoTasks.remove(pos); // remove the task
                mVisited.put(category, new CustomPair(ref.first, ref.second - 1));

            }
        } else {
            // if header is not located at pos-1, then simply remove the task and decrement
            // item count of associated header by 1 and decrement all header indexes below it by 1
            mTodoTasks.remove(pos);
            mVisited.put(category, new CustomPair(ref.first, ref.second - 1));
            count = 1;


        }
        decrementItems(count, headerIndex);

        showList();
        showMap();

        return count;
    }


    /**
     * decrements the index of all items in the list by "count"
     *
     * @param count can be either 1 or 2
     */
    private void decrementItems(int count, int headerIndex) {
        for (Map.Entry<String, CustomPair> itr : mVisited.entrySet()) {
            Integer index = itr.getValue().first;
            Integer itemCount = itr.getValue().second;

            if (index > headerIndex) {
                itr.setValue(new CustomPair(index - count, itemCount));
            }
        }


    }


    /*
     * inserts the course object into the list and saves the index of it in the list.
     */
    @Exclude
    public void insertTask(Task task, Integer headerIndex, Integer count) {
        Log.i("index: ", headerIndex + "");
        mTodoTasks.add(headerIndex + count + 1, task);
        if (task.getSchedule() != null) {
            mVisited.put(Common.parseDate(task.getSchedule()), new CustomPair(headerIndex, count + 1));
        } else {
            mVisited.put(task.getCategory(), new CustomPair(headerIndex, count + 1));
        }

        // increment the index of all headers that are below the current header
        for (Map.Entry<String, CustomPair> itr : mVisited.entrySet()) {
            Integer index = itr.getValue().first;
            Integer itemCount = itr.getValue().second;
            if (index > headerIndex) {
                itr.setValue(new CustomPair(index + 1, itemCount));
            }
        }
    }

    /*
     * inserts the header name as well as the task at the end of the todoTasks list
     * also marks the header visited
     * possible header types are: courseCode, Others, Self Study, date(format: dd.mm.yy)
     */
    @Exclude
    public Integer insertNewTask(Task task, String header) {
        // for upcoming tab, where we only have 1 header type: String
        // storing the header index, count of item the new header. which is always 1 here
        mTodoTasks.add(new HeaderItem(header));
        mVisited.put(header, new CustomPair(mTodoTasks.size() - 1, 1));
        mTodoTasks.add(task);

        return mTodoTasks.size() - 2;
    }


    public ArrayList<ListItem> getTodoTasks() {
        return mTodoTasks;
    }

    public void setTodoTasks(ArrayList<ListItem> todoTasks) {
        mTodoTasks = todoTasks;
    }

    public HashMap<String, CustomPair> getVisited() {
        return mVisited;
    }

    public void setVisited(HashMap<String, CustomPair> visited) {
        mVisited = visited;
    }
}
