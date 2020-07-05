package com.valhalla.studiac.activities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.SystemClock;
import android.text.InputType;
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

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.valhalla.studiac.R;
import com.valhalla.studiac.activities.setup.CreateAccountActivity;
import com.valhalla.studiac.activities.setup.SelectImageActivity;
import com.valhalla.studiac.database.Firebase;
import com.valhalla.studiac.utility.ErrorStyle;
import com.valhalla.studiac.utility.SharedPreference;

import java.util.HashMap;


public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "GoogleActivity";

    // [START firebase_auth]
    private static final int RESULT_GOOGLE_SIGN_IN = 999;
    private FirebaseAuth mAuth;
    private GoogleSignInClient mGoogleSignInClient;
    // [END firebase_auth]

    // Views
    private ProgressBar mProgressBar;
    private TextView mForgotPassword;
    private EditText mEmailField;
    private EditText mPasswordField;
    private TextView mSignUpBtn;
    private Button mLoginButton;
    private FloatingActionButton mLoginWithGoogle;

    final Context context = this;
    private long mLastClickTime = 0;

    private boolean mPasswordReveal;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initViews();
        mForgotPassword.setOnClickListener(this);
        mSignUpBtn.setOnClickListener(this);
        mLoginButton.setOnClickListener(this);
        mLoginWithGoogle.setOnClickListener(this);
        attachTouchListener();
        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        //todo:  set a clickable text view
        // ** https://stackoverflow.com/questions/21992735/how-to-set-multiple-click-event-for-the-single-textview/22006998#22006998
        // https://stackoverflow.com/questions/9969789/clickable-word-inside-textview-in-android

    }

    @SuppressLint("ClickableViewAccessibility")
    private void attachTouchListener() {
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
                        }
                    }
                }
                return false;
            }
        });


    }

    private void initViews() {
        mPasswordReveal = false;
        mForgotPassword = findViewById(R.id.login_forgot_password_button_id);
        mEmailField = findViewById(R.id.setup_email_id);
        mPasswordField = findViewById(R.id.setup_password_id);
        mSignUpBtn = findViewById(R.id.login_sign_up_button_id);
        mLoginButton = findViewById(R.id.login_login_button_id);
        mLoginWithGoogle = findViewById(R.id.login_google_button_id);
        mProgressBar = findViewById(R.id.login_progress_bar);

        ErrorStyle errorStyle = ErrorStyle.getInstance(getApplicationContext());

        errorStyle.resetError(mEmailField);
        errorStyle.resetError(mPasswordField);

    }

    @Override
    public void onClick(View v) {

        int BUTTON_DELAY_TIME = 1000;
        if (SystemClock.elapsedRealtime() - mLastClickTime < BUTTON_DELAY_TIME) {
            return;
        }

        mLastClickTime = SystemClock.elapsedRealtime();
        if (v.getId() == mForgotPassword.getId()) {
            startActivity(new Intent(getBaseContext(), ForgotPasswordActivity.class));
        }


        if (v.getId() == mSignUpBtn.getId()) {
            startActivity(new Intent(getBaseContext(), CreateAccountActivity.class));
        }

        if (v.getId() == mLoginWithGoogle.getId()) {
            signInGoogle();
        }


        if (v.getId() == mLoginButton.getId()) {
            if (isInputValid()) {
                firebaseAuthWithEmail(mEmailField.getText().toString(), mPasswordField.getText().toString());
            }
        }


    }

    private void signInGoogle() {
        mProgressBar.setVisibility(View.VISIBLE);
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RESULT_GOOGLE_SIGN_IN);

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RESULT_GOOGLE_SIGN_IN) {
            mProgressBar.setVisibility(View.INVISIBLE);
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account.getIdToken());
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w(TAG, "Google sign in failed", e);
                // Toast.makeText(context, "Update Your Google Play Services", Toast.LENGTH_SHORT).show();
                updateUserLogin(null);
            }
        }
    }

    // [START auth_with_google]
    private void firebaseAuthWithGoogle(String idToken) {
        mProgressBar.setVisibility(View.VISIBLE);

        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            mProgressBar.setVisibility(View.INVISIBLE);

                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUserLogin(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Snackbar.make((findViewById(R.id.login_root_layout)), "Authentication Failed.", Snackbar.LENGTH_SHORT).show();
                            updateUserLogin(null);
                        }

                        // [START_EXCLUDE]
                        //  hideProgressBar();
                        // [END_EXCLUDE]
                    }
                });
    }
    // [END auth_with_google]

    /*
     * authenticates the given account with firebase authentication with email api
     * if success, then checks if profile setup is complete or not.
     */
    private void firebaseAuthWithEmail(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            FirebaseUser user = mAuth.getCurrentUser();
                            if (user != null) {
                                if (user.isEmailVerified()) {
                                    //Toast.makeText(context, "Sign In Successful!", Toast.LENGTH_SHORT).show();
                                    checkProfileCompletion(user);

                                } else {
                                    Toast toast = Toast.makeText(context,
                                            "Please verify your account from email first",
                                            Toast.LENGTH_SHORT);
                                    TextView v = toast.getView().findViewById(android.R.id.message);
                                    if (v != null) {
                                        v.setGravity(Gravity.CENTER);
                                    }
                                    toast.show();
                                }
                            }

                            // startActivity(new Intent(getBaseContext(), DashboardActivity.class));
                        } else {
                            // If sign in fails, display a message to the user.
                            if (task.getException() instanceof FirebaseAuthInvalidUserException) {
                                Toast.makeText(LoginActivity.this, "No Account Found!",
                                        Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(LoginActivity.this, "Authentication failed.",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
    }

    private void checkProfileCompletion(final FirebaseUser user) {
        Firebase.getDatabaseReference().
                child(Firebase.USERS).
                child(user.getUid()).
                child(Firebase.BASIC_INFO).
                child("isProfileSetup").
                addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            // if isProfileSetup is found, check if it is true or false
                            Boolean isProfileSetup = dataSnapshot.getValue(Boolean.class);
                            if (isProfileSetup != null && isProfileSetup) {
                                Intent intent = new Intent(LoginActivity.this, DashboardActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                updateDeviceToken();
                                startActivity(intent);
                            } else {
                                Toast.makeText(context, "Setup Your Profile", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(LoginActivity.this, SelectImageActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                updateDeviceToken();
                                startActivity(intent);
                            }
                        } else {
                            // set profileSetup to false and start select image activity
                            // set the name of the user from the display name of user object

                            HashMap<String, Object> childUpdates = new HashMap<>();
                            childUpdates.put("/isProfileSetup", false);
                            childUpdates.put("/name", user.getDisplayName());

                            Firebase.getDatabaseReference().
                                    child(Firebase.USERS).
                                    child(user.getUid()).
                                    child(Firebase.BASIC_INFO).updateChildren(childUpdates).
                                    addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Toast.makeText(context, "Setup Your Profile", Toast.LENGTH_SHORT).show();
                                            Intent intent = new Intent(LoginActivity.this, SelectImageActivity.class);
                                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                            updateDeviceToken();
                                            startActivity(intent);
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.d(TAG, e.toString());
                                    Toast.makeText(context, "Something's Wrong", Toast.LENGTH_SHORT).show();
                                }
                            });

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

    }


    private void updateUserLogin(FirebaseUser user) {
        mProgressBar.setVisibility(View.INVISIBLE);
        if (user != null) {
            checkProfileCompletion(user);
        } else {
            // Toast.makeText(context, "Session Expired", Toast.LENGTH_SHORT).show();
            Log.d(TAG, "Session Expired");

        }
    }


    public boolean isInputValid() {
        ErrorStyle errorStyle = ErrorStyle.getInstance(getApplicationContext());
        if (mEmailField.getText().length() == 0) {
            errorStyle.setError(getString(R.string.Error_EmptyField), mEmailField);
            return false;
        } else {
            errorStyle.resetError(mEmailField);
        }

        if (mPasswordField.getText().length() == 0) {
            errorStyle.setError(getString(R.string.Error_EmptyField), mPasswordField);
            return false;
        } else {
            errorStyle.resetError(mPasswordField);
        }

        return true;

    }


    /*
     * updates the user's current device token in the database
     */
    private void updateDeviceToken() {
        String token = FirebaseInstanceId.getInstance().getToken();

        if (token != null) {
            DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
            FirebaseUser user = mAuth.getCurrentUser();
            if (user != null) {
                HashMap<String, Object> childUpdates = new HashMap<>();
                childUpdates.put("status", true);
                childUpdates.put("token", token);

                reference.child(Firebase.USERS)
                        .child(user.getUid())
                        .child("device-status").
                        updateChildren(childUpdates);
                // save it in local cache
                SharedPreference preference = SharedPreference.getInstance(getApplicationContext());
                preference.saveData(String.class.getSimpleName(), getString(R.string.device_token), token);
            }
        }
    }


}