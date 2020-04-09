package com.example.project.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
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
        initViews();
    }

    private void initViews(){
        mCourseNameField = findViewById(R.id.courseNameField);
        mCourseCodeField = findViewById(R.id.courseCode);
        mCourseCreditField = findViewById(R.id.courseCredit);
        mAddRoutine = findViewById(R.id.addroutineBtn);
        mAddCourse = findViewById(R.id.addCourseBtn);
        mAddLater = findViewById(R.id.addLaterBtn);
    }

    @Override
    public void onClick(View v) {


        if(v.getId()== mAddRoutine.getId()){
             //@TODO: open dialog box
            // routine is optional
        }

        if(v.getId()== mAddCourse.getId()){
            //@TODO: open dialog box
            // call profileSetup2InputCheck() to check input errors
        }

        if(v.getId()== mAddLater.getId()){
            startActivity(new Intent(ProfileSetup2Activity.this, DashboardActivity.class));
        }

    }


    public boolean profileSetup2InputCheck(){
        boolean mReturnValue = true;

        if(mCourseNameField.getText().length()==0){
            mCourseNameField.setError("This field cannot be empty");
            mReturnValue = false;
        }


        if(mCourseCodeField.getText().length()==0 && mReturnValue){
            mCourseCodeField.setError("This field cannot be empty");
            mReturnValue = false;
        }

        if(mCourseCreditField.getText().length()==0 && mReturnValue){
            mCourseCreditField.setError("This field cannot be empty");
            mReturnValue = false;
        }
        return  mReturnValue;
    }
}
