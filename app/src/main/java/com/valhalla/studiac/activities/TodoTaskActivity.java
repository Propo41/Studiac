package com.valhalla.studiac.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.gson.internal.LinkedTreeMap;
import com.valhalla.studiac.R;
import com.valhalla.studiac.fragments.todo.CurrentTasksFragment;
import com.valhalla.studiac.fragments.todo.CurrentWeekFragment;
import com.valhalla.studiac.fragments.todo.UpcomingFragment;
import com.valhalla.studiac.toolbars.NavigationToolbarWhite;
import com.valhalla.studiac.utility.common.Common;
import com.valhalla.studiac.utility.common.Course;
import com.valhalla.studiac.utility.todo.Task;
import com.valhalla.studiac.utility.todo.TasksUtil;
import com.valhalla.studiac.utility.todo.TodoTasks;
import java.io.IOException;
import java.util.ArrayList;

public class TodoTaskActivity extends NavigationToolbarWhite {

    private int mCurrentSelectedItemBottomNav;
    private ArrayList<Task> mCurrentTasks;
    private ArrayList<TasksUtil> mCurrentWeek;
    private ArrayList<Course> mCourses;
    private TasksUtil mUpcoming;
    private TodoTasks mTodoTasks;
    private String mStudentTag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        loadData();

        super.onCreate(savedInstanceState);
        super.setContent(R.layout.activity_todotask);
        setupBottomNav();
    }

    /*
     * loads data from internal storage
     */
    private void loadData() {
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        assert bundle != null;
        mStudentTag = bundle.getString("studentTag");
        mCourses =  bundle.getParcelableArrayList("courses");


        // load data from file
        try {
            mTodoTasks = (TodoTasks) Common.loadFromFile(Common.TODO, mStudentTag, getApplicationContext());
        } catch (IOException e) {
            e.printStackTrace();
        }
        assert mTodoTasks != null;
        mCurrentTasks = mTodoTasks.getCurrentTasks();
        mCurrentWeek = mTodoTasks.getCurrentWeek();
        mUpcoming = mTodoTasks.getUpcoming();

        for (TasksUtil tasksUtil : mCurrentWeek) {
            fixSerialization(tasksUtil);
        }
        fixSerialization(mUpcoming);

    }

    /*
     *  fixes the generic type problem.
     *  problem: generic types cause problem when de-serialising. if the object is of a generic
     *  type, then the Generic type information is lost because of Java Type Erasure.
     * for example, ArrayList<Object> is a generic type. Java cannot identity which is what.
     * That's why it returns a LinkedTreeMap of Strings.
     * The method iterates through this map and fetches the actual values and replaces them with the
     * original variable
     */
    private void fixSerialization(TasksUtil upcoming) {
        ArrayList<Object> parsedTodoTasks = new ArrayList<>();
        for (Object object : upcoming.getTodoTasks()) {
            if (object instanceof LinkedTreeMap) {
                LinkedTreeMap treeMapObject = (LinkedTreeMap) object;
                Task task = new Task(
                        (String) treeMapObject.get("mDescription"),
                        (String) treeMapObject.get("mAdditionalNotes"),
                        (String) treeMapObject.get("mCategory"),
                        (String) treeMapObject.get("mSchedule"),
                        (String) treeMapObject.get("mType"));
                parsedTodoTasks.add(task);
            } else {
                parsedTodoTasks.add(object);
            }
        }

        upcoming.setTodoTasks(parsedTodoTasks);
        upcoming.setVisited(upcoming.getVisited());

    }

    @Override
    protected void onPause() {
        super.onPause();
        Toast.makeText(getApplicationContext(), "saved", Toast.LENGTH_SHORT).show();
        try {
            Common.saveToFile(mTodoTasks, Common.TODO, mStudentTag, getApplicationContext());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void setupBottomNav() {

        // the default fragment that is open initially
        getSupportFragmentManager().beginTransaction().replace(R.id.todo_fragment_container,
                new CurrentTasksFragment(mCurrentTasks, mCourses)).commit();

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment selected = new CurrentTasksFragment(mCurrentTasks, mCourses);
                if (mCurrentSelectedItemBottomNav != item.getItemId()) {
                    switch (item.getItemId()) {

                        case R.id.nav_btm_current:
                            Log.println(Log.DEBUG, "todoActivity", "current tasks tab selected");
                            selected = new CurrentTasksFragment(mCurrentTasks, mCourses);
                            mCurrentSelectedItemBottomNav = item.getItemId();
                            break;
                        case R.id.nav_btm_week:
                            Log.println(Log.DEBUG, "todoActivity", "current week tab selected");
                            selected = new CurrentWeekFragment(mCurrentWeek, mCurrentTasks, mCourses);
                            mCurrentSelectedItemBottomNav = item.getItemId();
                            break;
                        case R.id.nav_btm_upcoming:
                            Log.println(Log.DEBUG, "todoActivity", "upcoming tab selected");
                            selected = new UpcomingFragment(mUpcoming, mCurrentTasks, mCourses);
                            mCurrentSelectedItemBottomNav = item.getItemId();
                            break;
                    }
                    getSupportFragmentManager().beginTransaction().replace(R.id.todo_fragment_container,
                            selected).commit();
                }

                return true;
            }
        });


    }
}
