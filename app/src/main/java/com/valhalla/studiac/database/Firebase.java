package com.valhalla.studiac.database;

import android.app.Application;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Firebase extends Application {

    private static DatabaseReference mDatabaseReference; // REFERS to the root dir
    public final static String UNIVERSITIES = "universities";
    public final static String USERS = "users";
    public final static String TODO = "todoTasks";
    public final static String TODO_CURRENT_TASKS = "currentTasks";
    public final static String TODO_CURRENT_WEEK = "currentWeek";
    public final static String CGPA = "cgpa";
    public final static String TODO_UPCOMING = "upcoming";
    public final static String BASIC_INFO = "basicInfo";
    public final static String POSTS = "posts";
    public final static String RESULTS = "semester-results";
    public final static String WEIGHTS = "grade-weights";
    public final static String USER_POSTS = "user-posts";


    @Override
    public void onCreate() {
        FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
        mDatabase.setPersistenceEnabled(true);
        mDatabase.getReference().getKey();
        mDatabaseReference = mDatabase.getReference();


        Firebase.getDatabaseReference().
                child(Firebase.UNIVERSITIES).
                child("AUST").
                child(Firebase.POSTS).keepSynced(true);
        super.onCreate();
    }

    public static DatabaseReference getDatabaseReference() {
        return mDatabaseReference;
    }

}
