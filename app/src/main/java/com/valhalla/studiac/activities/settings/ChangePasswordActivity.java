package com.valhalla.studiac.activities.settings;

import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;

import android.annotation.SuppressLint;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.InputType;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.valhalla.studiac.R;
import com.valhalla.studiac.toolbars.NavigationToolbarWhite;
import com.valhalla.studiac.utility.Common;

public class ChangePasswordActivity extends NavigationToolbarWhite {

    private EditText mOldPassword;
    private EditText mNewPassword;
    private EditText mNewPasswordConfirm;
    private Button changeButton;
    private FirebaseUser mUser;
    private Boolean mNewPasswordReveal;
    private Boolean mNewPasswordConfirmReveal;
    private Boolean mOldPasswordReveal;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContent(R.layout.activity_change_password);

        mUser = FirebaseAuth.getInstance().getCurrentUser();
        if (isUserAuthenticated(mUser)) {
            initViews();
            attachInputTouchListeners();
            onUpdateClick();
        }

    }

    @SuppressLint("ClickableViewAccessibility")
    private void attachInputTouchListeners() {
        // TODO: @mustofa: DO IT
/*
        mOldPassword.setOnTouchListener(new View.OnTouchListener() {
            final int DRAWABLE_RIGHT = 2;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    Toast.makeText(ChangePasswordActivity.this, event.getX() + "", Toast.LENGTH_SHORT).show();
                    if ((event.getRawX() <= (mOldPassword.getRight() - mOldPassword.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width()))) {
                        // your action here
                        Toast.makeText(ChangePasswordActivity.this, "clicked", Toast.LENGTH_SHORT).show();

                        return true;
                    }
                }
                return false;
            }
        });
*/

        mOldPassword.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                //       Toast.makeText(ShowHidePasswordEditText.this, motionEvent.getX()+"  " +motionEvent.getY(), Toast.LENGTH_SHORT).show();
                //Toast.makeText(ShowHidePasswordEditText.this, motionEvent.getSize()+"", Toast.LENGTH_SHORT).show();
                if(MotionEvent.ACTION_DOWN == motionEvent.getAction()) {
                    if (motionEvent.getX() > mOldPassword.getRight() - 250) {
                        if (!mOldPasswordReveal) {
                            mOldPassword.setInputType(   //to unhide
                                    InputType.TYPE_CLASS_TEXT |
                                            InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
                            );

                            mOldPassword.setCompoundDrawablesWithIntrinsicBounds(R.drawable.settings_ic_password_lock_ash, 0, R.drawable.common_ic_eye_disabled_ash, 0);
                            mOldPasswordReveal =true;

                            Typeface typeface = ResourcesCompat.getFont(getApplicationContext(), R.font.montserrat_regular);
                            mOldPassword.setTypeface(typeface);


                        } else if(mOldPasswordReveal){
                            //to hide

                            mOldPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                            mOldPassword.setCompoundDrawablesWithIntrinsicBounds(R.drawable.settings_ic_password_lock_ash, 0, R.drawable.common_ic_eye_enabled_ash, 0);
                            mOldPasswordReveal = false;

                            Typeface typeface = ResourcesCompat.getFont(getApplicationContext(), R.font.montserrat_regular);
                            mOldPassword.setTypeface(typeface);

                        }
                    }
                }
                return false;
            }


        });

        mNewPassword.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                //       Toast.makeText(ShowHidePasswordEditText.this, motionEvent.getX()+"  " +motionEvent.getY(), Toast.LENGTH_SHORT).show();
                //Toast.makeText(ShowHidePasswordEditText.this, motionEvent.getSize()+"", Toast.LENGTH_SHORT).show();
                if(MotionEvent.ACTION_DOWN == motionEvent.getAction()) {
                    if (motionEvent.getX() > mNewPassword.getRight() - 250) {
                        if (!mNewPasswordReveal) {
                            mNewPassword.setInputType(   //to unhide
                                    InputType.TYPE_CLASS_TEXT |
                                            InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
                            );

                            mNewPassword.setCompoundDrawablesWithIntrinsicBounds(R.drawable.settings_ic_password_lock_ash, 0, R.drawable.common_ic_eye_disabled_ash, 0);
                            mNewPasswordReveal =true;

                            Typeface typeface = ResourcesCompat.getFont(getApplicationContext(), R.font.montserrat_regular);
                            mNewPassword.setTypeface(typeface);


                        } else if(mNewPasswordReveal){
                            //to hide

                            mNewPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                            mNewPassword.setCompoundDrawablesWithIntrinsicBounds(R.drawable.settings_ic_password_lock_ash, 0, R.drawable.common_ic_eye_enabled_ash, 0);
                            mNewPasswordReveal = false;


                            Typeface typeface = ResourcesCompat.getFont(getApplicationContext(), R.font.montserrat_regular);
                            mNewPassword.setTypeface(typeface);


                        }
                    }
                }
                return false;
            }


        });




        mNewPasswordConfirm.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                //       Toast.makeText(ShowHidePasswordEditText.this, motionEvent.getX()+"  " +motionEvent.getY(), Toast.LENGTH_SHORT).show();
                //Toast.makeText(ShowHidePasswordEditText.this, motionEvent.getSize()+"", Toast.LENGTH_SHORT).show();
                if(MotionEvent.ACTION_DOWN == motionEvent.getAction()) {
                    if (motionEvent.getX() > mNewPasswordConfirm.getRight() - 250) {
                        if (!mNewPasswordConfirmReveal) {
                            mNewPasswordConfirm.setInputType(   //to unhide
                                    InputType.TYPE_CLASS_TEXT |
                                            InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
                            );

                            mNewPasswordConfirm.setCompoundDrawablesWithIntrinsicBounds(R.drawable.settings_ic_password_lock_ash, 0, R.drawable.common_ic_eye_disabled_ash, 0);
                            mNewPasswordConfirmReveal =true;

                            Typeface typeface = ResourcesCompat.getFont(getApplicationContext(), R.font.montserrat_regular);
                            mNewPasswordConfirm.setTypeface(typeface);


                        } else if(mNewPasswordConfirmReveal){
                            //to hide

                            mNewPasswordConfirm.setTransformationMethod(PasswordTransformationMethod.getInstance());
                            mNewPasswordConfirm.setCompoundDrawablesWithIntrinsicBounds(R.drawable.settings_ic_password_lock_ash, 0, R.drawable.common_ic_eye_enabled_ash, 0);
                            mNewPasswordConfirmReveal = false;


                            Typeface typeface = ResourcesCompat.getFont(getApplicationContext(), R.font.montserrat_regular);
                            mNewPasswordConfirm.setTypeface(typeface);

                        }
                    }
                }
                return false;
            }
        });


    }

    /*
     * checks if all inputs are vaLid and the old password matches with the current account's password
     * if so, then update the password
     */
    private void onUpdateClick() {
        changeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (inputValid()) {
                    AuthCredential credential = EmailAuthProvider
                            .getCredential(mUser.getEmail(), mNewPasswordConfirm.getText().toString());
                    // changing the password requires an additional authentication
                    mUser.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                // after old password matches, update the password
                                mUser.updatePassword(mNewPasswordConfirm.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            Log.d("FIREBASE AUTH", "Password updated");
                                            Toast.makeText(ChangePasswordActivity.this, "Changes Saved", Toast.LENGTH_SHORT).show();

                                        } else {
                                            Log.d("FIREBASE AUTH", "Error password not updated");
                                            Toast.makeText(ChangePasswordActivity.this, "Changes Couldn't Be Saved", Toast.LENGTH_SHORT).show();

                                        }
                                    }
                                });
                            } else {
                                Toast.makeText(ChangePasswordActivity.this, "Changes Couldn't Be Saved", Toast.LENGTH_SHORT).show();
                                Log.d("FIREBASE AUTH", "Error auth failed");
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            if (e instanceof FirebaseAuthInvalidCredentialsException) {
                                Toast.makeText(ChangePasswordActivity.this, "Invalid Password Entered", Toast.LENGTH_SHORT).show();
                            }
                            e.printStackTrace();
                        }
                    });

                }
            }
        });
    }

    private boolean inputValid() {
        String oldPass = mOldPassword.getText().toString();
        String newPass = mNewPassword.getText().toString();
        String confirmPass = mNewPasswordConfirm.getText().toString();

        if (oldPass.isEmpty()) {
            mOldPassword.setError("Field cannot be empty");
            Common.addStroke(mOldPassword, 5);
            return false;
        } else {
            mOldPassword.setError(null);
            Common.addStroke(mOldPassword, 0);
        }

        if (newPass.isEmpty()) {
            mNewPassword.setError("Field cannot be empty");
            Common.addStroke(mNewPassword, 5);
            return false;
        } else {
            mNewPassword.setError(null);
            Common.addStroke(mNewPassword, 0);
        }

        if (confirmPass.isEmpty()) {
            mNewPasswordConfirm.setError("Field cannot be empty");
            Common.addStroke(mNewPasswordConfirm, 5);
            return false;
        } else {
            mNewPasswordConfirm.setError(null);
            Common.addStroke(mNewPasswordConfirm, 0);
        }


        if (!newPass.equals(confirmPass)) {
            mNewPasswordConfirm.setError("Password Doesn't Match");
            Common.addStroke(mNewPasswordConfirm, 5);
            return false;
        } else {
            mNewPasswordConfirm.setError(null);
            Common.addStroke(mNewPasswordConfirm, 0);
        }


        return true;
    }

    private void initViews() {
        mOldPasswordReveal =false;
        mNewPasswordReveal = false;
        mNewPasswordConfirmReveal = false;
        mOldPassword = findViewById(R.id.change_password_old_id);
        mNewPassword = findViewById(R.id.change_password_new_id);
        mNewPasswordConfirm = findViewById(R.id.change_password_retype_id);
        changeButton = findViewById(R.id.change_password_button_id);
    }
}
