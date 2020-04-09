package com.example.project.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.project.R;

public class ForgotPasswordActivity extends AppCompatActivity {

    private EditText mEmailField;
    private Button mConfirmBtn;
    private TextView mSignInBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgotpassword);
        initViews();
    }

    private void initViews(){
        mSignInBtn = findViewById(R.id.signInBtn);
        mEmailField =  findViewById(R.id.emailField);
        mConfirmBtn =  findViewById(R.id.confirmBtn);

    }

    public void signInPressed(View v)
    {
        startActivity(new Intent(ForgotPasswordActivity.this, LoginActivity.class));
    }

    public void confirmPressed(View v)
    {

        if(forgotPassWordInputCheck(v)){

        }
        // TODO: open dialog box
    }

    public boolean forgotPassWordInputCheck(View v) {
        boolean mReturnValue = true;
        if(mEmailField.getText().length() == 0) {
            mEmailField.setError("Field cannot be empty!");
            mReturnValue = false;
        }

        return  mReturnValue;

    }


}
