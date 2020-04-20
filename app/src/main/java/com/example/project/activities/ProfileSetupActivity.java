package com.example.project.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.project.R;

public class ProfileSetupActivity extends AppCompatActivity implements View.OnClickListener {

    private Button mChangeAvatarBtn;
    private EditText mUniversityNameField;
    private EditText mDepartmentNameField;
    private EditText mCurrentSemesterField;
    private EditText mTotalSemesterField;
    private Button mNextBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profilesetup);
        initViews();

    }

    private void initViews() {
        mUniversityNameField = findViewById(R.id.universityNameField);
        mDepartmentNameField = findViewById(R.id.departmentNameField);
        mCurrentSemesterField = findViewById(R.id.currentSemesterField);
        mTotalSemesterField = findViewById(R.id.totalSemesterField);
        mNextBtn = findViewById(R.id.nextButton);
        mChangeAvatarBtn = findViewById(R.id.changeAvatarBtn);
        mUniversityNameField.setOnClickListener(this);
        mDepartmentNameField.setOnClickListener(this);
        mCurrentSemesterField.setOnClickListener(this);
        mTotalSemesterField.setOnClickListener(this);
        mNextBtn.setOnClickListener(this);
        mChangeAvatarBtn.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {


        if (v.getId() == mNextBtn.getId()) {
            if(checkUserInputProfileSetup(v))
                startActivity(new Intent(ProfileSetupActivity.this, ProfileSetup2Activity.class));
        }


        if (v.getId() == mChangeAvatarBtn.getId()) {
            // @TODO: make SelectImageActivity a fragment
            startActivity(new Intent(ProfileSetupActivity.this, SelectImageActivity.class));
        }

    }


    public boolean checkUserInputProfileSetup(View v){
        boolean mReturnValue = true;

        if(mUniversityNameField.getText().length()==0){
            mUniversityNameField.setError("This Field cannot be Empty");
            mReturnValue = false;
            return mReturnValue;
        }

        if(mDepartmentNameField.getText().length()==0){
            mDepartmentNameField.setError("This Field cannot be Empty");
            mReturnValue = false;
        }

        if(mCurrentSemesterField.getText().length()==0){
            mCurrentSemesterField.setError("This Field cannot be Empty");
            mReturnValue = false;
        }

        if(mTotalSemesterField.getText().length()==0){
            mTotalSemesterField.setError("This Field cannot be Empty");
            mReturnValue = false;
        }

        return mReturnValue;
    }

}
