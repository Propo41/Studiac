package com.example.project.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.project.R;

public class CreateAccountActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText mNameField;
    private EditText nEmailField;
    private EditText nPasswordField;
    private EditText nReTypePasswordField;
    private Button mCreateAccountBtn;
    private TextView mAlreadySignIn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_createaccount);
        initialize(savedInstanceState);

    }

    private void initialize(Bundle savedInstanceState){
        nEmailField =  findViewById(R.id.emailField);
        mNameField =  findViewById(R.id.nameField);
        nPasswordField =  findViewById(R.id.passwordField);
        nReTypePasswordField = findViewById(R.id.reTypePasswordField);
        mAlreadySignIn =  findViewById(R.id.alreadySignedInBtn);
        mCreateAccountBtn =  findViewById(R.id.createAccountBtn);
        nEmailField.setOnClickListener(this);
        mNameField.setOnClickListener(this);
        nPasswordField.setOnClickListener(this);
        nReTypePasswordField.setOnClickListener(this);
        mAlreadySignIn.setOnClickListener(this);
        mCreateAccountBtn.setOnClickListener(this);

    }


    @Override
    public void onClick(View v) {

        if(v.getId()==nEmailField.getId()){


        }

        if(v.getId()==mNameField.getId()){


        }
        if(v.getId()==nReTypePasswordField.getId()){


        }

        if(v.getId()==mAlreadySignIn.getId()){


        }

        if(v.getId()==nPasswordField.getId()){


        }

        if(v.getId()==mCreateAccountBtn.getId()){


        }
    }
}
