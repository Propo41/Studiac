package com.example.project.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.project.R;

public class ProfileSetupActivity extends AppCompatActivity implements View.OnClickListener {

    private Button changeAvatarBtn;
    private EditText universityNameField;
    private EditText departmentNameField;
    private EditText currentSemesterField;
    private EditText totalSemesterField;
    private Button nextBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profilesetup);
        initialize(savedInstanceState);

    }

    private void initialize(Bundle savedInstanceState){
        universityNameField = (EditText) findViewById(R.id.universityNameField);
        departmentNameField = (EditText) findViewById(R.id.departmentNameField);
        currentSemesterField = (EditText) findViewById(R.id.currentSemesterField);
        totalSemesterField = (EditText) findViewById(R.id.totalSemesterField);
        nextBtn= (Button) findViewById(R.id.nextButton);
        changeAvatarBtn= (Button) findViewById(R.id.changeAvatarBtn);
        universityNameField.setOnClickListener(this);
        departmentNameField.setOnClickListener(this);
        currentSemesterField.setOnClickListener(this);
        totalSemesterField.setOnClickListener(this);
        nextBtn.setOnClickListener(this);
        changeAvatarBtn.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {


        if(v.getId()==universityNameField.getId()){


        }


        if(v.getId()==departmentNameField.getId()){


        }



        if(v.getId()==currentSemesterField.getId()){


        }



        if(v.getId()==totalSemesterField.getId()){


        }


        if(v.getId()==nextBtn.getId()){


        }


        if(v.getId()==changeAvatarBtn.getId()){


        }
    }
}
