package com.valhalla.studiac.database;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.valhalla.studiac.utility.common.Common;
import com.valhalla.studiac.utility.common.Course;
import com.valhalla.studiac.utility.common.Result;
import com.valhalla.studiac.utility.common.Routine;
import com.valhalla.studiac.utility.common.Student;
import com.valhalla.studiac.utility.todo.TodoTasks;

import java.util.ArrayList;
import java.util.HashMap;

public class Firebase extends Application {

    private static FirebaseDatabase mDatabase;
    private static DatabaseReference mDatabaseReference; // REFERS to the root dir
    public final static int STUDENT = 1;
    public final static int TODO_TASKS = 2;
    public final static int COURSES = 3;
    public final static int ROUTINE = 4;
    public final static int RESULT = 5;
    public final static String TODO = "todoTasks";
    public final static String TODO_CURRENT_TASKS = "currentTasks";
    public final static String TODO_CURRENT_WEEK = "currentWeek";
    public final static String TODO_UPCOMING = "upcoming";
    public final static String BASIC_INFO = "basicInfo";


    @Override
    public void onCreate() {
        mDatabase = FirebaseDatabase.getInstance();
        mDatabase.setPersistenceEnabled(true);
        mDatabase.getReference().getKey();
        mDatabaseReference = mDatabase.getReference();
        super.onCreate();
    }


    public static FirebaseDatabase getDatabase() {
        return mDatabase;
    }

    public static DatabaseReference getDatabaseReference() {
        return mDatabaseReference;
    }


    public static void writeObject(Object object, int type, String uid) {

        if (type == STUDENT) {
            Student basicInfo = (Student) object;
            mDatabaseReference.child(uid).child(Firebase.BASIC_INFO).setValue(basicInfo);
        } else if (type == TODO_TASKS) {
            TodoTasks todoTasks = (TodoTasks) object;
            mDatabaseReference.child(uid).child(Firebase.TODO).setValue(todoTasks);
            Log.i("writing", "todo tasks");

        } else {
            Log.i("ERROR", "invalid type passed");
        }
    }

    public static void writeListCourse(ArrayList<Course> courses, String uid) {
        mDatabaseReference.child(uid).child(Common.COURSES).setValue(courses);
    }

    public static void writeListRoutine(ArrayList<Routine> routine, String uid) {
        mDatabaseReference.child(uid).child(Common.ROUTINE).setValue(routine);
    }

    public static void writeListResult(ArrayList<Result> result, String uid) {
        mDatabaseReference.child(uid).child(Common.RESULT).setValue(result);
    }

    public static void writeListGradeWeight(HashMap<String, Double> gradeWeight, String uid) {
        mDatabaseReference.child(uid).child(Common.GRADE_WEIGHT).setValue(gradeWeight);
    }

    public static void checkDbStatus() {
        DatabaseReference connectedRef = FirebaseDatabase.getInstance().getReference(".info/connected");
        connectedRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                boolean connected = snapshot.getValue(Boolean.class);
                if (connected) {
                    Log.i("DB STATUS", "connected");
                } else {
                    Log.i("STATUS", "disconnected");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.i("TAG: ", "Listener was cancelled");
            }
        });
    }
}
