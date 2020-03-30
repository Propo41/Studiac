package com.example.project;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class CreateNewAccount extends AppCompatActivity implements View.OnClickListener {

    private EditText nameField;
    private EditText emailField;
    private EditText passwordField;
    private EditText reTypePasswordField;
    private Button createAccountBtn;
    private EditText alreadySignIn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_new_account);
        initialize(savedInstanceState);


    }

    private void initialize(Bundle savedInstanceState){
        emailField = (EditText) findViewById(R.id.emailField);
        nameField = (EditText) findViewById(R.id.nameField);
        passwordField = (EditText) findViewById(R.id.passwordField);
        reTypePasswordField = (EditText) findViewById(R.id.reTypePasswordField);
        alreadySignIn = (EditText) findViewById(R.id.alreadySignedInBtn);
        createAccountBtn = (Button) findViewById(R.id.createAccountBtn);
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
