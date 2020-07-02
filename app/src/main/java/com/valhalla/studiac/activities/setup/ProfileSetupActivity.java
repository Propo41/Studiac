package com.valhalla.studiac.activities.setup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

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

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.valhalla.studiac.R;
import com.valhalla.studiac.activities.LoginActivity;
import com.valhalla.studiac.database.Firebase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ProfileSetupActivity extends AppCompatActivity {

    private AutoCompleteTextView mUniversityNameField;
    private EditText mDepartmentNameField;
    private EditText mCurrentSemesterField;
    private EditText mTotalSemesterField;
    private FirebaseUser mUser;
    private final String TAG = "GoogleActivity";
    private final int BUTTON_DELAY_TIME = 1000;
    private long mLastClickTime = 0;
    private ArrayList<String> mUniversityNames;
    private ProgressBar mProgressBar;
    Button mNextBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profilesetup);
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        isUserAuthenticated(mUser);
        initUniversities();
        importData();



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
        mUniversityNames.add("Ahsanullah University of Science & Technology");
        mUniversityNames.add("North South University");
        mUniversityNames.add("Independent University of Bangladesh");
        mUniversityNames.add("United International University");
        mUniversityNames.add("University of Dhaka");
        mUniversityNames.add("Bangladesh Agricultural University");
        mUniversityNames.add("Bangladesh University of Engineering and Technology");
        mUniversityNames.add("Chittagong University");
        mUniversityNames.add("Chittagong University of Engineering and Technology");
        mUniversityNames.add("Jahangirnagar University");
        mUniversityNames.add("Shahjalal University of Science & Technology");
        mUniversityNames.add("International Islamic University Chittagong");
        mUniversityNames.add("American International University of Bangladesh");
        mUniversityNames.add("East West University");
        mUniversityNames.add("The University of Asia Pacific");
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
                            if (!mUniversityNames.contains(name)) {
                                mUniversityNames.add(name);
                            }
                        }

                        initViews();
                        onNextClick();

                        Typeface typeface = ResourcesCompat.getFont(getApplicationContext(), R.font.montserrat_regular);

                        ArrayAdapter<String> adapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_list_item_1, mUniversityNames);
                        mUniversityNameField.setAdapter(adapter);
                        mUniversityNameField.setDropDownBackgroundDrawable(getResources().getDrawable(R.drawable.shape_input_blue));
                        mUniversityNameField.setTypeface(typeface);
                        mUniversityNameField.setTextSize(15);

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
                Log.d(TAG, e.getMessage());
                Log.d(TAG, "failed to upload user info");
                stopLoadingAnimation();
                mNextBtn.setEnabled(true);

            }
        });


    }

    public boolean isInputValid() {
        boolean mReturnValue = true;

        if (mUniversityNameField.getText().length() == 0) {
            mUniversityNameField.setError("This Field cannot be Empty");
            mReturnValue = false;
            return mReturnValue;
        }

        if (mDepartmentNameField.getText().length() == 0) {
            mDepartmentNameField.setError("This Field cannot be Empty");
            mReturnValue = false;
        }

        if (mCurrentSemesterField.getText().length() == 0) {
            mCurrentSemesterField.setError("This Field cannot be Empty");
            mReturnValue = false;
        }

        if (mTotalSemesterField.getText().length() == 0) {
            mTotalSemesterField.setError("This Field cannot be Empty");
            mReturnValue = false;
        }

        if(Integer.parseInt(mCurrentSemesterField.getText().toString()) > Integer.parseInt(mTotalSemesterField.getText().toString()) ){
            mCurrentSemesterField.setError("Invalid current semester number");
            mReturnValue = false;
        }

        return mReturnValue;
    }

    public void debugAddValues(View view) {
        mUniversityNameField.setText("AUST");
        mDepartmentNameField.setText("CSE");
        mCurrentSemesterField.setText("2");
        mTotalSemesterField.setText("5");
    }

}
