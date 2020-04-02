package com.example.project.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.project.R;

public class ForgotPasswordAlertActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText mEmailField;
    private Button mConfirmBtn;
    private TextView mSignInBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgotpassword);
        initialize(savedInstanceState);
    }


    private void initialize(Bundle savedInstanceState){
        mSignInBtn = findViewById(R.id.signInBtn);
        mEmailField =  findViewById(R.id.emailField);
        mConfirmBtn =  findViewById(R.id.confirmBtn);
        mSignInBtn.setOnClickListener(this);
        mEmailField.setOnClickListener(this);
        mConfirmBtn.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        if(v.getId()== mSignInBtn.getId()){


        }

        if(v.getId()== mEmailField.getId()){


        }

        if(v.getId()== mConfirmBtn.getId()){


        }
    }
}
