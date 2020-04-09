package com.example.project.activities;

import android.content.Context;
import android.os.Bundle;
import android.widget.Toast;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project.R;
import com.example.project.adapters.ExampleAdapter;
import com.example.project.toolbars.NavigationToolbarWhite;
import com.example.project.utility.ExampleItem;

import java.util.ArrayList;

public class ViewCoursesActivity extends NavigationToolbarWhite {

    ArrayList<ExampleItem> exampleItemsList = new ArrayList<>();
    // we need to change it to ExampleAdapter object
    // the adapter slowly passes the views that we need to the recycler view
    private ExampleAdapter mAdapter;
    // used to align our views using code
    private RecyclerView.LayoutManager mLayoutManager;
    Context context; // for debug

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this; // for debug
        super.setContent(R.layout.activity_viewcourses);
        initList();
        setupList();

    }

    private void setupList() {
        // will contain the View object we created in our layout file
        RecyclerView recyclerView = findViewById(R.id.recycle_view_id);
        recyclerView.setHasFixedSize(true); // this will lock the scrolling. We cant scroll
        mLayoutManager = new LinearLayoutManager(this);
        mAdapter = new ExampleAdapter(exampleItemsList);

        mAdapter.setOnItemClickListener(new ExampleAdapter.OnItemClickListener() {
            @Override
            public void onButtonClick(int position) {
                Toast.makeText(context, "button clicked at position: " + position, Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onItemClick(int position) {
                Toast.makeText(context, "Item clicked at position: " + position, Toast.LENGTH_SHORT).show();
                // when the particular card view item is clicked, then do:
                //exampleItemsList.get(position).changeText("clicked!!");
                mAdapter.notifyItemChanged(position); // this is required to show the ripple effect

            }
        });

        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(mAdapter);

    }

    private void initList() {

        exampleItemsList.add(new ExampleItem("Course Name", "Course Credit: 3.00", "Course Code: MATH123"));
        exampleItemsList.add(new ExampleItem("Course Name", "Course Credit: 3.00", "Course Code: MATH123"));
        exampleItemsList.add(new ExampleItem("Course Name", "Course Credit: 3.00", "Course Code: MATH123"));
        exampleItemsList.add(new ExampleItem("Course Name", "Course Credit: 3.00", "Course Code: MATH123"));
        exampleItemsList.add(new ExampleItem("Course Name", "Course Credit: 3.00", "Course Code: MATH123"));
        exampleItemsList.add(new ExampleItem("Course Name", "Course Credit: 3.00", "Course Code: MATH123"));
        exampleItemsList.add(new ExampleItem("Course Name", "Course Credit: 3.00", "Course Code: MATH123"));
        exampleItemsList.add(new ExampleItem("Course Name", "Course Credit: 3.00", "Course Code: MATH123"));
        exampleItemsList.add(new ExampleItem("Course Name", "Course Credit: 3.00", "Course Code: MATH123"));
        exampleItemsList.add(new ExampleItem("Course Name", "Course Credit: 3.00", "Course Code: MATH123"));
        exampleItemsList.add(new ExampleItem("Course Name", "Course Credit: 3.00", "Course Code: MATH123"));
        exampleItemsList.add(new ExampleItem("Course Name", "Course Credit: 3.00", "Course Code: MATH123"));
        exampleItemsList.add(new ExampleItem("Course Name", "Course Credit: 3.00", "Course Code: MATH123"));
        exampleItemsList.add(new ExampleItem("Course Name", "Course Credit: 3.00", "Course Code: MATH123"));
        exampleItemsList.add(new ExampleItem("Course Name", "Course Credit: 3.00", "Course Code: MATH123"));
        exampleItemsList.add(new ExampleItem("Course Name", "Course Credit: 3.00", "Course Code: MATH123"));
        exampleItemsList.add(new ExampleItem("Course Name", "Course Credit: 3.00", "Course Code: MATH123"));
        exampleItemsList.add(new ExampleItem("Course Name", "Course Credit: 3.00", "Course Code: MATH123"));


    }
}
