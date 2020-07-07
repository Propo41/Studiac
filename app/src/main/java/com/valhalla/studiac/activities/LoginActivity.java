package com.valhalla.studiac.activities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.SystemClock;
import android.text.InputType;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.method.PasswordTransformationMethod;
import android.text.style.ClickableSpan;
import android.text.style.StyleSpan;
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
import com.valhalla.studiac.activities.settings.ForgotPasswordActivity;
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

    private TextView mLoginEula;
    private boolean mPasswordReveal;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initViews();
        createEula();
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

        //  populateDataForSemester();


    }
/*
    // for AUST, CSE, 2.2, Sec A1
    private void populateDataForSemester() {
        Routine routine;
        ArrayList<Routine> routines = new ArrayList<>();

        routine = new Routine(Common.GET_DAY_FROM_INDEX[0]); // sun
        routine.getItems().add(new Schedule("CSE 2208", "01:00pm", "03:30pm", "Practical", "Algorithms Lab"));
        routine.getItems().add(new Schedule("CSE 2201", "03:30pm", "04:20pm", "Lecture", "Numerical Methods"));
        routine.getItems().add(new Schedule("MATH 2203", "04:20pm", "05:10pm", "Lecture", "Mathematics IV"));
        routine.getItems().add(new Schedule("CSE 2209", "05:10pm", "06:00pm", "Lecture","Digital Electronics and Pulse Techniques"));
        routines.add(routine);

        routine = new Routine(Common.GET_DAY_FROM_INDEX[1]); // mon
        routine.getItems().add(new Schedule("CSE 2201", "08:00am", "08:50am", "Lecture", "Numerical Methods"));
        routine.getItems().add(new Schedule("CSE 2207", "08:50am", "09:40am", "Lecture", "Algorithms"));
        routine.getItems().add(new Schedule("CSE 2213", "09:40am", "10:30am", "Lecture","Computer Architecture"));
        routines.add(routine);

        routine = new Routine(Common.GET_DAY_FROM_INDEX[2]); // tues
        routine.getItems().add(new Schedule("CSE 2200", "08:00am", "10:30am", "Practical", "Software Development-III"));
        routine.getItems().add(new Schedule("CSE 2209", "10:30am", "11:20am", "Lecture","Digital Electronics and Pulse Techniques"));
        routine.getItems().add(new Schedule("CSE 2207", "11:20am", "12:10pm", "Lecture", "Algorithms"));
        routine.getItems().add(new Schedule("MATH 2203", "12:10pm", "01:00pm", "Lecture", "Mathematics IV"));
        routines.add(routine);

        routine = new Routine(Common.GET_DAY_FROM_INDEX[3]); // wed
        routine.getItems().add(new Schedule("CSE 2214", "10:30am", "01:00pm", "Practical", "Assembly Language Programing"));

        routine.getItems().add(new Schedule("CSE 2213", "01:00pm", "01:50pm", "Lecture", "Computer Architecture"));
        routine.getItems().add(new Schedule("CSE 2209", "01:50pm", "02:40pm", "Lecture","Digital Electronics and Pulse Techniques"));
        routine.getItems().add(new Schedule("MATH 2203", "02:40pm", "03:30pm", "Lecture", "Mathematics IV"));
        routines.add(routine);

        routine = new Routine(Common.GET_DAY_FROM_INDEX[4]); // thurs
        routine.getItems().add(new Schedule("CSE 2201", "08:00am", "08:50am", "Lecture", "Numerical Methods"));
        routine.getItems().add(new Schedule("CSE 2213", "08:50am", "09:40am", "Lecture", "Computer Architecture"));
        routine.getItems().add(new Schedule("CSE 2207", "09:40am", "10:30am", "Lecture", "Algorithms"));
        routine.getItems().add(new Schedule("CSE 2210", "10:30am", "01:00pm", "Practical", "Digital Electronics and Pulse Techniques Lab"));
        routines.add(routine);

        routine = new Routine(Common.GET_DAY_FROM_INDEX[5]); // fri
        routines.add(routine);

        routine = new Routine(Common.GET_DAY_FROM_INDEX[6]); // sat
        routines.add(routine);

        ArrayList<Course> courses = new ArrayList<>();

        ArrayList<String> str = new ArrayList<>();
        str.add("Sunday");
        str.add("Tuesday");
        str.add("Wednesday");
        courses.add(new Course("Mathematics IV", "MATH 2203", 3.0, str, "Lecture"));

        str = new ArrayList<>();
        str.add("Tuesday");
        courses.add(new Course("Software Development-III", "CSE 2200", 0.75, str, "Practical"));

        str = new ArrayList<>();
        str.add("Thursday");
        str.add("Sunday");
        str.add("Monday");
        courses.add(new Course("Numerical Methods", "CSE 2201", 3.0, str, "Lecture"));

        str = new ArrayList<>();
        str.add("Monday");
        str.add("Tuesday");
        str.add("Thursday");
        courses.add(new Course("Algorithms", "CSE 2207", 3.0, str, "Lecture"));

        str = new ArrayList<>();
        str.add("Sunday");
        courses.add(new Course("Algorithms Lab", "CSE 2208", 1.5, str, "Practical"));

        str = new ArrayList<>();
        str.add("Thursday");
        courses.add(new Course("Digital Electronics and Pulse Techniques Lab", "CSE 2210", 0.75, str, "Practical"));

        str = new ArrayList<>();
        str.add("Sunday");
        str.add("Tuesday");
        str.add("Wednesday");
        courses.add(new Course("Digital Electronics and Pulse Techniques", "CSE 2209", 3.0, str, "Lecture"));

        str = new ArrayList<>();
        str.add("Monday");
        str.add("Wednesday");
        str.add("Thursday");
        courses.add(new Course("Computer Architecture", "CSE 2213", 3.0, str, "Lecture"));

        str = new ArrayList<>();
        str.add("Tuesday");
        courses.add(new Course("Assembly Language Programing", "CSE 2214", 1.5, str, "Practical"));

        HashMap<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("courses", courses);
        childUpdates.put("routine", routines);

        Firebase.getDatabaseReference().
                child("universities").
                child("Ahsanullah University of Science & Technology").
                child("semester-info").
                child("4").
                child("Computer Science and Engineering").
                child("A").
                child("A1").
                setValue(childUpdates).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(getApplicationContext(), "Data uploaded!", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(), "Data not uploaded!", Toast.LENGTH_SHORT).show();

            }
        });
    }*/

    private void createEula() {
        String text = getString(R.string.eula);
        createSpannableString(text, mLoginEula);
    }

    /**
     * creates a string with 2 bold words that are clickable
     * upon clicking them, 2 activities are opened
     *
     * @param message  the string that will be displayed
     * @param textView the view
     */
    private void createSpannableString(String message, TextView textView) {
        SpannableString ss = new SpannableString(message);
        StyleSpan boldSpan = new StyleSpan(Typeface.BOLD);
        StyleSpan boldSpan2 = new StyleSpan(Typeface.BOLD);
        ss.setSpan(boldSpan, 36, 48, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        ss.setSpan(boldSpan2, 53, 67, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        ClickableSpan termsClick = new ClickableSpan() {
            @Override
            public void onClick(@NonNull View textView) {
                Intent intent = new Intent(LoginActivity.this, WebActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("url", "terms");
                intent.putExtras(bundle);
                startActivity(intent);
            }
        };

        ClickableSpan policyClick = new ClickableSpan() {
            @Override
            public void onClick(@NonNull View textView) {
                Intent intent = new Intent(LoginActivity.this, WebActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("url", "policy");
                intent.putExtras(bundle);
                startActivity(intent);
            }
        };

        ss.setSpan(termsClick, 36, 48, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        ss.setSpan(policyClick, 53, 67, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        textView.setMovementMethod(LinkMovementMethod.getInstance());
        textView.setHighlightColor(Color.TRANSPARENT); // prevents the background to change when selected
        textView.setText(ss, TextView.BufferType.SPANNABLE);
        textView.setLinkTextColor(ResourcesCompat.getColor(getResources(), R.color.colorWhite, null));
        textView.setSelected(true);

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
        mLoginEula = findViewById(R.id.login_eula);

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
                mProgressBar.setVisibility(View.VISIBLE);
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
                                    mProgressBar.setVisibility(View.INVISIBLE);
                                    Toast toast = Toast.makeText(context,
                                            "Please verify your account from email first",
                                            Toast.LENGTH_SHORT);
                                    View view = toast.getView();
                                    if (view != null) {
                                        TextView v = view.findViewById(android.R.id.message);
                                        if (v != null) {
                                            v.setGravity(Gravity.CENTER);
                                        }
                                        toast.show();
                                    }

                                }
                            }

                            // startActivity(new Intent(getBaseContext(), DashboardActivity.class));
                        } else {
                            // If sign in fails, display a message to the user.
                            if (task.getException() instanceof FirebaseAuthInvalidUserException) {
                                mProgressBar.setVisibility(View.INVISIBLE);
                                Toast.makeText(LoginActivity.this, "No Account Found!",
                                        Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(LoginActivity.this, "Authentication failed.",
                                        Toast.LENGTH_SHORT).show();
                                mProgressBar.setVisibility(View.INVISIBLE);

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
                            mProgressBar.setVisibility(View.INVISIBLE);
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
                                            mProgressBar.setVisibility(View.INVISIBLE);

                                            startActivity(intent);
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.d(TAG, e.toString());
                                    mProgressBar.setVisibility(View.INVISIBLE);

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
