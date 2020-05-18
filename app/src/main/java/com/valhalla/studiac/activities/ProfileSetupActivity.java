package com.valhalla.studiac.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.valhalla.studiac.R;

public class ProfileSetupActivity extends AppCompatActivity implements View.OnClickListener {

    private Button mChangeAvatarBtn;
    private EditText mUniversityNameField;
    private EditText mDepartmentNameField;
    private EditText mCurrentSemesterField;
    private EditText mTotalSemesterField;
    private Button mNextBtn;
    private int mImageId = R.drawable.profilesetup_ic_avator; // default value. If changes, then update it

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profilesetup);
        initViews();
        mNextBtn = findViewById(R.id.nextButton);

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

    public void onNextClick(View view) {
        Log.i("on next click:", "all input valid");
        if (isInputValid()) {
            // todo: upload it to db
            Log.i("on next click:", "all input valid");
            Bundle bundle = getIntent().getExtras();
            bundle.putString("universityName", mDepartmentNameField.getText().toString());
            bundle.putString("departmentName", mUniversityNameField.getText().toString());
            bundle.putInt("currentSemester", Integer.parseInt(mCurrentSemesterField.getText().toString()));
            bundle.putInt("totalSemesters", Integer.parseInt(mTotalSemesterField.getText().toString()));
            bundle.putInt("studentImage", mImageId);
            Intent intent = new Intent(ProfileSetupActivity.this, ProfileSetup2Activity.class);
            intent.putExtras(bundle);
            startActivity(intent);
        }
    }


    @Override
    public void onClick(View v) {


        if (v.getId() == mChangeAvatarBtn.getId()) {
            // @TODO: make SelectImageActivity a fragment
            startActivity(new Intent(getBaseContext(), SelectImageActivity.class));
        }

    }


    public boolean isInputValid() {
        boolean mReturnValue = true;

        if (mUniversityNameField.getText().length() == 0) {
            mUniversityNameField.setError("This Field cannot be Empty");
            mReturnValue = false;
            return mReturnValue;
        }

        if (mDepartmentNameField.getText().length() == 0) {
            mDepartmentNameField.setError("This Field cannot be Empty");
            mReturnValue = false;
        }

        if (mCurrentSemesterField.getText().length() == 0) {
            mCurrentSemesterField.setError("This Field cannot be Empty");
            mReturnValue = false;
        }

        if (mTotalSemesterField.getText().length() == 0) {
            mTotalSemesterField.setError("This Field cannot be Empty");
            mReturnValue = false;
        }

        mReturnValue = true;
        return mReturnValue;
    }

}
