package com.valhalla.studiac.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.internal.LinkedTreeMap;
import com.valhalla.studiac.R;
import com.valhalla.studiac.database.Firebase;
import com.valhalla.studiac.database.FirebaseListener;
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

public class TodoTaskActivity extends NavigationToolbarWhite implements FirebaseListener {

    private int mCurrentSelectedItemBottomNav;
    private ArrayList<Task> mCurrentTasks;
    private ArrayList<String> mCurrentTasksKeys;
    private ArrayList<TasksUtil> mCurrentWeek;
    private ArrayList<Course> mCourses = new ArrayList<>(); // todo add real courses later from db
    private TasksUtil mUpcoming;
    private TodoTasks mTodoTasks;
    private String mStudentTag;
    private String mUserUid;
    private FirebaseListener mFirebaseListener;
    private DatabaseReference mDatabaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContent(R.layout.activity_todotask);
        fetchDataFromIntent();

        mDatabaseReference = Firebase.getDatabaseReference().child(mUserUid).child(Firebase.TODO);
        importData();
        mFirebaseListener = this;
        //setupBottomNav();

    }

    private void importData() {

        // first check if current tasks already exists. If it does, then just import the values from db
        // and while iterating, fetch the key as well as values
        mDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mFirebaseListener.onLoadSuccess(dataSnapshot);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                mFirebaseListener.onLoadFailure(databaseError);

            }

        });

    }

    @Override
    public void onLoadSuccess(DataSnapshot dataSnapshot) {
        mTodoTasks = dataSnapshot.getValue(TodoTasks.class);
        setupBottomNav();

    }

    private void fetchDataFromIntent() {
        // fetch the user uid from the parent activity
        Bundle bundle = getIntent().getExtras();
        assert bundle != null;
        mCourses = bundle.getParcelableArrayList("courses");
        mUserUid = bundle.getString(Firebase.UID);
    }

    private void setupBottomNav() {

        // the default fragment that is open initially
        getSupportFragmentManager().beginTransaction().replace(R.id.todo_fragment_container,
                new CurrentTasksFragment(mUserUid, mTodoTasks.getCurrentTasks(), mCourses)).commit();

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment selected = new CurrentTasksFragment(mUserUid, mTodoTasks.getCurrentTasks(), mCourses);
                if (mCurrentSelectedItemBottomNav != item.getItemId()) {
                    switch (item.getItemId()) {

                        case R.id.nav_btm_current:
                            Log.println(Log.DEBUG, "todoActivity", "current tasks tab selected");
                            selected = new CurrentTasksFragment(mUserUid, mTodoTasks.getCurrentTasks(), mCourses);
                            mCurrentSelectedItemBottomNav = item.getItemId();
                            break;
                        case R.id.nav_btm_week:
                            Log.println(Log.DEBUG, "todoActivity", "current week tab selected");
                            selected = new CurrentWeekFragment(mUserUid, mTodoTasks.getCurrentWeek(), mTodoTasks.getCurrentTasks(), mCourses);
                            mCurrentSelectedItemBottomNav = item.getItemId();
                            break;
                        case R.id.nav_btm_upcoming:
                            Log.println(Log.DEBUG, "todoActivity", "upcoming tab selected");
                            selected = new UpcomingFragment(mUserUid, mTodoTasks.getUpcoming(), mTodoTasks.getCurrentTasks(), mCourses);
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


    @Override
    public void onLoadFailure(DatabaseError databaseError) {

    }
}
