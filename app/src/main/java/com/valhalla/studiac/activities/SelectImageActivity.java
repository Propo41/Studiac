package com.valhalla.studiac.activities;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.appcompat.app.AppCompatActivity;

import com.valhalla.studiac.R;
import com.valhalla.studiac.utility.Common;

import java.util.HashMap;


public class SelectImageActivity extends AppCompatActivity implements CompoundButton.OnClickListener {
    private int mSelectedImageId; // holds the currently selected image view id
    private int mSelectedImageToggleOffImageId; // holds the currently selected image's off-state background id
    private HashMap<Integer, Integer> mToggleOnStateImages; // holds: viewID -> toggledStateImage of that view
    private String mSelectedImageResName; // holds the resource name. This is the one saved to the db


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setupprofile_selectimage);
        initViews();
        onNextClick();
    }

    private void onNextClick() {
        Button nextButton = findViewById(R.id.changeimg_button_id);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // todo: upload "mSelectedImageResName" to database
                Toast.makeText(SelectImageActivity.this, mSelectedImageResName, Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getBaseContext(), ProfileSetupActivity.class));
            }
        });
    }


    /*
     * initialized views and attaches listener to the toggle buttons
     */
    private void initViews() {
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

        // setting the default selected imaged
        mSelectedImageId = R.id.profile_picture_three_id;
        mSelectedImageToggleOffImageId = R.drawable.profile_picture_three;
        profilePictureThree.setBackgroundResource(R.drawable.profile_picture_three_selected);
        mSelectedImageResName = setSelectedImageRes(mSelectedImageToggleOffImageId);

    }

    private String setSelectedImageRes(int imageDrawable) {
        Resources resources = getResources();
        return resources.getResourceEntryName(imageDrawable);
    }


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
            mSelectedImageResName = setSelectedImageRes(mSelectedImageToggleOffImageId);

        }

    }


}
