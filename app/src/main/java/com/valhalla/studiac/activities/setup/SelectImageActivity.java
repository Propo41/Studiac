package com.valhalla.studiac.activities.setup;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.SystemClock;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.valhalla.studiac.R;
import com.valhalla.studiac.activities.LoginActivity;
import com.valhalla.studiac.database.Firebase;

import java.util.HashMap;


public class SelectImageActivity extends AppCompatActivity implements CompoundButton.OnClickListener {
    private int mSelectedImageId; // holds the currently selected image view id
    private int mSelectedImageToggleOffImageId; // holds the currently selected image's off-state background id
    private HashMap<Integer, Integer> mToggleOnStateImages; // holds: viewID -> toggledStateImage of that view
    private String mSelectedImageResName; // holds the resource name. This is the one saved to the db
    private String TAG = "GoogleActivity";
    private FirebaseUser mUser;
    private String mParent;
    private final int BUTTON_DELAY_TIME = 1000;
    private long mLastClickTime = 0;
    private HashMap<Integer, Integer> mImages; // contains: toggleOffImage -> toggleOnImage
    private ProgressBar mProgressBar;
    private boolean dialogState;
    private static final int TIMEOUT = 20000;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setupprofile_selectimage);
        // Initialize Firebase Auth
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        if (isUserAuthenticated(mUser)) {
            // get intent from parent activity: EditProfileActivity
            Intent intent = getIntent();
            Bundle bundle = intent.getExtras();
            if (bundle != null) {
                mSelectedImageResName = bundle.getString("selectedImage");
                mParent = "EditProfile";
            }
            initViews();
            onNextClick();
        }

    }

    private boolean isUserAuthenticated(FirebaseUser user) {
        if (user == null) {
            Toast.makeText(this, "Session Expired. Log in again!", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, LoginActivity.class);
            // adding flags to intent so that in the new activity, pressing back button doesn't bring this activity back
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            return false;
        }
        return true;

    }

    /*
     * activates loading animation from child activities
     */
    public void startLoadingAnimation() {
        mProgressBar.setVisibility(View.VISIBLE);
        startTimer();

    }

    private void startTimer() {

        // if after 20sec the loading is not completed, stop it and show toast
        new CountDownTimer(TIMEOUT, 1000) {
            public void onTick(long millisUntilFinished) {
            }

            public void onFinish() {
                if (dialogState) {
                    stopLoadingAnimation();
                    Toast toast = Toast.makeText(SelectImageActivity.this, "Connection timeout.", Toast.LENGTH_SHORT);
                    TextView v = toast.getView().findViewById(android.R.id.message);
                    if (v != null) {
                        v.setGravity(Gravity.CENTER);
                    }
                    toast.show();
                }

            }
        }.start();
    }

    /*
     * stops loading animation from child activities
     */
    public void stopLoadingAnimation() {
        mProgressBar.setVisibility(View.INVISIBLE);
        dialogState = false;
    }

    /*
     * initialized views and attaches listener to the toggle buttons
     */
    private void initViews() {
        mProgressBar = findViewById(R.id.select_img_progress_bar);

        mToggleOnStateImages = new HashMap<>();
        mToggleOnStateImages.put(R.id.profile_picture_one_id, R.drawable.profile_picture_one_selected);
        mToggleOnStateImages.put(R.id.profile_picture_two_id, R.drawable.profile_picture_two_selected);
        mToggleOnStateImages.put(R.id.profile_picture_three_id, R.drawable.profile_picture_three_selected);
        mToggleOnStateImages.put(R.id.profile_picture_four_id, R.drawable.profile_picture_four_selected);
        mToggleOnStateImages.put(R.id.profile_picture_five_id, R.drawable.profile_picture_five_selected);
        mToggleOnStateImages.put(R.id.profile_picture_six_id, R.drawable.profile_picture_six_selected);
        mToggleOnStateImages.put(R.id.profile_picture_seven_id, R.drawable.profile_picture_seven_selected);
        mToggleOnStateImages.put(R.id.profile_picture_eight_id, R.drawable.profile_picture_eight_selected);
        mToggleOnStateImages.put(R.id.profile_picture_nine_id, R.drawable.profile_picture_nine_selected);

        ToggleButton profilePictureOne = findViewById(R.id.profile_picture_one_id);
        profilePictureOne.setTag(R.drawable.profile_picture_one);

        ToggleButton profilePictureTwo = findViewById(R.id.profile_picture_two_id);
        profilePictureTwo.setTag(R.drawable.profile_picture_two);

        ToggleButton profilePictureThree = findViewById(R.id.profile_picture_three_id);
        profilePictureThree.setTag(R.drawable.profile_picture_three);

        ToggleButton profilePictureFour = findViewById(R.id.profile_picture_four_id);
        profilePictureFour.setTag(R.drawable.profile_picture_four);

        ToggleButton profilePictureFive = findViewById(R.id.profile_picture_five_id);
        profilePictureFive.setTag(R.drawable.profile_picture_five);


        ToggleButton profilePictureSix = findViewById(R.id.profile_picture_six_id);
        profilePictureSix.setTag(R.drawable.profile_picture_six);

        ToggleButton profilePictureSeven = findViewById(R.id.profile_picture_seven_id);
        profilePictureSeven.setTag(R.drawable.profile_picture_seven);

        ToggleButton profilePictureEight = findViewById(R.id.profile_picture_eight_id);
        profilePictureEight.setTag(R.drawable.profile_picture_eight);

        ToggleButton profilePictureNine = findViewById(R.id.profile_picture_nine_id);
        profilePictureNine.setTag(R.drawable.profile_picture_nine);


        profilePictureOne.setOnClickListener(this);
        profilePictureTwo.setOnClickListener(this);
        profilePictureThree.setOnClickListener(this);
        profilePictureFour.setOnClickListener(this);
        profilePictureFive.setOnClickListener(this);
        profilePictureSix.setOnClickListener(this);
        profilePictureSeven.setOnClickListener(this);
        profilePictureEight.setOnClickListener(this);
        profilePictureNine.setOnClickListener(this);

        mImages = new HashMap<>();
        mImages.put(R.drawable.profile_picture_one, R.drawable.profile_picture_one_selected);
        mImages.put(R.drawable.profile_picture_two, R.drawable.profile_picture_two_selected);
        mImages.put(R.drawable.profile_picture_three, R.drawable.profile_picture_three_selected);
        mImages.put(R.drawable.profile_picture_four, R.drawable.profile_picture_four_selected);
        mImages.put(R.drawable.profile_picture_five, R.drawable.profile_picture_five_selected);
        mImages.put(R.drawable.profile_picture_six, R.drawable.profile_picture_six_selected);
        mImages.put(R.drawable.profile_picture_seven, R.drawable.profile_picture_seven_selected);
        mImages.put(R.drawable.profile_picture_eight, R.drawable.profile_picture_eight_selected);
        mImages.put(R.drawable.profile_picture_nine, R.drawable.profile_picture_nine_selected);


        // setting the default selected imaged
        mSelectedImageId = R.id.profile_picture_three_id;
        mSelectedImageToggleOffImageId = R.drawable.profile_picture_three;
        profilePictureThree.setBackgroundResource(R.drawable.profile_picture_three_selected);
        mSelectedImageResName = setSelectedImageRes(mImages.get(mSelectedImageToggleOffImageId));


    }

    private String setSelectedImageRes(int imageDrawable) {
        Resources resources = getResources();
        return resources.getResourceEntryName(imageDrawable);
    }

    private void onNextClick() {
        final Button nextButton = findViewById(R.id.changeimg_button_id);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (SystemClock.elapsedRealtime() - mLastClickTime < BUTTON_DELAY_TIME) {
                    return;
                }
                mLastClickTime = SystemClock.elapsedRealtime();

                nextButton.setEnabled(false);
                startLoadingAnimation();
                // if activity is opened from EditProfile Activity
                // then pass the selected image the parent activity
                if (mParent != null) {
                    Intent data = new Intent();
                    data.putExtra("selectedImage", mSelectedImageResName);
                    setResult(RESULT_OK, data);
                    finish();
                } else {
                    // uploading the user's image res name to database
                    Firebase.getDatabaseReference().
                            child(Firebase.USERS).
                            child(mUser.getUid()).
                            child(Firebase.BASIC_INFO).
                            child("imageResName").setValue(mSelectedImageResName).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                          //  Toast.makeText(SelectImageActivity.this, "Looking Nice!", Toast.LENGTH_SHORT).show();
                            nextButton.setEnabled(true);
                            stopLoadingAnimation();
                            startActivity(new Intent(getBaseContext(), ProfileSetupActivity.class));
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            e.printStackTrace();
                            Toast.makeText(SelectImageActivity.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                            nextButton.setEnabled(true);
                            stopLoadingAnimation();
                        }
                    });

                }

            }
        });
    }

    /*
     * on click listener for selecting the images
     */
    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id != mSelectedImageId) {
            // if the selected image is not toggled, then remove previously
            // toggled image and toggle the new one and mark it
            View view = findViewById(mSelectedImageId);
            view.setBackgroundResource(mSelectedImageToggleOffImageId);
            Integer toggledId = mToggleOnStateImages.get(id);
            if (toggledId != null) {
                v.setBackgroundResource(toggledId); // set the background to the corresponding image
            }
            mSelectedImageId = v.getId(); // set the currently selected image that is toggled
            mSelectedImageToggleOffImageId = (Integer) v.getTag();
            mSelectedImageResName = setSelectedImageRes(mImages.get(mSelectedImageToggleOffImageId));

        }

    }


}
