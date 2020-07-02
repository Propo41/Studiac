package com.valhalla.studiac.activities.setup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.SystemClock;
import android.text.InputType;
import android.text.TextUtils;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseNetworkException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.valhalla.studiac.R;
import com.valhalla.studiac.activities.LoginActivity;
import com.valhalla.studiac.database.Firebase;
import com.valhalla.studiac.utility.Common;
import com.valhalla.studiac.utility.SharedPreference;

import java.util.HashMap;

public class CreateAccountActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText mNameField;
    private EditText mEmailField;
    private EditText mPasswordField;
    private EditText mReTypePasswordField;
    private Button mCreateAccountBtn;
    private TextView mSignIn;
    // [START] firebase_authentication
    private FirebaseAuth mAuth;
    // [END] firebase_authentication
    private static final String TAG = "GoogleActivity";
    private long mLastClickTime = 0;
    private final int BUTTON_DELAY_TIME = 1000;
    private ProgressBar mProgressBar;
    private Boolean mPasswordReveal;
    private Boolean mReTypePasswordReveal;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_createaccount);
        mAuth = FirebaseAuth.getInstance();
        initViews();
        attachListener();
        attachTouchListeners();

    }

    private void attachTouchListeners() {

        mPasswordField.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                //       Toast.makeText(ShowHidePasswordEditText.this, motionEvent.getX()+"  " +motionEvent.getY(), Toast.LENGTH_SHORT).show();
                //Toast.makeText(ShowHidePasswordEditText.this, motionEvent.getSize()+"", Toast.LENGTH_SHORT).show();
                if (MotionEvent.ACTION_DOWN == motionEvent.getAction()) {
                    if (motionEvent.getX() > mPasswordField.getRight() - 250) {
                        if (!mPasswordReveal) {
                            mPasswordField.setInputType(   //to unhide
                                    InputType.TYPE_CLASS_TEXT |
                                            InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
                            );

                            mPasswordField.setCompoundDrawablesWithIntrinsicBounds(R.drawable.settings_ic_password_lock, 0, R.drawable.common_ic_eye_disabled_white, 0);
                            mPasswordReveal = true;

                            Typeface typeface = ResourcesCompat.getFont(getApplicationContext(), R.font.montserrat_regular);
                            mPasswordField.setTypeface(typeface);


                        } else if (mPasswordReveal) {
                            //to hide

                            mPasswordField.setTransformationMethod(PasswordTransformationMethod.getInstance());
                            mPasswordField.setCompoundDrawablesWithIntrinsicBounds(R.drawable.settings_ic_password_lock, 0, R.drawable.common_ic_eye_enabled_white, 0);
                            mPasswordReveal = false;


                            Typeface typeface = ResourcesCompat.getFont(getApplicationContext(), R.font.montserrat_regular);
                            mPasswordField.setTypeface(typeface);


                        }
                    }
                }
                return false;
            }
        });


        mReTypePasswordField.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                //       Toast.makeText(ShowHidePasswordEditText.this, motionEvent.getX()+"  " +motionEvent.getY(), Toast.LENGTH_SHORT).show();
                //Toast.makeText(ShowHidePasswordEditText.this, motionEvent.getSize()+"", Toast.LENGTH_SHORT).show();
                if (MotionEvent.ACTION_DOWN == motionEvent.getAction()) {
                    if (motionEvent.getX() > mPasswordField.getRight() - 250) {
                        if (!mReTypePasswordReveal) {
                            mReTypePasswordField.setInputType(   //to unhide
                                    InputType.TYPE_CLASS_TEXT |
                                            InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
                            );

                            //Typeface typeface = getResources().getFont(R.font.myfont);
                            Typeface typeface = ResourcesCompat.getFont(getApplicationContext(), R.font.montserrat_regular);
                            mReTypePasswordField.setTypeface(typeface);

                            mReTypePasswordField.setCompoundDrawablesWithIntrinsicBounds(R.drawable.settings_ic_password_lock, 0, R.drawable.common_ic_eye_disabled_white, 0);
                            mReTypePasswordReveal = true;
                        } else if (mReTypePasswordReveal) {
                            //to hide
                            Typeface typeface = ResourcesCompat.getFont(getApplicationContext(), R.font.montserrat_regular);
                            mReTypePasswordField.setTypeface(typeface);
                            mReTypePasswordField.setTransformationMethod(PasswordTransformationMethod.getInstance());
                            mReTypePasswordField.setCompoundDrawablesWithIntrinsicBounds(R.drawable.settings_ic_password_lock, 0, R.drawable.common_ic_eye_enabled_white, 0);
                            mReTypePasswordReveal = false;
                        }
                    }
                }
                return false;
            }
        });


    }

    private void attachListener() {
        mSignIn.setOnClickListener(this);
        mCreateAccountBtn.setOnClickListener(this);
    }

    private void initViews() {
        mPasswordReveal = false;
        mReTypePasswordReveal = false;
        mEmailField = findViewById(R.id.setup_email_id);
        mNameField = findViewById(R.id.setup_name_id);
        mPasswordField = findViewById(R.id.setup_password_id);
        mReTypePasswordField = findViewById(R.id.setup_retype_password_id);
        mSignIn = findViewById(R.id.setup_sign_in_button_id);
        mCreateAccountBtn = findViewById(R.id.setup_create_account_button_id);
        mProgressBar = findViewById(R.id.create_account_progress_bar);

    }


    /*
     * activates loading animation from child activities
     */
    public void startLoadingAnimation() {
        mProgressBar.setVisibility(View.VISIBLE);
    }

    /*
     * stops loading animation from child activities
     */
    public void stopLoadingAnimation() {
        mProgressBar.setVisibility(View.INVISIBLE);
    }

    public void createAccount(String email, String password, final String name) {
        if (isInputValid()) {
            startLoadingAnimation();
            mCreateAccountBtn.setEnabled(false);


            // todo: send verification email. open a dialog to input the code. if code is okay, the continue
            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                Log.d(TAG, "createUserWithEmail:success");

                                mAuth.getCurrentUser().sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            Toast.makeText(CreateAccountActivity.this, "Registered successfully. Please check \nyour email for verification", Toast.LENGTH_SHORT).show();
                                            startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                                        } else {
                                            Toast.makeText(CreateAccountActivity.this, "Unsuccessful in sending email", Toast.LENGTH_SHORT).show();
                                            Log.d(TAG, task.getException().getMessage());
                                        }
                                    }
                                });

                            /*    FirebaseUser user = mAuth.getCurrentUser(); //commeneted out
                                if (user != null) {
                                    updateUser(name, user); // uploads the user's name in the database by creating a new child node
                                } else {
                                    Log.d(TAG, "Unknown error: user null");
                                }*/
                            } else {
                                checkCause(task.getException());
                            }

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(CreateAccountActivity.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                    mCreateAccountBtn.setEnabled(true);
                    stopLoadingAnimation();

                }
            });


        }


    }

    private void checkCause(Exception exception) {
        // If create account creation fails, display a message to the user.
        Log.w(TAG, "createUserWithEmail:failure", exception);
        if (exception instanceof FirebaseAuthWeakPasswordException) {
            // weak password
            Toast.makeText(CreateAccountActivity.this, "Try with a stronger password!", Toast.LENGTH_SHORT).show();
        } else if (exception instanceof FirebaseAuthInvalidCredentialsException) {
            // invalid email format entered
            Toast.makeText(CreateAccountActivity.this, "Invalid Format!", Toast.LENGTH_SHORT).show();
        } else if (exception instanceof FirebaseAuthUserCollisionException) {
            // email already used
            Toast.makeText(CreateAccountActivity.this, "An account with this email already exists", Toast.LENGTH_SHORT).show();
        } else if (exception instanceof FirebaseNetworkException) {
            Toast.makeText(CreateAccountActivity.this, "Connection Timeout", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(CreateAccountActivity.this, "Error", Toast.LENGTH_SHORT).show();
        }

    }

    private void updateUser(String name, FirebaseUser user) {

        HashMap<String, Object> childUpdate = new HashMap<>();
        childUpdate.put("name", name);
        childUpdate.put("isProfileSetup", false); // setting isProfileSetup to false

        Firebase.getDatabaseReference().child(Firebase.USERS).
                child(user.getUid()).
                child(Firebase.BASIC_INFO).
                updateChildren(childUpdate).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(CreateAccountActivity.this, "Account Created Successfully!", Toast.LENGTH_SHORT).show();
                updateDeviceToken();
                stopLoadingAnimation();
                mCreateAccountBtn.setEnabled(true);

                Intent intent = new Intent(getBaseContext(), SelectImageActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(CreateAccountActivity.this, "Something's Wrong!", Toast.LENGTH_SHORT).show();
                mCreateAccountBtn.setEnabled(true);
                stopLoadingAnimation();

            }
        });


    }

    /*
     * updates the user's current device token in the database
     */
    private void updateDeviceToken() {
        String token = FirebaseInstanceId.getInstance().getToken();

        if (token != null) {
            Log.d(TAG, token);
            Log.d(TAG, "sendRegistrationToServer: sending token to server: " + token);
            DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
            FirebaseUser user = mAuth.getCurrentUser();
            if (user != null) {
                reference.child(Firebase.USERS)
                        .child(user.getUid())
                        .child("device-status")
                        .child(token)
                        .setValue(true);

                // save it in local cache
                SharedPreference preference = SharedPreference.getInstance(getApplicationContext());
                preference.saveData(String.class.getSimpleName(), getString(R.string.device_token), token);
            }
        }
    }

    private boolean isInputValid() {
        boolean valid = true;

        String email = mEmailField.getText().toString();
        if (TextUtils.isEmpty(email)) {
            Common.addStroke(mEmailField, Common.STROKE_WIDTH);
            mEmailField.setError("Required.");
            valid = false;
        } else {
            Common.addStroke(mEmailField, 0);
            mEmailField.setError(null);
        }

        String password = mPasswordField.getText().toString();
        String rePassword = mReTypePasswordField.getText().toString();

        if (password.equals(rePassword)) {
            if (TextUtils.isEmpty(password)) {
                Common.addStroke(mPasswordField, Common.STROKE_WIDTH);
                mPasswordField.setError("Required.");
                valid = false;
            } else {
                Common.addStroke(mPasswordField, 0);
                mPasswordField.setError(null);
            }
        } else {
            Toast.makeText(this, "Password doesn't match!", Toast.LENGTH_SHORT).show();
            valid = false;
        }
        return valid;
    }

    @Override
    public void onClick(View v) {

        if (SystemClock.elapsedRealtime() - mLastClickTime < BUTTON_DELAY_TIME) {
            return;
        }
        mLastClickTime = SystemClock.elapsedRealtime();

        if (v.getId() == mSignIn.getId()) {
            Intent intent = new Intent(CreateAccountActivity.this, LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);

        } else if (v.getId() == mCreateAccountBtn.getId()) {
            createAccount(mEmailField.getText().toString(), mPasswordField.getText().toString(), mNameField.getText().toString());
        }
    }

    public void debugAddValues(View view) {
        mEmailField.setText("aliahnaf327@gmail.com");
        mPasswordField.setText("microlab123");
        mReTypePasswordField.setText("microlab123");
        mNameField.setText("Propo");
    }
}
