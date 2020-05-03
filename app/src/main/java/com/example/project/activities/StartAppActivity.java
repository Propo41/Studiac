package com.example.project.activities;

import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;

import com.example.project.R;
import com.example.project.fragments.dialogs.SelectCourseDialog;
import com.example.project.utility.common.Course;

import java.util.ArrayList;

public class StartAppActivity extends AppCompatActivity  {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_startapp);
        Button button = findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<Course> courses = new ArrayList<>();
                courses.add(new Course("MATH 1234", "MATH 1234", 3.0));
                courses.add(new Course("MATH 1234", "MATH 1234", 3.0));
                courses.add(new Course("MATH 1234", "MATH 1234", 3.0));
                courses.add(new Course("MATH 1234", "MATH 1234", 3.0));
                courses.add(new Course("MATH 1234", "MATH 1234", 3.0));
                courses.add(new Course("MATH 1234", "MATH 1234", 3.0));
                courses.add(new Course("MATH 1234", "MATH 1234", 3.0));
                courses.add(new Course("MATH 1234", "MATH 1234", 3.0));


                SelectCourseDialog dialog = new SelectCourseDialog(courses);
                dialog.show(getSupportFragmentManager(), "ASD");
            }
        });


    }

}
