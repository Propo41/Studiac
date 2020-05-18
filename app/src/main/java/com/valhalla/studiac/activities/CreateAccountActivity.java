package com.valhalla.studiac.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.valhalla.studiac.R;

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
        nEmailField =  findViewById(R.id.setup_email_id);
        mNameField =  findViewById(R.id.setup_name_id);
        nPasswordField =  findViewById(R.id.setup_password_id);
        nReTypePasswordField = findViewById(R.id.setup_retype_password_id);
        mSignIn =  findViewById(R.id.setup_sign_in_button_id);
        mCreateAccountBtn =  findViewById(R.id.setup_create_account_button_id);
    }

    public void onSignInClick(View v)
    {
        startActivity(new Intent(CreateAccountActivity.this, LoginActivity.class));
    }

    public void onCreateAccountClick(View v)
    {
        if(isInputValid()) {
            Intent intent = new Intent(getBaseContext(), SelectImageActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString("studentName", mNameField.getText().toString());
            bundle.putString("studentEmail", nEmailField.getText().toString());
            bundle.putString("studentPassword", nPasswordField.getText().toString());
            // todo: add a student to the database, and set isProfileSetup to false.
            intent.putExtras(bundle);
            startActivity(intent);

        }


    }

    public boolean isInputValid(){

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
        returnValue = true; // for debug
        return  returnValue ;
    }

}
