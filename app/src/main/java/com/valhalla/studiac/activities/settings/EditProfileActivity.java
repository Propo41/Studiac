package com.valhalla.studiac.activities.settings;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthRecentLoginRequiredException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.valhalla.studiac.R;
import com.valhalla.studiac.activities.setup.SelectImageActivity;
import com.valhalla.studiac.database.Firebase;
import com.valhalla.studiac.models.Student;
import com.valhalla.studiac.toolbars.NavigationToolbarWhite;
import com.valhalla.studiac.utility.Common;
import com.valhalla.studiac.utility.ErrorStyle;
import com.valhalla.studiac.utility.SharedPreference;

import java.util.HashMap;

public class EditProfileActivity extends NavigationToolbarWhite implements View.OnClickListener {

    private ImageView mImage;
    private EditText mName;
    private EditText mEmail;
    private FirebaseUser mUser;
    private String mSelectedImage;
    private Button mUpdateChangesButton;
    private FloatingActionButton mEditImageButton;
    private final int RESULT_SELECTED_IMAGE = 1;
    private Student mStudent;
    private boolean updateSuccess = true;
    private final String TAG = "EditProfileActivity";
    private ErrorStyle errorStyle;
    private Boolean mConnectionStatus = false;
    private ValueEventListener mNetworkListener;
    private ConstraintLayout mRootView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContent(R.layout.activity_edit_profile);

        mUser = FirebaseAuth.getInstance().getCurrentUser();
        if (super.isUserAuthenticated(mUser)) {
            // get the basicInfo from user's id from firebase
            Firebase.getDatabaseReference().
                    child(Firebase.USERS).
                    child(mUser.getUid()).
                    child(Firebase.BASIC_INFO).
                    addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            mStudent = dataSnapshot.getValue(Student.class);
                            if (mStudent != null) {
                                initViews();
                                loadContent();
                                mUpdateChangesButton.setOnClickListener(EditProfileActivity.this);
                                mEditImageButton.setOnClickListener(EditProfileActivity.this);
                            } else {
                                Log.d(TAG, "student instance imported is null");
                            }

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            Log.d("FIREBASE", databaseError.getDetails());
                            Toast.makeText(EditProfileActivity.this, "Couldn't retrieve data", Toast.LENGTH_SHORT).show();
                        }
                    });
        }


    }


    /**
     * checks when the user is active or disconnected.
     * firebase automatically removes the listener and disconnects after inactivity of 60 seconds
     */
    private void checkNetworkStatus() {
        DatabaseReference connectedRef = FirebaseDatabase.getInstance().getReference(".info/connected");
        mNetworkListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                mConnectionStatus = snapshot.getValue(Boolean.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.i("TAG: ", "Listener was cancelled");
            }
        };

        connectedRef.addValueEventListener(mNetworkListener);

    }

    private void loadContent() {
        mName.setText(mStudent.getName());
        mEmail.setText(mUser.getEmail());
        mSelectedImage = mStudent.getImageResName(); // the currently selected image
        int imgId = getImageDrawableId(mStudent.getImageResName(), getApplicationContext());
        mImage.setImageResource(imgId); // setting the selected image
    }

    private void initViews() {

        errorStyle = ErrorStyle.getInstance(getApplicationContext());

        mRootView = findViewById(R.id.edit_profile_root);
        mImage = findViewById(R.id.edit_profile_image_id);
        mName = findViewById(R.id.edit_profile_name_id);
        mEmail = findViewById(R.id.edit_profile_email_id);
        //  mRoll = findViewById(R.id.edit_profile_roll_id);
        mEditImageButton = findViewById(R.id.edit_profile_edit_image_button_id);
        mUpdateChangesButton = findViewById(R.id.edit_profile_update_button_id);


        errorStyle.resetError(mName);
        errorStyle.resetError(mEmail);

    }

    @Override
    protected void onPause() {
        super.onPause();
        // detach network checker listener
        FirebaseDatabase.getInstance().getReference(".info/connected").removeEventListener(mNetworkListener);
    }

    @Override
    protected void onResume() {
        super.onResume();
        // attach network checker listener
        checkNetworkStatus();
        //Toast.makeText(this, "status: " + mConnectionStatus, Toast.LENGTH_SHORT).show();

    }

    /*
     * result obtained after selecting the image from the SelectedImageActivity
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RESULT_SELECTED_IMAGE && resultCode == RESULT_OK) {
            if (data != null) {
                mSelectedImage = data.getStringExtra("selectedImage");
                int imgId = getImageDrawableId(mSelectedImage, getApplicationContext());
                mImage.setImageResource(imgId); // setting the selected image
            } else {
                throw new NullPointerException("result not passed");
            }
        }
    }

    private boolean isInputValid() {
        String email = mEmail.getText().toString();
        String name = mName.getText().toString();
        if (email.isEmpty()) {
            errorStyle.setError(getString(R.string.Error_EmptyField), mEmail);
            return false;

        } else {
            errorStyle.resetError(mEmail);
        }

        if (name.isEmpty()) {
            errorStyle.setError(getString(R.string.Error_EmptyField), mName);
            return false;
        } else {
            errorStyle.resetError(mName);
        }

        if (name.length() < 5) {
            errorStyle.setError(getString(R.string.Error_ShortName), mName);
            return false;
        } else {
            errorStyle.resetError(mName);
        }

        return true;
    }

    private int getImageDrawableId(String resName, Context context) {
        Resources resources = context.getResources();
        return resources.getIdentifier(resName, "drawable", Common.PACKAGE_NAME);
    }


    /**
     * @param v the view that is clicked
     * @events: Edit Image button
     * @events: Update Changes button
     */
    @Override
    public void onClick(View v) {
        final SharedPreference preference = SharedPreference.getInstance(getApplicationContext());
        if (v.getId() == mEditImageButton.getId()) {
            // open select image activity
            Intent intent = new Intent(EditProfileActivity.this, SelectImageActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString("selectedImage", mSelectedImage);
            intent.putExtras(bundle);
            startActivityForResult(intent, RESULT_SELECTED_IMAGE);
        } else if (v.getId() == mUpdateChangesButton.getId()) {
            if (mConnectionStatus) {
                // if input valid then make changes and continue else don't
                if (isInputValid()) {
                    // upload to firebase
                    HashMap<String, Object> childUpdates = new HashMap<>();
                    if (!mName.getText().toString().equals(mStudent.getName())) {
                        childUpdates.put("name", mName.getText().toString());
                    }
                    if (!mSelectedImage.equals(mStudent.getImageResName())) {
                        childUpdates.put("imageResName", mSelectedImage);
                    }

                    // if changes are made, then update data in database
                    if (childUpdates.size() != 0) {
                        Firebase.getDatabaseReference().
                                child(Firebase.USERS).
                                child(mUser.getUid()).
                                child(Firebase.BASIC_INFO).
                                updateChildren(childUpdates).
                                addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        // save profileChanges to true in local storage
                                        preference.saveData(Boolean.class.getSimpleName(), getString(R.string.profile_changes), true);

                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                updateSuccess = false;
                                Toast.makeText(EditProfileActivity.this, "Changes couldn't be saved", Toast.LENGTH_SHORT).show();
                            }
                        });
                        // Toast.makeText(EditProfileActivity.this, "Changes Saved", Toast.LENGTH_SHORT).show();

                    }

                    // if new email is entered, update it
                    if (!mEmail.getText().toString().equals(mUser.getEmail())) {

                        mUser.updateEmail(mEmail.getText().toString()).
                                addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        mEmail.setError(null);
                                        Common.addStroke(mEmail, 0);
                                        // save profileChanges to true in local storage
                                        preference.saveData(Boolean.class.getSimpleName(), getString(R.string.profile_changes), true);

                                    }
                                }).
                                addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        updateSuccess = false;
                                        if (e instanceof FirebaseAuthRecentLoginRequiredException) {
                                            //re-authenticate user
                                            startActivity(new Intent(EditProfileActivity.this, VerifyAccountActivity.class));
                                        } else {
                                            mEmail.setError(e.getLocalizedMessage());
                                            Common.addStroke(mEmail, 5);
                                            Toast.makeText(EditProfileActivity.this, e.toString(), Toast.LENGTH_SHORT).show();

                                        }

                                    }
                                });
                    }

                    if (updateSuccess) {
                        Toast.makeText(EditProfileActivity.this, "Changes Saved", Toast.LENGTH_SHORT).show();
                    }

                }


            } else {
                Snackbar.make(mRootView, "Cannot update changes. " +
                                "Check your internet connection and try again.",
                        BaseTransientBottomBar.LENGTH_LONG).show();

            }
        }

    }
}
