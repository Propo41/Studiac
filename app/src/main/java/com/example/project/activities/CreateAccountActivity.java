package com.example.project.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

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
        if(createAccountInputCheck(v)) {
            startActivity(new Intent(CreateAccountActivity.this, ProfileSetupActivity.class));
        }
        else{

        }
    }
    
    public boolean createAccountInputCheck(View v){

        boolean returnValue = true;
        if(mNameField.getText().length() == 0) {
            mNameField.setError("Field cannot be empty!");
            returnValue = false;
        }

        if(mNameField.getText().length() < 6) {
            mNameField.setError("Minimum 6 characters needed");
            returnValue = false;
        }

        if(nEmailField.getText().length() == 0) {
            nEmailField.setError("Field cannot be empty!");
            returnValue = false;
        }

        if(nEmailField.getText().length() < 6){
            nEmailField.setError("Minimum 6 characters needed");
            returnValue =false;
        }

        if(nPasswordField.getText().length() == 0) {
            nPasswordField.setError("Field cannot be empty!");
            returnValue = false;
        }


        if(nPasswordField.getText().length() < 6){
            nPasswordField.setError("Password too weak. Minimum 6 characters needed");
            returnValue =false;
        }

        if(nReTypePasswordField.getText().length() == 0) {
            nReTypePasswordField.setError("Field cannot be empty!");
            returnValue =false;
        }

        if(nReTypePasswordField.getText().length() < 6){
            nReTypePasswordField.setError("Password too weak. Minimum 6 characters needed");
            returnValue =false;
        }

        if(nReTypePasswordField.getText()!= nPasswordField.getText()){
            nReTypePasswordField.setError("Password doesn't match");
            returnValue =true;
        }
        return  returnValue ;
    }

}
