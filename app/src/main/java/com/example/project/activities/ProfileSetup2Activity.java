package com.example.project.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
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
    final Context context = this;

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


    public void addCoursePressed(View v){

        final Dialog courseAddedDialog = new Dialog(context);
        courseAddedDialog.setContentView(R.layout.dialog_setupprofile2_courseadded);

        Button addMoreBv = courseAddedDialog.findViewById(R.id.course_addmore_button_id);
        Button continueBv = courseAddedDialog.findViewById(R.id.course_continue_button_id);

        addMoreBv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v0) {
                courseAddedDialog.dismiss();
            }
        });

        continueBv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v0) {
                courseAddedDialog.dismiss();
                // @TODO: open dashboard

            }
        });

        courseAddedDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        courseAddedDialog.show();


    }

    @Override
    public void onClick(View v) {


        if(v.getId()== mAddRoutine.getId()){
             //@TODO: open dialog box
            // routine is optional
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
