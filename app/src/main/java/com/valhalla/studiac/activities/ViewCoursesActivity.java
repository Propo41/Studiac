package com.valhalla.studiac.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.valhalla.studiac.R;
import com.valhalla.studiac.adapters.dashboard.ViewCoursesRecycleAdapter;
import com.valhalla.studiac.fragments.dialogs.CourseDetailsDialog;
import com.valhalla.studiac.fragments.dialogs.CourseEditBottomSheetDialog;
import com.valhalla.studiac.toolbars.NavigationToolbarWhite;
import com.valhalla.studiac.utility.common.Common;
import com.valhalla.studiac.utility.common.Course;

import java.util.ArrayList;


public class ViewCoursesActivity extends NavigationToolbarWhite {

    ArrayList<Course> mCourses;
    private ViewCoursesRecycleAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    Context context; // for debug

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.childActivity = Common.VIEW_COURSES;

        context = this; // for debug
        super.setContent(R.layout.activity_viewcourses);
        // fetch the transferred list of courses from the parent activity
        Bundle bundle = getIntent().getExtras();
        assert bundle != null;
        mCourses = bundle.getParcelableArrayList("courses");
        setupList();

    }

    private void setupList() {
        // will contain the View object we created in our layout file
        RecyclerView recyclerView = findViewById(R.id.view_courses_recycle_view_id);
        recyclerView.setHasFixedSize(true); // this will lock the scrolling. We cant scroll
        mLayoutManager = new LinearLayoutManager(this);
        mAdapter = new ViewCoursesRecycleAdapter(mCourses);
        handleUserEvents();
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(mAdapter);

    }



    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        Toast.makeText(getApplicationContext(), "backPressed", Toast.LENGTH_SHORT).show();

        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList("courses", mCourses);
        if(mCourses==null) {
            Toast.makeText(getApplicationContext(), "null data", Toast.LENGTH_SHORT).show();
        }
        intent.putExtras(bundle);
        setResult(Activity.RESULT_OK, intent);
        finish();
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
                    }

                    @Override
                    public void onDeleteClick() {
                        mCourses.remove(position);
                        mAdapter.notifyItemRemoved(position);
                        Toast.makeText(getApplicationContext(), "Course Deleted!", Toast.LENGTH_SHORT).show();

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

    @Override
    protected void onPause() {
        super.onPause();
        //    Common.saveToFile();
    }
}
