package com.valhalla.studiac.activities;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.telephony.mbms.MbmsErrors;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.Group;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.valhalla.studiac.R;
import com.valhalla.studiac.activities.setup.SelectImageActivity;
import com.valhalla.studiac.database.Firebase;
import com.valhalla.studiac.fragments.dialogs.AddCourseBottomSheetDialog;
import com.valhalla.studiac.fragments.dialogs.SetupAddRoutineDialog;
import com.valhalla.studiac.models.Schedule;
import com.valhalla.studiac.utility.Common;
import com.valhalla.studiac.utility.SharedPreference;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;


public class StartAppActivity extends AppCompatActivity {

    private static final String TAG = "FIREBASE";
    private final int DELAY = 2000;
    private FirebaseAuth mAuth;
    Group logo_text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_startapp);

        SharedPreference preference = SharedPreference.getInstance(getApplicationContext());
        Boolean isNewUser = (Boolean) preference.getData(Boolean.class.getSimpleName(), getString(R.string.is_new_user));

        if (isNewUser) {
            //if new user, then show walk through slides
            Intent intent = new Intent(StartAppActivity.this, WalkThroughActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);

          //  Toast.makeText(this, isNewUser + "", Toast.LENGTH_SHORT).show();
        } else {
            // if user is not new, check if the user is authenticated.
            setContentView(R.layout.activity_startapp);
            // [START] show_animation
            ConstraintLayout root = findViewById(R.id.constraintLayout_id);
            Animation popUpAnimation = AnimationUtils.loadAnimation(this, R.anim.view_scale_up);
            for (int i = 0; i < root.getChildCount(); i++) {
                View childView = root.getChildAt(i);
                childView.startAnimation(popUpAnimation);
            }
            // [STOP] show_animation
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    // execute following lines after 2s
                    mAuth = FirebaseAuth.getInstance();
                    if (mAuth.getCurrentUser() != null) {
                        //reloadUser(); // checks if the user's account is still valid: for debug
                        checkProfileCompletion(mAuth.getCurrentUser());
                    } else {
                        Intent intent = new Intent(StartAppActivity.this, LoginActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    }
                }
            }, DELAY);

        }


    }


    /**
     * check if the user's profile setup is complete.
     * if complete, start dashboard activity, else setup the profile
     *
     * @param user the firebase user instance
     */
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
                                Intent intent = new Intent(StartAppActivity.this, DashboardActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                updateDeviceToken();
                                startActivity(intent);
                            } else {
                                Toast.makeText(getApplicationContext(), "Setup Your Profile", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(StartAppActivity.this, SelectImageActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
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
                                            Toast.makeText(getApplicationContext(), "Setup Your Profile", Toast.LENGTH_SHORT).show();
                                            Intent intent = new Intent(StartAppActivity.this, SelectImageActivity.class);
                                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                            startActivity(intent);
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.d(TAG, e.toString());
                                    Toast.makeText(getApplicationContext(), "Something's Wrong", Toast.LENGTH_SHORT).show();
                                }
                            });

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

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
