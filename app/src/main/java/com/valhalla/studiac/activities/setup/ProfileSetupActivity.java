package com.valhalla.studiac.activities.setup;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.SystemClock;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.Group;

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
import com.valhalla.studiac.adapters.SpinnerAdapter;
import com.valhalla.studiac.database.Firebase;
import com.valhalla.studiac.holders.ImageItem;
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
    private Button mNextBtn;
    private ErrorStyle errorStyle;
    private ProgressBar mProgressBar;

    private boolean dialogState;
    private static final int TIMEOUT = 20000;
    private ArrayList<ImageItem> mSpinnerItemsSection;
    private ArrayList<ImageItem> mSpinnerItemsGroup;
    private String mSectionSelected;
    private String mGroupSelected;
    private Group mGroupAust;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profilesetup);
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        isUserAuthenticated(mUser);
        initUniversities();
        initDepartment();
        initSpinnerItems();
        importData();
    }

    private void initSpinnerItems() {
        mSpinnerItemsSection = new ArrayList<>();
        mSpinnerItemsGroup = new ArrayList<>();
        mSpinnerItemsSection.add(new ImageItem("A", R.drawable.ic_section));
        mSpinnerItemsSection.add(new ImageItem("B", R.drawable.ic_section));
        mSpinnerItemsSection.add(new ImageItem("C", R.drawable.ic_section));

        mSpinnerItemsGroup.add(new ImageItem("A1", R.drawable.ic_group));
        mSpinnerItemsGroup.add(new ImageItem("A2", R.drawable.ic_group));
        mSpinnerItemsGroup.add(new ImageItem("B1", R.drawable.ic_group));
        mSpinnerItemsGroup.add(new ImageItem("B2", R.drawable.ic_group));
        mSpinnerItemsGroup.add(new ImageItem("C1", R.drawable.ic_group));
        mSpinnerItemsGroup.add(new ImageItem("C2", R.drawable.ic_group));

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




    // handles spinner group events
    private void populateSpinner1(Spinner spinner) {
        SpinnerAdapter adapter = new SpinnerAdapter(getApplicationContext(), mSpinnerItemsGroup, ProfileSetup2Activity.class.getSimpleName());
        spinner.setAdapter(adapter);
        handleSpinnerEvents1(spinner);
    }

    // handles spinner section events
    private void populateSpinner2(Spinner spinner) {
        SpinnerAdapter adapter = new SpinnerAdapter(getApplicationContext(), mSpinnerItemsSection, ProfileSetup2Activity.class.getSimpleName());
        spinner.setAdapter(adapter);
        handleSpinnerEvents2(spinner);
    }

    // handles spinner group events
    private void handleSpinnerEvents1(Spinner spinner) {
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ImageItem clickedItem = (ImageItem) parent.getItemAtPosition(position);
                mGroupSelected = clickedItem.getName();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    // handles spinner section events
    private void handleSpinnerEvents2(Spinner spinner) {
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ImageItem clickedItem = (ImageItem) parent.getItemAtPosition(position);
                mSectionSelected = clickedItem.getName();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
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
                    Toast toast = Toast.makeText(ProfileSetupActivity.this, "Connection timeout.", Toast.LENGTH_SHORT);
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

                        mUniversityNameField.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                            @Override
                            public void onFocusChange(View v, boolean hasFocus) {
                                if (!hasFocus) {
                                    String university = mUniversityNameField.getText().toString().toLowerCase();
                                    if (university.equals("ahsanullah university of science & technology")
                                            || university.equals("aust")) {
                                        // show the 2 spinners
                                        mGroupAust.setVisibility(View.VISIBLE);
                                    } else {
                                        mGroupAust.setVisibility(View.GONE);
                                    }

                                }
                            }
                        });

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
        mGroupAust = findViewById(R.id.profile_setup_aust_group);
        errorStyle = ErrorStyle.getInstance(getApplicationContext());
        mUniversityNameField = findViewById(R.id.universityNameField);
        mDepartmentNameField = findViewById(R.id.departmentNameField);
        mCurrentSemesterField = findViewById(R.id.currentSemesterField);
        mTotalSemesterField = findViewById(R.id.totalSemesterField);
        mProgressBar = findViewById(R.id.profile_setup_progress_bar);
        Spinner spinnerSection = findViewById(R.id.profile_setup_select_section);
        Spinner spinnerGroup = findViewById(R.id.profile_setup_select_group);

        populateSpinner1(spinnerGroup);
        populateSpinner2(spinnerSection);
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
        final String aust = "ahsanullah university of science & technology";
        final String university = mUniversityNameField.getText().toString().toLowerCase();

        Map<String, Object> userUpdates = new HashMap<>();
        userUpdates.put("universityName", mUniversityNameField.getText().toString());
        userUpdates.put("departmentName", mDepartmentNameField.getText().toString());
        userUpdates.put("currentSemester", Integer.parseInt(mCurrentSemesterField.getText().toString()));
        userUpdates.put("totalSemester", Integer.parseInt(mTotalSemesterField.getText().toString()));
        userUpdates.put("isProfileSetup", true);

        if (university.equals(aust) || university.equals("aust")) {
            userUpdates.put("section", mSectionSelected);
            userUpdates.put("group", mGroupSelected);

        }

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


                        // iff user is from aust, then send the following data as a bundle
                        if (university.equals(aust) || university.equals("aust")) {
                            Bundle bundle = new Bundle();
                            bundle.putString("university", mUniversityNameField.getText().toString());
                            bundle.putString("department", mDepartmentNameField.getText().toString());
                            bundle.putString("currentSemester", mCurrentSemesterField.getText().toString());
                            bundle.putString("section", mSectionSelected);
                            bundle.putString("group", mGroupSelected);
                            intent.putExtras(bundle);
                        }

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
