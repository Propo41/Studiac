package com.example.project.activities;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.util.Pair;
import androidx.fragment.app.Fragment;
import com.example.project.R;
import com.example.project.fragments.dialogs.RoundBottomSheetDialog;
import com.example.project.fragments.todo.CurrentTasksFragment;
import com.example.project.fragments.todo.CurrentWeekFragment;
import com.example.project.fragments.todo.UpcomingFragment;
import com.example.project.toolbars.NavigationToolbarBlue;
import com.example.project.utility.todo.Task;
import com.example.project.utility.todo.TaskItems;
import com.example.project.utility.todo.Days;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class TodoTaskActivity extends NavigationToolbarBlue implements RoundBottomSheetDialog.BottomSheetListener {

    private ArrayList<Days> mDays;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContent(R.layout.activity_todotask, R.id.nav_toDoTask);

        initList();
        setupBottomNav();

        handleEvents();




    }

    /*
     * when the user clicks on the large add button, open up bottom sheet dialog to add task
     */
    private void handleEvents() {
        // when user clicks on the large add button
        FloatingActionButton addButton = findViewById(R.id.todo_ADD_task_button_id);

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RoundBottomSheetDialog bottomSheetDialog = new RoundBottomSheetDialog();
                if (getFragmentManager() != null) {
                    bottomSheetDialog.show(getSupportFragmentManager(), "example");
                }
            }
        });
    }


    @Override
    public void onAddPressed(Task task) {

        // if task.type == "Current Task", add it into current task stack
        // if task.type == "Current Week", add it to current week stack
        // if task.type == "Future", add it to future stack

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

        exampleItemsList.add(new TaskItems("this is android", "MATH 1230",  true));
        exampleItemsList.add(new TaskItems("is cloud"));
        exampleItemsList.add(new TaskItems("is cloud"));
        exampleItemsList.add(new TaskItems("this is android", "ARCH 1254" , true));
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

        exampleItemsList.add(new TaskItems("this is android", "MATH 1230",  true));
        exampleItemsList.add(new TaskItems("is cloud"));
        exampleItemsList.add(new TaskItems("is cloud"));
        exampleItemsList.add(new TaskItems("this is android", "ARCH 1254" , true));
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



        exampleItemsList.add(new TaskItems("this is android", "MATH 1230",  true));
        exampleItemsList.add(new TaskItems("is cloud"));
        exampleItemsList.add(new TaskItems("is cloud"));
        exampleItemsList.add(new TaskItems("this is android", "ARCH 1254" , true));
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

        exampleItemsList.add(new TaskItems("this is android", "MATH 1230",  true));
        exampleItemsList.add(new TaskItems("is cloud"));
        exampleItemsList.add(new TaskItems("is cloud"));
        exampleItemsList.add(new TaskItems("this is android", "ARCH 1254" , true));
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

        exampleItemsList.add(new TaskItems("this is android", "MATH 1230",  true));
        exampleItemsList.add(new TaskItems("is cloud"));
        exampleItemsList.add(new TaskItems("is cloud"));
        exampleItemsList.add(new TaskItems("this is android", "ARCH 1254" , true));
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

        exampleItemsList.add(new TaskItems("this is android", "MATH 1230",  true));
        exampleItemsList.add(new TaskItems("is cloud"));
        exampleItemsList.add(new TaskItems("is cloud"));
        exampleItemsList.add(new TaskItems("this is android", "ARCH 1254" , true));
        exampleItemsList.add(new TaskItems("is cloud"));
        mDays.add(new Days(exampleItemsList, "Fri")); // day 2 initialized




    }


    private void setupBottomNav() {

        // the default fragment that is open initially
        getSupportFragmentManager().beginTransaction().replace(R.id.todo_fragment_container,
                new CurrentTasksFragment()).commit();

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment selected = new CurrentTasksFragment();
                switch (item.getItemId()){
                    case R.id.nav_btm_current:
                        selected = new CurrentTasksFragment();
                        break;
                    case R.id.nav_btm_week:
                        selected = new CurrentWeekFragment(mDays);
                        break;
                    case R.id.nav_btm_upcoming:
                        selected = new UpcomingFragment(mDays);
                        break;
                }

                getSupportFragmentManager().beginTransaction().replace(R.id.todo_fragment_container,
                        selected).commit();
                return true;
            }
        });


    }
}
