package com.valhalla.studiac.activities.setup;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.valhalla.studiac.R;
import com.valhalla.studiac.activities.LoginActivity;
import com.valhalla.studiac.adapters.AutoCompleteCustomAdapter;
import com.valhalla.studiac.database.Firebase;
import com.valhalla.studiac.holders.TextItem;
import com.valhalla.studiac.utility.ErrorStyle;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ProfileSetupActivity extends AppCompatActivity {

    private AutoCompleteTextView mUniversityNameField;
    private AutoCompleteTextView mDepartmentNameField;
    private EditText mCurrentSemesterField;
    private EditText mTotalSemesterField;
    private FirebaseUser mUser;
    private final String TAG = "GoogleActivity";
    private final int BUTTON_DELAY_TIME = 1000;
    private long mLastClickTime = 0;
    private ArrayList<TextItem> mUniversityNames;
    private ArrayList<TextItem> mDepartmentNames;
    private ProgressBar mProgressBar;
    private Button mNextBtn;
    private ErrorStyle errorStyle;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profilesetup);
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        isUserAuthenticated(mUser);
        initUniversities();
        initDepartment();
        importData();
    }

    private void initDepartment() {
        mDepartmentNames = new ArrayList<>();
        mDepartmentNames.add(new TextItem("Computer Science and Engineering"));
        mDepartmentNames.add(new TextItem("Electrical and Electronic Engineering"));
        mDepartmentNames.add(new TextItem("Mechanical Engineering"));
        mDepartmentNames.add(new TextItem("Industrial and Production Engineering"));
        mDepartmentNames.add(new TextItem("Textile Engineering"));
        mDepartmentNames.add(new TextItem("Department of Humanities"));
        mDepartmentNames.add(new TextItem("Water Resources Engineering"));
        mDepartmentNames.add(new TextItem("Biomedical Engineering"));
        mDepartmentNames.add(new TextItem("Naval Architecture and Marine Engineering"));
        mDepartmentNames.add(new TextItem("Chemical Engineering"));
        mDepartmentNames.add(new TextItem("Materials and Metallurgical Engineering"));
        mDepartmentNames.add(new TextItem("Department of Chemistry"));
        mDepartmentNames.add(new TextItem("Department of Mathematics"));
        mDepartmentNames.add(new TextItem("Department of Physics"));
        mDepartmentNames.add(new TextItem("Petroleum and Mineral Resources Engineering"));
        mDepartmentNames.add(new TextItem("Glass and Ceramic Engineering"));
        mDepartmentNames.add(new TextItem("Civil Engineering"));
        mDepartmentNames.add(new TextItem("Urban and Regional Planning"));
        mDepartmentNames.add(new TextItem("Bachelors of Business Administration"));





    }


    public void startLoadingAnimation() {
        mProgressBar.setVisibility(View.VISIBLE);
    }

    public void stopLoadingAnimation() {
        mProgressBar.setVisibility(View.INVISIBLE);
    }

    /*
     * generates some university names for auto complete input
     */
    private void initUniversities() {
        mUniversityNames = new ArrayList<>();
        mUniversityNames.add(new TextItem("Ahsanullah University of Science & Technology"));
        mUniversityNames.add(new TextItem("North South University"));
        mUniversityNames.add(new TextItem("Independent University of Bangladesh"));


    }

    /*
     * imports the names of the universities already in the database
     */
    private void importData() {
        Firebase.getDatabaseReference().
                child(Firebase.UNIVERSITIES).
                addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            String name = snapshot.getKey();
                            TextItem textItem = new TextItem(name);
                            if (!mUniversityNames.contains(textItem)) {
                                mUniversityNames.add(textItem);
                            }
                        }

                        initViews();
                        onNextClick();

                        AutoCompleteCustomAdapter universityAdapter = new AutoCompleteCustomAdapter(
                                getApplicationContext(),
                                mUniversityNames);
                        mUniversityNameField.setAdapter(universityAdapter);
                        mUniversityNameField.setDropDownBackgroundDrawable(
                                getApplicationContext().getResources().getDrawable(R.drawable.custom_autocomplete_adapter_background));


                        AutoCompleteCustomAdapter departmentAdapter = new AutoCompleteCustomAdapter(
                                getApplicationContext(),
                                mDepartmentNames);
                        mDepartmentNameField.setAdapter(departmentAdapter);
                        mDepartmentNameField.setDropDownBackgroundDrawable(
                                getApplicationContext().getResources().getDrawable(R.drawable.custom_autocomplete_adapter_background));


                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }

    private void isUserAuthenticated(FirebaseUser user) {
        if (user == null) {
            Toast.makeText(this, "Session Expired. Log in again!", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, LoginActivity.class);
            // adding flags to intent so that in the new activity, pressing back button doesn't bring this activity back
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }

    }

    private void initViews() {
        errorStyle = ErrorStyle.getInstance(getApplicationContext());
        mUniversityNameField = findViewById(R.id.universityNameField);
        mDepartmentNameField = findViewById(R.id.departmentNameField);
        mCurrentSemesterField = findViewById(R.id.currentSemesterField);
        mTotalSemesterField = findViewById(R.id.totalSemesterField);
        mProgressBar = findViewById(R.id.profile_setup_progress_bar);
    }

    private void onNextClick() {
        mNextBtn = findViewById(R.id.nextButton);
        mNextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (SystemClock.elapsedRealtime() - mLastClickTime < BUTTON_DELAY_TIME) {
                    return;
                }
                mLastClickTime = SystemClock.elapsedRealtime();

                if (isInputValid()) {
                    mNextBtn.setEnabled(false);
                    startLoadingAnimation();
                    uploadData(); // if data successfully uploads, then continue
                }
            }
        });

    }

    /*
     *  uploads university name, deptName, currentSem, and totalSem to the database
     *  iff data is uploaded successfully, then continue to the next activity, else show error
     */
    private void uploadData() {
        Map<String, Object> userUpdates = new HashMap<>();
        userUpdates.put("universityName", mUniversityNameField.getText().toString());
        userUpdates.put("departmentName", mDepartmentNameField.getText().toString());
        userUpdates.put("currentSemester", Integer.parseInt(mCurrentSemesterField.getText().toString()));
        userUpdates.put("totalSemester", Integer.parseInt(mTotalSemesterField.getText().toString()));
        userUpdates.put("isProfileSetup", true);

        // uploads the values university name, dept, ....
        Firebase.getDatabaseReference().
                child(Firebase.USERS).
                child(mUser.getUid()).
                child(Firebase.BASIC_INFO).
                updateChildren(userUpdates).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                // uploads the uid to university/users/
                Firebase.getDatabaseReference().
                        child(Firebase.UNIVERSITIES).
                        child(mUniversityNameField.getText().toString()).
                        child(Firebase.USERS).
                        push().setValue(mUser.getUid()).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(ProfileSetupActivity.this, "Setup Complete!", Toast.LENGTH_SHORT).show();
                        stopLoadingAnimation();
                        mNextBtn.setEnabled(true);
                        Intent intent = new Intent(ProfileSetupActivity.this, ProfileSetup2Activity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(ProfileSetupActivity.this, "Connection Error", Toast.LENGTH_SHORT).show();
                        Log.d(TAG, "failed to upload uid in universities");
                        stopLoadingAnimation();
                        mNextBtn.setEnabled(true);


                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(ProfileSetupActivity.this, "Connection Error", Toast.LENGTH_SHORT).show();
                Log.d(TAG, "failed to upload user info");
                stopLoadingAnimation();
                mNextBtn.setEnabled(true);

            }
        });


    }

    public boolean isInputValid() {
        if (mUniversityNameField.getText().length() == 0) {
            errorStyle.setError(getString(R.string.Error_EmptyField), mUniversityNameField);
            return false;
        } else {
            errorStyle.resetError(mUniversityNameField);
        }

        if (mDepartmentNameField.getText().length() == 0) {
            errorStyle.setError(getString(R.string.Error_EmptyField), mDepartmentNameField);
            return false;
        } else {
            errorStyle.resetError(mDepartmentNameField);
        }

        if (mCurrentSemesterField.getText().length() == 0) {
            errorStyle.setError(getString(R.string.Error_EmptyField), mCurrentSemesterField);
            return false;
        } else {
            errorStyle.resetError(mCurrentSemesterField);
        }

        if (mTotalSemesterField.getText().length() == 0) {
            errorStyle.setError(getString(R.string.Error_EmptyField), mTotalSemesterField);
            return false;
        } else {
            errorStyle.resetError(mTotalSemesterField);
        }


        if (Integer.parseInt(mCurrentSemesterField.getText().toString()) > Integer.parseInt(mTotalSemesterField.getText().toString())) {
            errorStyle.setError(getString(R.string.Error_InvalidSemesterNumber), mCurrentSemesterField);
            return false;
        } else {
            errorStyle.resetError(mCurrentSemesterField);
        }

        return true;
    }

}
