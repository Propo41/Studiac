package com.valhalla.studiac.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.valhalla.studiac.R;
import com.valhalla.studiac.adapters.dashboard.ViewCoursesRecycleAdapter;
import com.valhalla.studiac.database.Firebase;
import com.valhalla.studiac.database.FirebaseListener;
import com.valhalla.studiac.fragments.dialogs.CourseDetailsDialog;
import com.valhalla.studiac.fragments.dialogs.CourseEditBottomSheetDialog;
import com.valhalla.studiac.toolbars.NavigationToolbarWhite;
import com.valhalla.studiac.utility.common.Common;
import com.valhalla.studiac.utility.common.Course;
import com.valhalla.studiac.utility.common.Routine;

import java.util.ArrayList;


public class ViewCoursesActivity extends NavigationToolbarWhite implements FirebaseListener {

    private ArrayList<Course> mCourses;
    private ViewCoursesRecycleAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private Context context; // for debug
    private String mUserUid;
    private FirebaseListener mFirebaseListener;
    private RecyclerView mRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContent(R.layout.activity_viewcourses);
        super.childActivity = Common.VIEW_COURSES;

        mFirebaseListener = this;
        context = this;
        fetchDataFromIntent();
        setupList();

    }

    private void fetchDataFromIntent() {
        // fetch the user uid from the parent activity
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            mUserUid = bundle.getString(Common.UID);
        } else {
            Common.showExceptionPrompt(this, "bundle is null");
        }
    }

    private void setupList() {
        mRecyclerView = findViewById(R.id.view_courses_recycle_view_id);
        mLayoutManager = new LinearLayoutManager(this);
        importData();

    }

    /*
     * uses an interface that gets triggered whenever data is available. After the data is obtained,
     * the adapter is set up
     */
    private void importData() {

        Firebase.getDatabaseReference().
                child(mUserUid).
                child(Common.COURSES).addListenerForSingleValueEvent(new ValueEventListener() {

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
        ArrayList<Course> courses = new ArrayList<>();
        for (DataSnapshot data : dataSnapshot.getChildren()) {
            courses.add(data.getValue(Course.class));
        }

        mCourses = courses;
        mAdapter = new ViewCoursesRecycleAdapter(mCourses);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
        handleUserEvents();

    }

    private void handleUserEvents() {

        mAdapter.setOnItemClickListener(new ViewCoursesRecycleAdapter.OnItemClickListener() {
            @Override
            public void onButtonClick(final int position) {
                CourseEditBottomSheetDialog dialog = new CourseEditBottomSheetDialog(mCourses.get(position));
                dialog.show(getSupportFragmentManager(), "viewCoursesActivity");

                dialog.setOnUpdateClickListener(new CourseEditBottomSheetDialog.OnUpdateClickListener() {
                    @Override
                    public void onUpdateClick() {
                        mAdapter.notifyItemChanged(position);
                        // update it in the database as well
                        Firebase.getDatabaseReference().child(mUserUid).child(Common.COURSES).child(String.valueOf(position)).setValue(mCourses.get(position));

                    }

                    @Override
                    public void onDeleteClick() {
                        deleteFromDatabase(position);
                        mCourses.remove(position);
                        mAdapter.notifyItemRemoved(position);

                    }
                });

            }

            @Override
            public void onItemClick(int position) {
                Course course = mCourses.get(position);
                CourseDetailsDialog dialog = new CourseDetailsDialog(course);
                dialog.show(getSupportFragmentManager(), "viewCoursesActivity");

            }
        });
    }

    private void deleteFromDatabase(int position) {
        Query query = Firebase.getDatabaseReference().child(mUserUid).child(Common.COURSES).orderByChild("code").equalTo(mCourses.get(position).getCode());
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    snapshot.getRef().removeValue();
                }
                Toast.makeText(getApplicationContext(), "Course Deleted!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(), "Error!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onLoadFailure(DatabaseError databaseError) {

    }
}
