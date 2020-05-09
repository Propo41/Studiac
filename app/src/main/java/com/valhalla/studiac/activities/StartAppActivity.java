package com.valhalla.studiac.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import com.valhalla.studiac.R;
import com.valhalla.studiac.fragments.dialogs.SelectCourseDialog;
import com.valhalla.studiac.utility.common.Course;

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
