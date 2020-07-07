package com.valhalla.studiac.activities.settings;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.InputType;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;

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
import com.valhalla.studiac.utility.ErrorStyle;

import java.util.Objects;

public class VerifyAccountActivity extends NavigationToolbarWhite {

    private EditText mInput;
    private FloatingActionButton mGoogleSignIn;
    private FirebaseUser mUser;
    private Button mVerifyButton;
    private ErrorStyle errorStyle;
    private Boolean mInputReveal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContent(R.layout.activity_verify_account);
        errorStyle = ErrorStyle.getInstance(getApplicationContext());
        mUser = FirebaseAuth.getInstance().getCurrentUser();
        mInputReveal = false;
        if (super.isUserAuthenticated(mUser)) {
            mInput = findViewById(R.id.reauthenticate_input_id);
            errorStyle.resetError(mInput);
            mGoogleSignIn = findViewById(R.id.reauthenticate_google_button_id);
            mVerifyButton = findViewById(R.id.reauthenticate_verify_button_id);
            attachTouchListener();
            onVerifyClick();
            onGoogleClick();
        }

    }

    // cause mustofa is cold hearted
    @SuppressLint("ClickableViewAccessibility")
    private void attachTouchListener() {
        mInput.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                //       Toast.makeText(ShowHidePasswordEditText.this, motionEvent.getX()+"  " +motionEvent.getY(), Toast.LENGTH_SHORT).show();
                //Toast.makeText(ShowHidePasswordEditText.this, motionEvent.getSize()+"", Toast.LENGTH_SHORT).show();
                if (MotionEvent.ACTION_DOWN == motionEvent.getAction()) {
                    if (motionEvent.getX() > mInput.getRight() - 250) {
                        if (!mInputReveal) {
                            mInput.setInputType(   //to unhide
                                    InputType.TYPE_CLASS_TEXT |
                                            InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
                            );

                            mInput.setCompoundDrawablesWithIntrinsicBounds(R.drawable.settings_ic_password_lock_ash, 0, R.drawable.ic_eye_visible_ash, 0);
                            mInputReveal = true;

                            Typeface typeface = ResourcesCompat.getFont(getApplicationContext(), R.font.montserrat_regular);
                            mInput.setTypeface(typeface);


                        } else {
                            //to hide
                            mInput.setTransformationMethod(PasswordTransformationMethod.getInstance());
                            mInput.setCompoundDrawablesWithIntrinsicBounds(R.drawable.settings_ic_password_lock_ash, 0, R.drawable.ic_eye_invisible_ash, 0);
                            mInputReveal = false;
                            Typeface typeface = ResourcesCompat.getFont(getApplicationContext(), R.font.montserrat_regular);
                            mInput.setTypeface(typeface);

                        }
                    }
                }
                return false;
            }


        });
    }

    private void onGoogleClick() {
        mGoogleSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                VerifyAccountActivity.super.startLoadingAnimation();

                // Get the account instance
                GoogleSignInAccount googleAcc = GoogleSignIn.getLastSignedInAccount(getApplicationContext());
                // if user is signed in using google
                if (googleAcc != null) {
                    AuthCredential credential = GoogleAuthProvider.getCredential(googleAcc.getIdToken(), null);
                    mUser.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                VerifyAccountActivity.super.stopLoadingAnimation();

                                Toast.makeText(VerifyAccountActivity.this, "Verification complete", Toast.LENGTH_SHORT).show();
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
                            errorStyle.setError(e.getLocalizedMessage(), mInput);
                            Log.d("FIREBASE", e.toString());
                            VerifyAccountActivity.super.stopLoadingAnimation();

                        }
                    });
                } else {
                    VerifyAccountActivity.super.stopLoadingAnimation();
                    createToast("Not signed in with google");
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
                        VerifyAccountActivity.super.startLoadingAnimation();
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
                                VerifyAccountActivity.super.stopLoadingAnimation();

                                finish();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                VerifyAccountActivity.super.stopLoadingAnimation();

                                if (e instanceof FirebaseAuthInvalidCredentialsException) {
                                    errorStyle.setError(getString(R.string.Error_IncorrectCredential), mInput);
                                } else {
                                    Toast.makeText(VerifyAccountActivity.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                                    Log.d("FIREBASE", e.toString());
                                }
                                Log.d("FIREBASE", e.toString());
                            }
                        });
                    }
                } else {
                    VerifyAccountActivity.super.stopLoadingAnimation();
                    createToast("Your current account was signed in using google!");
                }

            }
        });

    }

    private void createToast(String message) {
        Toast toast = Toast.makeText(VerifyAccountActivity.this, message, Toast.LENGTH_SHORT);
        TextView v = toast.getView().findViewById(android.R.id.message);
        if (v != null) {
            v.setGravity(Gravity.CENTER);
        }
        toast.show();
    }


    private boolean isInputValid() {
        if (mInput.getText().toString().isEmpty()) {
            errorStyle.setError(getString(R.string.Error_EmptyField), mInput);
            return false;
        } else {
            errorStyle.resetError(mInput);
        }
        return true;
    }
}
