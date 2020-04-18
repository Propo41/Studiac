package com.example.project.activities;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.project.R;
import com.example.project.fragments.dialogs.AddTaskMainBSDialog;
import com.example.project.fragments.todo.CurrentTasksFragment;
import com.example.project.fragments.todo.CurrentWeekFragment;
import com.example.project.fragments.todo.UpcomingFragment;
import com.example.project.toolbars.NavigationToolbarBlue;
import com.example.project.utility.common.Course;
import com.example.project.utility.todo.Task;
import com.example.project.utility.todo.TaskItems;
import com.example.project.utility.todo.Days;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class TodoTaskActivity extends NavigationToolbarBlue  {

    private ArrayList<Days> mDays;
    private int mCurrentSelectedItemBottomNav;
    private ArrayList<Task> mCurrentTasks;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContent(R.layout.activity_todotask, R.id.nav_toDoTask);
        //initList();
        mCurrentTasks = new ArrayList<>();
        mCurrentTasks.add(null);
        setupBottomNav();
    }



    private void initList() {

        mDays = new ArrayList<>();
        // we can define the sections in an array list of Objects and then use
        // if (object instanceof SectionClass) to identify if it is a section or not

        ArrayList<TaskItems> exampleItemsList = new ArrayList<>();

        exampleItemsList.add(new TaskItems("this is android", "ALGORITHM 1234", true));
        exampleItemsList.add(new TaskItems("is cloud"));
        exampleItemsList.add(new TaskItems("is cloud"));
        exampleItemsList.add(new TaskItems("this is android", "CSE 1200", true));
        exampleItemsList.add(new TaskItems("is cloud"));
        mDays.add(new Days(exampleItemsList, "APRIL 2020")); // day 1 initialized
        exampleItemsList = new ArrayList<>();

        exampleItemsList.add(new TaskItems("this is android", "MATH 1230", true));
        exampleItemsList.add(new TaskItems("is cloud"));
        exampleItemsList.add(new TaskItems("is cloud"));
        exampleItemsList.add(new TaskItems("this is android", "ARCH 1254", true));
        exampleItemsList.add(new TaskItems("is cloud"));
        mDays.add(new Days(exampleItemsList, "Wed")); // day 2 initialized
        exampleItemsList = new ArrayList<>();


        exampleItemsList.add(new TaskItems("this is android", "ALGORITHM 1234", true));
        exampleItemsList.add(new TaskItems("is cloud"));
        exampleItemsList.add(new TaskItems("is cloud"));
        exampleItemsList.add(new TaskItems("this is android", "CSE 1200", true));
        exampleItemsList.add(new TaskItems("is cloud"));
        mDays.add(new Days(exampleItemsList, "Thurs")); // day 2 initialized
        exampleItemsList = new ArrayList<>();

        exampleItemsList.add(new TaskItems("this is android", "MATH 1230", true));
        exampleItemsList.add(new TaskItems("is cloud"));
        exampleItemsList.add(new TaskItems("is cloud"));
        exampleItemsList.add(new TaskItems("this is android", "ARCH 1254", true));
        exampleItemsList.add(new TaskItems("is cloud"));
        mDays.add(new Days(exampleItemsList, "Fri")); // day 2 initialized
        exampleItemsList = new ArrayList<>();

        exampleItemsList.add(new TaskItems("this is android", "ALGORITHM 1234", true));
        exampleItemsList.add(new TaskItems("is cloud"));
        exampleItemsList.add(new TaskItems("is cloud"));
        exampleItemsList.add(new TaskItems("this is android", "CSE 1200", true));
        exampleItemsList.add(new TaskItems("is cloud"));
        mDays.add(new Days(exampleItemsList, "Fri")); // day 2 initialized
        exampleItemsList = new ArrayList<>();


        exampleItemsList.add(new TaskItems("this is android", "MATH 1230", true));
        exampleItemsList.add(new TaskItems("is cloud"));
        exampleItemsList.add(new TaskItems("is cloud"));
        exampleItemsList.add(new TaskItems("this is android", "ARCH 1254", true));
        exampleItemsList.add(new TaskItems("is cloud"));
        mDays.add(new Days(exampleItemsList, "Fri")); // day 2 initialized
        exampleItemsList = new ArrayList<>();

        exampleItemsList.add(new TaskItems("this is android", "ALGORITHM 1234", true));
        exampleItemsList.add(new TaskItems("is cloud"));
        exampleItemsList.add(new TaskItems("is cloud"));
        exampleItemsList.add(new TaskItems("this is android", "CSE 1200", true));
        exampleItemsList.add(new TaskItems("is cloud"));
        mDays.add(new Days(exampleItemsList, "Fri")); // day 2 initialized
        exampleItemsList = new ArrayList<>();

        exampleItemsList.add(new TaskItems("this is android", "MATH 1230", true));
        exampleItemsList.add(new TaskItems("is cloud"));
        exampleItemsList.add(new TaskItems("is cloud"));
        exampleItemsList.add(new TaskItems("this is android", "ARCH 1254", true));
        exampleItemsList.add(new TaskItems("is cloud"));
        mDays.add(new Days(exampleItemsList, "Fri")); // day 2 initialized
        exampleItemsList = new ArrayList<>();

        exampleItemsList.add(new TaskItems("this is android", "ALGORITHM 1234", true));
        exampleItemsList.add(new TaskItems("is cloud"));
        exampleItemsList.add(new TaskItems("is cloud"));
        exampleItemsList.add(new TaskItems("this is android", "CSE 1200", true));
        exampleItemsList.add(new TaskItems("is cloud"));
        mDays.add(new Days(exampleItemsList, "Fri")); // day 2 initialized
        exampleItemsList = new ArrayList<>();

        exampleItemsList.add(new TaskItems("this is android", "MATH 1230", true));
        exampleItemsList.add(new TaskItems("is cloud"));
        exampleItemsList.add(new TaskItems("is cloud"));
        exampleItemsList.add(new TaskItems("this is android", "ARCH 1254", true));
        exampleItemsList.add(new TaskItems("is cloud"));
        mDays.add(new Days(exampleItemsList, "Fri")); // day 2 initialized
        exampleItemsList = new ArrayList<>();

        exampleItemsList.add(new TaskItems("this is android", "ALGORITHM 1234", true));
        exampleItemsList.add(new TaskItems("is cloud"));
        exampleItemsList.add(new TaskItems("is cloud"));
        exampleItemsList.add(new TaskItems("this is android", "CSE 1200", true));
        exampleItemsList.add(new TaskItems("is cloud"));
        mDays.add(new Days(exampleItemsList, "Fri")); // day 2 initialized
        exampleItemsList = new ArrayList<>();

        exampleItemsList.add(new TaskItems("this is android", "MATH 1230", true));
        exampleItemsList.add(new TaskItems("is cloud"));
        exampleItemsList.add(new TaskItems("is cloud"));
        exampleItemsList.add(new TaskItems("this is android", "ARCH 1254", true));
        exampleItemsList.add(new TaskItems("is cloud"));
        mDays.add(new Days(exampleItemsList, "Fri")); // day 2 initialized


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
                if(mCurrentSelectedItemBottomNav != item.getItemId()){
                    switch (item.getItemId()) {

                        case R.id.nav_btm_current:
                            Log.println(Log.DEBUG, "todoActivity", "current tasks tab selected");
                            selected = new CurrentTasksFragment(mCurrentTasks);
                            mCurrentSelectedItemBottomNav = item.getItemId();
                            break;
                        case R.id.nav_btm_week:
                            Log.println(Log.DEBUG, "todoActivity", "current week tab selected");
                            selected = new CurrentWeekFragment(mDays);
                            mCurrentSelectedItemBottomNav = item.getItemId();
                            break;
                        case R.id.nav_btm_upcoming:
                            Log.println(Log.DEBUG, "todoActivity", "upcoming tab selected");
                            selected = new UpcomingFragment(mDays);
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
