package com.example.project;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class ForgotPasswordAlert extends AppCompatActivity implements View.OnClickListener {

    private EditText emailField;
    private Button confirmBtn;
    private EditText signInBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password_alert);
        initialize(savedInstanceState);
    }


    private void initialize(Bundle savedInstanceState){
        signInBtn = (EditText) findViewById(R.id.signInBtn);
        emailField = (EditText) findViewById(R.id.emailField);
        confirmBtn = (Button) findViewById(R.id.confirmBtn);
        signInBtn.setOnClickListener(this);
        emailField.setOnClickListener(this);
        confirmBtn.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        if(v.getId()==signInBtn.getId()){


        }

        if(v.getId()==emailField.getId()){


        }

        if(v.getId()==confirmBtn.getId()){


        }
    }
}
