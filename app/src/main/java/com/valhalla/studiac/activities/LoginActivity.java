package com.valhalla.studiac.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.SystemClock;
import android.text.InputType;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

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

    }

    private void attachTouchListener() {

        mPasswordField.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                //       Toast.makeText(ShowHidePasswordEditText.this, motionEvent.getX()+"  " +motionEvent.getY(), Toast.LENGTH_SHORT).show();
                //Toast.makeText(ShowHidePasswordEditText.this, motionEvent.getSize()+"", Toast.LENGTH_SHORT).show();
                if(MotionEvent.ACTION_DOWN == motionEvent.getAction()) {
                    if (motionEvent.getX() > mPasswordField.getRight() - 250) {
                        if (!mPasswordReveal) {
                            mPasswordField.setInputType(   //to unhide
                                    InputType.TYPE_CLASS_TEXT |
                                            InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
                            );
                            mPasswordField.setCompoundDrawablesWithIntrinsicBounds(R.drawable.settings_ic_password_lock, 0, R.drawable.common_ic_eye_disabled_white, 0);
                            mPasswordReveal=true;

                            Typeface typeface = ResourcesCompat.getFont(getApplicationContext(), R.font.montserrat_regular);
                            mPasswordField.setTypeface(typeface);


                        } else if(mPasswordReveal){
                            //to hide
                            mPasswordField.setTransformationMethod(PasswordTransformationMethod.getInstance());
                            mPasswordField.setCompoundDrawablesWithIntrinsicBounds(R.drawable.settings_ic_password_lock, 0, R.drawable.common_ic_eye_enabled_white, 0);
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

    /*
     * Manually refreshes the data of the current user (for example, attached providers, display name, and so on).
     * checks if a user's account is valid or not, ie if it has been deleted or undergone a major account update
     * from another device or from the cloud console
     */
    private void reloadUser() {
        // TODO: reload takes long time. only use it for debug. In actual app, dont use it
        mAuth.getCurrentUser().reload().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    updateUserLogin(mAuth.getCurrentUser());
                } else {
                    if (task.getException() instanceof FirebaseAuthInvalidUserException) {
                        Toast.makeText(LoginActivity.this, "No Account Found", Toast.LENGTH_SHORT).show();
                    }
                    Log.e(TAG, "reload", task.getException());
                }
            }
        });
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
                Log.d(TAG, "Google sign in success, authId:" + account.getId());
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
                            assert user != null;
                            Firebase.getDatabaseReference().
                                    child(Firebase.USERS).
                                    child(user.getUid()).
                                    child(Firebase.BASIC_INFO).
                                    child("name").
                                    setValue(user.getDisplayName());
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
                            if(FirebaseAuth.getInstance().getCurrentUser().isEmailVerified()) {


                                Log.d(TAG, "signInWithEmail:success");
                                Toast.makeText(context, "Sign In Successful!", Toast.LENGTH_SHORT).show();
                                if (mAuth.getCurrentUser() != null) {
                                    checkProfileCompletion(mAuth.getCurrentUser());
                                }



                            }else{

                                Toast.makeText(context, "Please verify your account from email first", Toast.LENGTH_SHORT).show();

                            }
                            // startActivity(new Intent(getBaseContext(), DashboardActivity.class));
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.d(TAG, "signInWithEmail:failure", task.getException());
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
                            Firebase.getDatabaseReference().
                                    child(Firebase.USERS).
                                    child(user.getUid()).
                                    child(Firebase.BASIC_INFO).
                                    child("isProfileSetup").
                                    setValue(false).
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
        boolean mReturnValue = true;
        if (mEmailField.getText().length() == 0) {
            mEmailField.setError("This field cannot be empty");
            mReturnValue = false;
        }

        if (mPasswordField.getText().length() == 0 && mReturnValue) {
            mPasswordField.setError("This field cannot be empty");
            mReturnValue = false;
        }


        return mReturnValue;

    }


    /*
     * updates the user's current device token in the database
     */
    private void updateDeviceToken() {
        String token = FirebaseInstanceId.getInstance().getToken();

        if (token != null) {
            Log.d(TAG, "sendRegistrationToServer: sending token to server: " + token);
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
