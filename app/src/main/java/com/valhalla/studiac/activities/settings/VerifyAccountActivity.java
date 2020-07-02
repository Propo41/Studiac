package com.valhalla.studiac.activities.settings;

import androidx.annotation.NonNull;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.valhalla.studiac.R;
import com.valhalla.studiac.toolbars.NavigationToolbarWhite;
import com.valhalla.studiac.utility.Common;

import java.util.Objects;

public class VerifyAccountActivity extends NavigationToolbarWhite {

    private EditText mInput;
    private FloatingActionButton mGoogleSignIn;
    private FirebaseUser mUser;
    private Button mVerifyButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContent(R.layout.activity_verify_account);

        mUser = FirebaseAuth.getInstance().getCurrentUser();
        if (super.isUserAuthenticated(mUser)) {
            mInput = findViewById(R.id.reauthenticate_input_id);
            mGoogleSignIn = findViewById(R.id.reauthenticate_google_button_id);
            mVerifyButton = findViewById(R.id.reauthenticate_verify_button_id);
            onVerifyClick();
            onGoogleClick();
        }


    }

    private void onGoogleClick() {
        mGoogleSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get the account instance
                GoogleSignInAccount googleAcc = GoogleSignIn.getLastSignedInAccount(getApplicationContext());
                // if user is signed in using google
                if (googleAcc != null) {
                    AuthCredential credential = GoogleAuthProvider.getCredential(googleAcc.getIdToken(), null);
                    mUser.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(VerifyAccountActivity.this, "Verification Complete", Toast.LENGTH_SHORT).show();
                                // send verified value to the parent activity using intent
                                Intent resultIntent = new Intent();
                                Bundle bundle = new Bundle();
                                bundle.putBoolean("verified", true);
                                resultIntent.putExtras(bundle);
                                setResult(RESULT_OK, resultIntent);
                                finish();
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            if (e instanceof FirebaseAuthInvalidCredentialsException) {
                                // The supplied auth credential is malformed or has expired
                                // sign in again
                              /*  Intent intent = new Intent(VerifyAccountActivity.this, LoginActivity.class);
                                // adding flags to intent so that in the new activity, pressing back button doesn't bring this activity back
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                //startActivity(intent);*/
                            }
                            mInput.setError(e.getLocalizedMessage());
                            Common.addStroke(mInput, 5);
                            Log.d("FIREBASE", e.toString());


                        }
                    });
                } else {
                    Toast.makeText(VerifyAccountActivity.this, "Not Signed In With Google", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }

    private boolean isGoogleUser() {
        GoogleSignInAccount googleSignInAccount = GoogleSignIn.getLastSignedInAccount(getApplicationContext());
        return googleSignInAccount != null;
    }

    private void onVerifyClick() {
        mVerifyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!isGoogleUser()) {
                    if (isInputValid()) {
                        AuthCredential credential = EmailAuthProvider
                                .getCredential(Objects.requireNonNull(mUser.getEmail()), mInput.getText().toString());
                        mUser.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Toast.makeText(VerifyAccountActivity.this, "Verification Complete", Toast.LENGTH_SHORT).show();
                                // send verified value to the parent activity
                                // using intent
                                Intent resultIntent = new Intent();
                                Bundle bundle = new Bundle();
                                bundle.putBoolean("verified", true);
                                resultIntent.putExtras(bundle);
                                setResult(RESULT_OK, resultIntent);
                                finish();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                if (e instanceof FirebaseAuthInvalidCredentialsException) {
                                    mInput.setError("Incorrect Credentials");
                                    Common.addStroke(mInput, 5);
                                } else {
                                    Toast.makeText(VerifyAccountActivity.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                                    Log.d("FIREBASE", e.toString());

                                }
                                Log.d("FIREBASE", e.toString());
                            }
                        });
                    }
                } else {
                    Toast.makeText(VerifyAccountActivity.this, "Your current account was signed in using google!", Toast.LENGTH_SHORT).show();
                }

            }
        });

    }

    private boolean isInputValid() {
        if (mInput.getText().toString().isEmpty()) {
            mInput.setError("Field Cannot Be Empty");
            Common.addStroke(mInput, 5);
            return false;
        } else {
            mInput.setError(null);
            Common.addStroke(mInput, 0);
        }
        return true;
    }
}
