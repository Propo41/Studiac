package com.valhalla.studiac.activities;

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

import com.valhalla.studiac.R;


public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView mForgotPasswordBtn;
    private EditText mEmailField;
    private EditText mPasswordField;
    private TextView mSignUpBtn;
    private Button mLoginButton;
    private Button mLoginWithFacebook;
    private Button mLoginWithGoogle;
    final Context context = this;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initViews();
    }

    private void initViews() {
        mForgotPasswordBtn = findViewById(R.id.login_forgot_password_button_id);
        mEmailField = findViewById(R.id.setup_email_id);
        mPasswordField = findViewById(R.id.setup_password_id);
        mSignUpBtn = findViewById(R.id.login_sign_up_button_id);
        mLoginButton = findViewById(R.id.login_login_button_id);
        mLoginWithGoogle = findViewById(R.id.loginWithGoogle);
        mForgotPasswordBtn.setOnClickListener(this);
        mEmailField.setOnClickListener(this);
        mPasswordField.setOnClickListener(this);
        mSignUpBtn.setOnClickListener(this);
        mLoginButton.setOnClickListener(this);
        mLoginWithFacebook.setOnClickListener(this);
        mLoginWithGoogle.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        if (v.getId() == mForgotPasswordBtn.getId()) {
            startActivity(new Intent(getBaseContext(), ForgotPasswordActivity.class));
        }


        if (v.getId() == mSignUpBtn.getId()) {
            startActivity(new Intent(getBaseContext(), CreateAccountActivity.class));
        }


        if (v.getId() == mLoginButton.getId()) {
            if (loginInputErrorCheck()) {
                // code after successful login
                startActivity(new Intent(getBaseContext(), DashboardActivity.class));

            } else {

                // dialog pops up if incorrect credentials entered
                final Dialog dialogLoginIncorrect = new Dialog(context);
                dialogLoginIncorrect.setContentView(R.layout.dialog_login_incorrect);
                Button dialogLoginIncorrectButton = dialogLoginIncorrect.findViewById(R.id.alert_button_id);

                dialogLoginIncorrectButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v0) {
                        dialogLoginIncorrect.dismiss();
                    }
                });

                dialogLoginIncorrect.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialogLoginIncorrect.show();

            }
        }




    }


    public boolean loginInputErrorCheck() {
        boolean mReturnValue = true;
        if (mEmailField.getText().length() == 0) {
            mEmailField.setError("This field cannot be empty");
            mReturnValue = false;
        }

        if (mPasswordField.getText().length() == 0 && mReturnValue) {
            mPasswordField.setError("This field cannot be empty");
            mReturnValue = false;
        }


        return mReturnValue;

    }


}
