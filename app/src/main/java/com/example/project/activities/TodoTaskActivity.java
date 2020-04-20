package com.example.project.activities;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.project.R;
import com.example.project.fragments.todo.CurrentTasksFragment;
import com.example.project.fragments.todo.CurrentWeekFragment;
import com.example.project.fragments.todo.UpcomingFragment;
import com.example.project.toolbars.NavigationToolbarBlue;
import com.example.project.utility.common.Common;
import com.example.project.utility.common.Student;
import com.example.project.utility.todo.Task;
import com.example.project.utility.todo.TaskItems;
import com.example.project.utility.todo.Day;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;

public class TodoTaskActivity extends NavigationToolbarBlue  {

    private int mCurrentSelectedItemBottomNav;
    private ArrayList<Task> mCurrentTasks;
    private ArrayList<Day> mCurrentWeek;
    private Day mUpcoming;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        // for debug
        Student dStudent = new Student();
        Common.setStudent(dStudent);

        // if values are already initialized, then just fetch them
        // else, initialize them
        if(!Common.getStudent().getTodoTasks().isInitialized()) {
            Common.getStudent().getTodoTasks().initialize();
        }

        mCurrentTasks = Common.getStudent().getTodoTasks().getCurrentTasks();
        mCurrentWeek = Common.getStudent().getTodoTasks().getCurrentWeek();
        mUpcoming = Common.getStudent().getTodoTasks().getUpcoming();

        super.onCreate(savedInstanceState);
        super.setContent(R.layout.activity_todotask, R.id.nav_toDoTask);
       /* mCurrentTasks = new ArrayList<>();
        Day day1 = new Day("1 Nothing here yet! Add a task.");
        Day day2 = new Day("2 Nothing here yet! Add a task.");
        Day day3 = new Day("3 Nothing here yet! Add a task.");
        Day day4 = new Day("4 Nothing here yet! Add a task.");
        Day day5 = new Day("5 Nothing here yet! Add a task.");
        Day day6 = new Day("6 Nothing here yet! Add a task.");
        Day day7 = new Day("7 Nothing here yet! Add a task.");

        mCurrentWeek = new ArrayList<>();
        mCurrentWeek.add(day1);
        mCurrentWeek.add(day2);
        mCurrentWeek.add(day3);
        mCurrentWeek.add(day4);
        mCurrentWeek.add(day5);
        mCurrentWeek.add(day6);
        mCurrentWeek.add(day7);
        mUpcoming = new Day();
        mCurrentTasks.add(null);*/
        setupBottomNav();
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
                            selected = new CurrentWeekFragment(mCurrentWeek);
                            mCurrentSelectedItemBottomNav = item.getItemId();
                            break;
                        case R.id.nav_btm_upcoming:
                            Log.println(Log.DEBUG, "todoActivity", "upcoming tab selected");
                            selected = new UpcomingFragment(mUpcoming);
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
