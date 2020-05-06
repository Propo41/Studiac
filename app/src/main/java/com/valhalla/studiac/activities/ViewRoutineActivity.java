package com.valhalla.studiac.activities;

import android.content.Context;
import android.os.Bundle;

import com.valhalla.studiac.R;
import com.valhalla.studiac.adapters.dashboard.CustomViewPager;
import com.valhalla.studiac.adapters.dashboard.ViewRoutinePagerAdapter;
import com.valhalla.studiac.toolbars.NavigationToolbarWhite;
import com.valhalla.studiac.utility.common.Common;
import com.valhalla.studiac.utility.common.Routine;

import java.util.ArrayList;

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



