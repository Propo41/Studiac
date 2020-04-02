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
    private EditText mEmailField;
    private EditText mPassswordField;
    private TextView mSignUpBtn;
    private Button mLoginButton;
    private Button mLoginWithFacebook;
    private Button mLoginWithGoogle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initialize(savedInstanceState);



    }

    private void initialize(Bundle savedInstanceState){
        mForgotPasswordBtn =  findViewById(R.id.forgotPasswordBtn);
        mEmailField =  findViewById(R.id.emailField);
        mPassswordField = findViewById(R.id.passwordField);
        mSignUpBtn =  findViewById(R.id.signUpBtn);
        mLoginButton =  findViewById(R.id.loginBtn);
        mLoginWithFacebook = findViewById(R.id.loginWithFacebook);
        mLoginWithGoogle =  findViewById(R.id.loginWithGoogle);
        mForgotPasswordBtn.setOnClickListener(this);
        mEmailField.setOnClickListener(this);
        mPassswordField.setOnClickListener(this);
        mSignUpBtn.setOnClickListener(this);
        mLoginButton.setOnClickListener(this);
        mLoginWithFacebook.setOnClickListener(this);
        mLoginWithGoogle.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        if(v.getId()==mForgotPasswordBtn.getId()){


        }


        if(v.getId()== mEmailField.getId()){


        }


        if(v.getId()== mPassswordField.getId()){


        }


        if(v.getId()== mSignUpBtn.getId()){


        }


        if(v.getId()== mLoginButton.getId()){


        }


        if(v.getId()== mLoginWithFacebook.getId()){


        }


        if(v.getId()== mLoginWithGoogle.getId()){


        }





    }





}
