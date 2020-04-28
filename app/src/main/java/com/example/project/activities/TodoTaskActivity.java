package com.example.project.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.project.R;
import com.example.project.fragments.todo.CurrentTasksFragment;
import com.example.project.fragments.todo.CurrentWeekFragment;
import com.example.project.fragments.todo.UpcomingFragment;
import com.example.project.toolbars.NavigationToolbarBlue;
import com.example.project.toolbars.NavigationToolbarWhite;
import com.example.project.utility.common.Common;
import com.example.project.utility.todo.Task;
import com.example.project.utility.todo.TasksUtil;
import com.example.project.utility.todo.TodoTasks;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.gson.internal.LinkedTreeMap;

import java.io.InputStreamReader;
import java.util.ArrayList;

public class TodoTaskActivity extends NavigationToolbarWhite {

    private int mCurrentSelectedItemBottomNav;
    private ArrayList<Task> mCurrentTasks;
    private ArrayList<TasksUtil> mCurrentWeek;
    private TasksUtil mUpcoming;
    private TodoTasks mTodoTasks;
    private String mStudentTag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        // uncomment the following lines.
        // get the student tag from the passed intent
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        mStudentTag = bundle.getString("studentTag");

        // load data from file
        mTodoTasks = (TodoTasks) Common.loadFromFile(Common.TODO, mStudentTag, getApplicationContext());
        mCurrentTasks = mTodoTasks.getCurrentTasks();
        mCurrentWeek = mTodoTasks.getCurrentWeek();
        mUpcoming = mTodoTasks.getUpcoming();

        // https://stackoverflow.com/questions/32444863/google-gson-linkedtreemap-class-cast-to-myclass
        // problem: generic types cause problem when deserialising. if the object is of a generic
        // type, then the Generic type information is lost because of Java Type Erasure.
       /* int i = 0;
        for (Object object : mUpcoming.getTodoTasks()) {
            System.out.println(i + ":  , " + object.getClass().getSimpleName());
            if (object.getClass().getSimpleName().equals("LinkedTreeMap")) {
                LinkedTreeMap<Object, Object> t = (LinkedTreeMap) object;

            }
            i++;
        }*/

        super.onCreate(savedInstanceState);
        super.setContent(R.layout.activity_todotask);
        setupBottomNav();
    }

    @Override
    protected void onPause() {
        super.onPause();
        Toast.makeText(getApplicationContext(), "on pause", Toast.LENGTH_SHORT).show();
       // Common.saveToFile(mTodoTasks, Common.TODO, mStudentTag, getApplicationContext());
    }

    private void setupBottomNav() {

        // the default fragment that is open initially
        getSupportFragmentManager().beginTransaction().replace(R.id.todo_fragment_container,
                new CurrentTasksFragment(mCurrentTasks)).commit();

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment selected = new CurrentTasksFragment(mCurrentTasks);
                if (mCurrentSelectedItemBottomNav != item.getItemId()) {
                    switch (item.getItemId()) {

                        case R.id.nav_btm_current:
                            Log.println(Log.DEBUG, "todoActivity", "current tasks tab selected");
                            selected = new CurrentTasksFragment(mCurrentTasks);
                            mCurrentSelectedItemBottomNav = item.getItemId();
                            break;
                        case R.id.nav_btm_week:
                            Log.println(Log.DEBUG, "todoActivity", "current week tab selected");
                            selected = new CurrentWeekFragment(mCurrentWeek, mCurrentTasks);
                            mCurrentSelectedItemBottomNav = item.getItemId();
                            break;
                        case R.id.nav_btm_upcoming:
                            Log.println(Log.DEBUG, "todoActivity", "upcoming tab selected");
                            selected = new UpcomingFragment(mUpcoming, mCurrentTasks);
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
