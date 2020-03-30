package com.example.project;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class ProfileSetup2 extends AppCompatActivity implements View.OnClickListener {

    private EditText courseNameField;
    private EditText courseCodeField;
    private EditText courseCreditField;
    private Button addRoutine;
    private Button addCourse;
    private EditText addLater;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_setup2);
        initialize(savedInstanceState);
    }

    private void initialize(Bundle savedInstanceState){
        courseNameField= (EditText) findViewById(R.id.courseNameField);
        courseCodeField = (EditText) findViewById(R.id.courseCode);
        courseCreditField = (EditText) findViewById(R.id.courseCredit);
        addRoutine = (Button) findViewById(R.id.addroutineBtn);
        addCourse = (Button) findViewById(R.id.addCourseBtn);
        addLater = (EditText) findViewById(R.id.addLaterBtn);
        courseNameField.setOnClickListener(this);
        courseCodeField.setOnClickListener(this);
        courseCreditField.setOnClickListener(this);
        addRoutine.setOnClickListener(this);
        addCourse.setOnClickListener(this);
        addLater.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        if(v.getId()==courseNameField.getId()){


        }

        if(v.getId()==courseCodeField.getId()){


        }


        if(v.getId()==courseCreditField.getId()){


        }


        if(v.getId()==addRoutine.getId()){


        }


        if(v.getId()==addCourse.getId()){


        }


        if(v.getId()==addLater.getId()){


        }
    }
}
