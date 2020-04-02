package com.example.project.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.project.R;

public class CreateAccountActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText nameField;
    private EditText emailField;
    private EditText passwordField;
    private EditText reTypePasswordField;
    private Button createAccountBtn;
    private EditText alreadySignIn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_createaccount);
        initialize(savedInstanceState);


    }

    private void initialize(Bundle savedInstanceState){
        emailField =  findViewById(R.id.emailField);
        nameField =  findViewById(R.id.nameField);
        passwordField =  findViewById(R.id.passwordField);
        reTypePasswordField = findViewById(R.id.reTypePasswordField);
        alreadySignIn =  findViewById(R.id.alreadySignedInBtn);
        createAccountBtn =  findViewById(R.id.createAccountBtn);
        emailField.setOnClickListener(this);
        nameField.setOnClickListener(this);
        passwordField.setOnClickListener(this);
        reTypePasswordField.setOnClickListener(this);
        alreadySignIn.setOnClickListener(this);
        createAccountBtn.setOnClickListener(this);

    }


    @Override
    public void onClick(View v) {

        if(v.getId()==emailField.getId()){


        }

        if(v.getId()==nameField.getId()){


        }
        if(v.getId()==reTypePasswordField.getId()){


        }

        if(v.getId()==alreadySignIn.getId()){


        }

        if(v.getId()==passwordField.getId()){


        }

        if(v.getId()==createAccountBtn.getId()){


        }
    }
}
