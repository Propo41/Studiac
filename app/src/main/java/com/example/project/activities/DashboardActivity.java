package com.example.project.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import com.example.project.R;
import com.example.project.toolbars.NavigationToolbarBlue;

public class DashboardActivity extends NavigationToolbarBlue {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContent(R.layout.activity_dashboard);

    }

    public void courseRoutinePressed(View v) {
        startActivity(new Intent(this, ViewRoutineActivity.class));
    }

    public void viewCoursesPressed(View v) {
        startActivity(new Intent(this, ViewCoursesActivity.class));
    }





}
