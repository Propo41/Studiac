package com.example.project.activities;

import androidx.appcompat.app.AppCompatActivity;

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
        initialize(savedInstanceState);

    }

    private void initialize(Bundle savedInstanceState){
        mUniversityNameField = (EditText) findViewById(R.id.universityNameField);
        mDepartmentNameField = (EditText) findViewById(R.id.departmentNameField);
        mCurrentSemesterField = (EditText) findViewById(R.id.currentSemesterField);
        mTotalSemesterField = (EditText) findViewById(R.id.totalSemesterField);
        mNextBtn = (Button) findViewById(R.id.nextButton);
        mChangeAvatarBtn = (Button) findViewById(R.id.changeAvatarBtn);
        mUniversityNameField.setOnClickListener(this);
        mDepartmentNameField.setOnClickListener(this);
        mCurrentSemesterField.setOnClickListener(this);
        mTotalSemesterField.setOnClickListener(this);
        mNextBtn.setOnClickListener(this);
        mChangeAvatarBtn.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {


        if(v.getId()== mUniversityNameField.getId()){


        }


        if(v.getId()== mDepartmentNameField.getId()){


        }



        if(v.getId()== mCurrentSemesterField.getId()){


        }



        if(v.getId()== mTotalSemesterField.getId()){


        }


        if(v.getId()== mNextBtn.getId()){


        }


        if(v.getId()== mChangeAvatarBtn.getId()){


        }
    }
}
