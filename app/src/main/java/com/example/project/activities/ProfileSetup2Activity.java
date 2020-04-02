package com.example.project.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.project.R;

public class ProfileSetup2Activity extends AppCompatActivity implements View.OnClickListener {

    private EditText mCourseNameField;
    private EditText mCourseCodeField;
    private EditText mCourseCreditField;
    private Button mAddRoutine;
    private Button mAddCourse;
    private TextView mAddLater;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profilesetup2);
        initialize(savedInstanceState);
    }

    private void initialize(Bundle savedInstanceState){
        mCourseNameField = (EditText) findViewById(R.id.courseNameField);
        mCourseCodeField = (EditText) findViewById(R.id.courseCode);
        mCourseCreditField = (EditText) findViewById(R.id.courseCredit);
        mAddRoutine = (Button) findViewById(R.id.addroutineBtn);
        mAddCourse = (Button) findViewById(R.id.addCourseBtn);
        mAddLater = (TextView) findViewById(R.id.addLaterBtn);
        mCourseNameField.setOnClickListener(this);
        mCourseCodeField.setOnClickListener(this);
        mCourseCreditField.setOnClickListener(this);
        mAddRoutine.setOnClickListener(this);
        mAddCourse.setOnClickListener(this);
        mAddLater.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        if(v.getId()== mCourseNameField.getId()){


        }

        if(v.getId()== mCourseCodeField.getId()){


        }


        if(v.getId()== mCourseCreditField.getId()){


        }


        if(v.getId()== mAddRoutine.getId()){


        }


        if(v.getId()== mAddCourse.getId()){


        }


        if(v.getId()== mAddLater.getId()){


        }
    }
}
