package com.valhalla.studiac.activities;

import androidx.annotation.NonNull;
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
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.valhalla.studiac.R;
import com.valhalla.studiac.utility.Common;

import org.w3c.dom.Text;

public class ForgotPasswordActivity extends AppCompatActivity {

    private EditText mEmailField;
    final Context context = this;
    private TextView mSignInButton;
    private Button mConfirmButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgotpassword);
        initViews();
        attachListeners();
        Common.addStroke(mEmailField, 0);
    }

    private void attachListeners() {

        mSignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ForgotPasswordActivity.this, LoginActivity.class));
            }
        });

        mConfirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(isInputValid()){
                    FirebaseAuth.getInstance().sendPasswordResetEmail(mEmailField.getText().toString()).
                            addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                        Toast.makeText(context, "Check email for resetting password", Toast.LENGTH_SHORT).show();
                                    }else{
                                        Toast.makeText(context, "This email is not registered", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });

                    openDialog();
                }else{
                    // TODO: show error popup with border when email not valid or no input provided
                    Common.addStroke(mEmailField, 5);
                    mEmailField.setError("");
                }
            }


        });


    }

    private void initViews(){
        mSignInButton = findViewById(R.id.setup_sign_in_button_id);
        mEmailField =  findViewById(R.id.setup_email_id);
        mConfirmButton = findViewById(R.id.setup_confirm_button);
    }


    private void openDialog() {
        final Dialog dialogForgotPasswordEmailSent = new Dialog(context);
        dialogForgotPasswordEmailSent.setContentView(R.layout.dialog_forgotpassword_emailsent);
        Button dialogForgotPasswordEmailSentButton = dialogForgotPasswordEmailSent.findViewById(R.id.forgotpassword_button_id);
        dialogForgotPasswordEmailSentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v0) {
                dialogForgotPasswordEmailSent.dismiss();
                startActivity(new Intent(ForgotPasswordActivity.this, LoginActivity.class));
            }
        });
        dialogForgotPasswordEmailSent.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialogForgotPasswordEmailSent.show();
    }

    public boolean isInputValid() {
        boolean mReturnValue = true;
        if(mEmailField.getText().length() == 0) {
            mEmailField.setError("Field cannot be empty!");
            mReturnValue = false;
        }

        return  mReturnValue;

    }


}
