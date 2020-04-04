package com.example.project.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.project.R;

public class CreateAccountActivity extends AppCompatActivity {

    private EditText mNameField;
    private EditText nEmailField;
    private EditText nPasswordField;
    private EditText nReTypePasswordField;
    private Button mCreateAccountBtn;
    private TextView mSignIn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_createaccount);
        initViews();

    }

    private void initViews(){
        nEmailField =  findViewById(R.id.emailField);
        mNameField =  findViewById(R.id.nameField);
        nPasswordField =  findViewById(R.id.passwordField);
        nReTypePasswordField = findViewById(R.id.reTypePasswordField);
        mSignIn =  findViewById(R.id.signInBtn);
        mCreateAccountBtn =  findViewById(R.id.createAccountBtn);

    }

    public void signInPressed(View v)
    {
        startActivity(new Intent(CreateAccountActivity.this, LoginActivity.class));
    }

    public void createAccountPressed(View v)
    {
        startActivity(new Intent(CreateAccountActivity.this, ProfileSetupActivity.class));

    }

}
