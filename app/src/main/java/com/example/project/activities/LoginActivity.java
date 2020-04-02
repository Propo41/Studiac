package com.example.project.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.project.R;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView mForgotPasswordBtn;
    private EditText emailField;
    private EditText passswordField;
    private EditText signInBtn;
    private Button loginButton;
    private Button loginWithFacebook;
    private Button loginWithGoogle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initialize(savedInstanceState);



    }

    private void initialize(Bundle savedInstanceState){
        mForgotPasswordBtn = (TextView) findViewById(R.id.forgotPasswordBtn);
        emailField = (EditText) findViewById(R.id.emailField);
        passswordField = (EditText) findViewById(R.id.passwordField);
        signInBtn = (EditText) findViewById(R.id.signInBtn);
        loginButton = (Button) findViewById(R.id.loginBtn);
        loginWithFacebook = (Button) findViewById(R.id.loginWithFacebook);
        loginWithGoogle = (Button) findViewById(R.id.loginWithGoogle);
        mForgotPasswordBtn.setOnClickListener(this);
        emailField.setOnClickListener(this);
        passswordField.setOnClickListener(this);
        signInBtn.setOnClickListener(this);
        loginButton.setOnClickListener(this);
        loginWithFacebook.setOnClickListener(this);
        loginWithGoogle.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        if(v.getId()==mForgotPasswordBtn.getId()){


        }


        if(v.getId()==emailField.getId()){


        }


        if(v.getId()==passswordField.getId()){


        }


        if(v.getId()==signInBtn.getId()){


        }


        if(v.getId()==loginButton.getId()){


        }


        if(v.getId()==loginWithFacebook.getId()){


        }


        if(v.getId()==loginWithGoogle.getId()){


        }





    }





}
