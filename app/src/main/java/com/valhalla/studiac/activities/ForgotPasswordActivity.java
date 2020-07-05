package com.valhalla.studiac.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseNetworkException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.valhalla.studiac.R;
import com.valhalla.studiac.fragments.dialogs.ForgotPasswordEmailSentDialog;
import com.valhalla.studiac.utility.ErrorStyle;


public class ForgotPasswordActivity extends AppCompatActivity {

    private EditText mEmailField;
    private TextView mSignInButton;
    private Button mConfirmButton;
    private ErrorStyle errorStyle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgotpassword);
        initViews();
        attachListeners();
        errorStyle.resetError(mEmailField);
    }

    private void attachListeners() {

        mSignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ForgotPasswordActivity.this, LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);

            }
        });

        mConfirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isInputValid()) {
                    FirebaseAuth.getInstance().sendPasswordResetEmail(mEmailField.getText().toString()).
                            addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        ForgotPasswordEmailSentDialog dialog = new ForgotPasswordEmailSentDialog();
                                        dialog.show(getSupportFragmentManager(), "ForgotPasswordActivity");

                                    } else {
                                        checkCause(task.getException());
                                    }
                                }
                            });
                }
            }


        });


    }


    private void checkCause(Exception exception) {

        // If create account creation fails, display a message to the user.
        if (exception instanceof FirebaseAuthInvalidCredentialsException) {
            // invalid email format entered
            errorStyle.setError("Invalid Format!", mEmailField);
        } else if (exception instanceof FirebaseNetworkException) {
            errorStyle.setError("Connection Timeout", mEmailField);
        } else {
            errorStyle.setError("Something's Wrong", mEmailField);
        }

    }


    private void initViews() {
        mSignInButton = findViewById(R.id.setup_sign_in_button_id);
        mEmailField = findViewById(R.id.setup_email_id);
        mConfirmButton = findViewById(R.id.setup_confirm_button);
        errorStyle = ErrorStyle.getInstance(getApplicationContext());
        errorStyle.resetError(mEmailField);
    }

    public boolean isInputValid() {

        if (mEmailField.getText().length() == 0) {
            errorStyle.setError(getString(R.string.Error_EmptyField), mEmailField);
            return false;
        } else {
            errorStyle.resetError(mEmailField);
        }
        return true;
    }


}
