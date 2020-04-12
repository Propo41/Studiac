package com.example.project.activities;

import android.os.Bundle;
import android.view.MenuItem;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import com.example.project.R;
import com.example.project.fragments.todo.CurrentTasksFragment;
import com.example.project.fragments.todo.CurrentWeekFragment;
import com.example.project.fragments.todo.UpcomingFragment;
import com.example.project.toolbars.NavigationToolbarBlue;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class ToDoTaskActivity extends NavigationToolbarBlue {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContent(R.layout.activity_todotask, R.id.nav_toDoTask);

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
                        selected = new CurrentWeekFragment();
                        break;
                    case R.id.nav_btm_upcoming:
                        selected = new UpcomingFragment();
                        break;
                }

                getSupportFragmentManager().beginTransaction().replace(R.id.todo_fragment_container,
                        selected).commit();
                return true;
            }
        });


        setupBottomNav();

    }

    private void setupBottomNav() {


    }
}
