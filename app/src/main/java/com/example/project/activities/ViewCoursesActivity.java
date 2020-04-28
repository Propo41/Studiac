package com.example.project.activities;

import android.content.Context;
import android.os.Bundle;
import android.widget.Toast;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project.R;
import com.example.project.adapters.dashboard.ViewCoursesRecycleAdapter;
import com.example.project.toolbars.NavigationToolbarWhite;
import com.example.project.utility.common.Course;

import java.util.ArrayList;

public class ViewCoursesActivity extends NavigationToolbarWhite {

    ArrayList<Course> mCourses;
    private ViewCoursesRecycleAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    Context context; // for debug

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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

    private void handleUserEvents() {

        mAdapter.setOnItemClickListener(new ViewCoursesRecycleAdapter.OnItemClickListener() {
            @Override
            public void onButtonClick(int position) {
                // todo: open dialog C
                Toast.makeText(context, "button clicked at position: " + position, Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onItemClick(int position) {
                // todo: open dialog B
                Toast.makeText(context, "Item clicked at position: " + position, Toast.LENGTH_SHORT).show();
                // when the particular card view item is clicked, then do:
                //exampleItemsList.get(position).changeText("clicked!!");
                mAdapter.notifyItemChanged(position); // this is required to show the ripple effect

            }
        });
    }


}
