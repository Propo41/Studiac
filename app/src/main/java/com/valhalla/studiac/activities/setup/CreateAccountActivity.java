package com.valhalla.studiac.activities.setup;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.SystemClock;
import android.text.InputType;
import android.text.TextUtils;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseNetworkException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.valhalla.studiac.R;
import com.valhalla.studiac.activities.LoginActivity;
import com.valhalla.studiac.database.Firebase;
import com.valhalla.studiac.utility.ErrorStyle;
import com.valhalla.studiac.utility.SharedPreference;

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
    private static final int BUTTON_DELAY_TIME = 1000;
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


    @SuppressLint("ClickableViewAccessibility")
    private void attachTouchListeners() {
        mPasswordField.setOnTouchListener(new View.OnTouchListener() {
            @SuppressLint("ClickableViewAccessibility")
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

                            mPasswordField.setCompoundDrawablesWithIntrinsicBounds(R.drawable.settings_ic_password_lock, 0, R.drawable.ic_eye_invisible, 0);
                            mPasswordReveal = true;

                            Typeface typeface = ResourcesCompat.getFont(getApplicationContext(), R.font.montserrat_regular);
                            mPasswordField.setTypeface(typeface);


                        } else {
                            //to hide

                            mPasswordField.setTransformationMethod(PasswordTransformationMethod.getInstance());
                            mPasswordField.setCompoundDrawablesWithIntrinsicBounds(R.drawable.settings_ic_password_lock, 0, R.drawable.ic_eye_visible, 0);
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

                            mReTypePasswordField.setCompoundDrawablesWithIntrinsicBounds(R.drawable.settings_ic_password_lock, 0, R.drawable.ic_eye_invisible, 0);
                            mReTypePasswordReveal = true;
                        } else {
                            //to hide
                            Typeface typeface = ResourcesCompat.getFont(getApplicationContext(), R.font.montserrat_regular);
                            mReTypePasswordField.setTypeface(typeface);
                            mReTypePasswordField.setTransformationMethod(PasswordTransformationMethod.getInstance());
                            mReTypePasswordField.setCompoundDrawablesWithIntrinsicBounds(R.drawable.settings_ic_password_lock, 0, R.drawable.ic_eye_visible, 0);
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

            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                final FirebaseUser user = mAuth.getCurrentUser();

                                // send email verification to the user
                                if (user != null) {
                                    UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                            .setDisplayName(name).build();
                                    // set the name of the user in the user object
                                    user.updateProfile(profileUpdates)
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()) {
                                                        Log.d(TAG, "User profile updated.");
                                                    }
                                                    user.sendEmailVerification().
                                                            addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                @Override
                                                                public void onComplete(@NonNull Task<Void> task) {
                                                                    if (task.isSuccessful()) {
                                                                        createToast("Registered successfully. Please check your email for verification");
                                                                        startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                                                                    } else {
                                                                        Toast.makeText(CreateAccountActivity.this, "Something's wrong.", Toast.LENGTH_SHORT).show();
                                                                    }
                                                                }
                                                            });
                                                }
                                            });
                                }


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


    private void createToast(String message) {
        Toast toast = Toast.makeText(CreateAccountActivity.this, message, Toast.LENGTH_SHORT);
        TextView v = toast.getView().findViewById(android.R.id.message);
        if (v != null) {
            v.setGravity(Gravity.CENTER);
        }
        toast.show();
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
            createToast("An account with this email already exists");
        } else if (exception instanceof FirebaseNetworkException) {
            Toast.makeText(CreateAccountActivity.this, "Connection Timeout", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(CreateAccountActivity.this, "Error", Toast.LENGTH_SHORT).show();
        }

    }


    private boolean isInputValid() {
        String name = mNameField.getText().toString();
        ErrorStyle errorStyle = ErrorStyle.getInstance(getApplicationContext());

        if (TextUtils.isEmpty(name)) {
            errorStyle.setError(getString(R.string.Error_EmptyField), mNameField);
            return false;
        } else {
            errorStyle.resetError(mNameField);
        }

        String email = mEmailField.getText().toString();
        if (TextUtils.isEmpty(email)) {
            errorStyle.setError(getString(R.string.Error_EmptyField), mEmailField);
            return false;
        } else {
            errorStyle.resetError(mEmailField);

        }

        String password = mPasswordField.getText().toString();
        String rePassword = mReTypePasswordField.getText().toString();

        if (password.equals(rePassword)) {
            if (TextUtils.isEmpty(password)) {
                errorStyle.setError(getString(R.string.Error_EmptyField), mPasswordField);
                return false;
            } else {
                errorStyle.resetError(mPasswordField);
            }
        } else {
            errorStyle.setError(getString(R.string.Error_Password_Mismatch), mPasswordField);
            return false;
        }
        return true;
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

}
