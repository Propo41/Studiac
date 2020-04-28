package com.example.project.activities;

import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.project.R;
import com.example.project.adapters.dashboard.CustomViewPager;
import com.example.project.adapters.dashboard.ViewRoutinePagerAdapter;
import com.example.project.toolbars.NavigationToolbarWhite;
import com.example.project.utility.common.Common;
import com.example.project.utility.common.Routine;
import com.example.project.utility.common.Schedule;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Calendar;

public class ViewRoutineActivity extends NavigationToolbarWhite {

    Context mContext; // for debug
    private ArrayList<Routine> mRoutine;
    private CustomViewPager mViewPager;
    private ViewRoutinePagerAdapter mRoutineAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContent(R.layout.activity_view_routine);
        mContext = this;

        // fetch the transferred list of courses from the parent activity
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            mRoutine = bundle.getParcelableArrayList("routine");
        } else {
            Common.showExceptionPrompt(this, "bundle is null");
        }

        createCardView();

    }

    private void createCardView() {

        mRoutineAdapter = new ViewRoutinePagerAdapter(mRoutine, mContext);
        mViewPager = findViewById(R.id.view_routine_viewPager);
        mViewPager.setAdapter(mRoutineAdapter);
        mViewPager.setPadding(50, 0, 50, 0);
    }

}



