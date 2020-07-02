package com.valhalla.studiac.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.valhalla.studiac.R;
import com.valhalla.studiac.adapters.result_tracker.ResultTrackerRecycleAdapter;
import com.valhalla.studiac.database.Firebase;
import com.valhalla.studiac.fragments.dialogs.CalculateGPABottomSheet;
import com.valhalla.studiac.fragments.dialogs.EditWeightUpdatePromptDialog;
import com.valhalla.studiac.fragments.dialogs.LastSemesterDisplayDialog;
import com.valhalla.studiac.fragments.dialogs.SemesterGPAInputBottomSheet;
import com.valhalla.studiac.fragments.dialogs.SemesterResultDisplayDialog;
import com.valhalla.studiac.models.Course;
import com.valhalla.studiac.models.Result;
import com.valhalla.studiac.models.Weight;
import com.valhalla.studiac.toolbars.NavigationToolbarWhite;
import com.valhalla.studiac.utility.Common;
import java.util.ArrayList;
import java.util.HashMap;

public class ResultTrackerActivity extends NavigationToolbarWhite implements SemesterGPAInputBottomSheet.BottomSheetListener,
        CalculateGPABottomSheet.OnItemClickListener {

    private FirebaseUser mUser;
    private RecyclerView mRecycleView;
    private ResultTrackerRecycleAdapter mAdapter;
   // private TextView mCGPAText;
    private FloatingActionButton mCalculateGpaButton;
    private int mSelectedItemPosition;
    private final int RESULT_WEIGHTS_UPDATED = 1;

    private final String TAG = "ResultTrackerActivity";
    public static double mLastSemesterGPA;
    public static double mLastSemesterCoursesTaken;

    private ArrayList<Course> mCourseList;
    private ArrayList<Result> mResultList;
    private ArrayList<Weight> mWeightList;
    private Boolean mWeightsUpdated;
    private Double mCgpa;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContent(R.layout.activity_result_tracker);
        mUser = FirebaseAuth.getInstance().getCurrentUser();
        if (super.isUserAuthenticated(mUser)) {
            importData();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        // upload the result
        Firebase.getDatabaseReference().
                child(Firebase.USERS).
                child(mUser.getUid()).
                child(Firebase.RESULTS).
                setValue(mResultList);
    }

    private void importData() {
        final DatabaseReference ref = Firebase.getDatabaseReference().
                child(Firebase.USERS).
                child(mUser.getUid());

        super.startLoadingAnimation();
        // [START] IMPORT_DATA
        // [START] import_results
        ref.child(Firebase.RESULTS).
                addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        mResultList = new ArrayList<>();
                        for (DataSnapshot data : dataSnapshot.getChildren()) {
                            Result result = data.getValue(Result.class);
                            if (result != null) {
                                if (result.getGradesObtained() == null) {
                                    result.setGradesObtained(new HashMap<String, String>());
                                }
                                mResultList.add(result);
                            } else {
                                Log.d(TAG, "imported result is null");
                            }

                            // for debug - for current semester
                        }
                        // [STOP] import_results

                        // [START] import_grade_weight
                        ref.child(Firebase.WEIGHTS).child("list").addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                mWeightList = new ArrayList<>();
                                for (DataSnapshot data : dataSnapshot.getChildren()) {
                                    mWeightList.add(data.getValue(Weight.class));
                                }

                                // [STOP] import_grade_weight

                                ref.child(Firebase.WEIGHTS).child("updated").
                                        addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                // importing the boolean value
                                                mWeightsUpdated = dataSnapshot.getValue(Boolean.class);

                                                // [START] import_courses
                                                ref.child(Common.COURSES).
                                                        addListenerForSingleValueEvent(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                        mCourseList = new ArrayList<>();
                                                        for (DataSnapshot data : dataSnapshot.getChildren()) {
                                                            mCourseList.add(data.getValue(Course.class));
                                                        }
                                                        // [STOP] import_courses
                                                        // [STOP] IMPORT_DATA
                                                        ResultTrackerActivity.super.stopLoadingAnimation();
                                                        //    calculateCGPALastSemester();
                                                        //  calculateCGPA();

                                                        ref.child(Firebase.CGPA).
                                                                addListenerForSingleValueEvent(new ValueEventListener() {
                                                                    @Override
                                                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                                        mCgpa = dataSnapshot.getValue(Double.class);
                                                                        if (mCgpa == null) {
                                                                            Log.d(TAG, "CGPA DOWNLAODED IS NULL");
                                                                            mCgpa = 0.0;
                                                                        }
                                                                        initializeViews();
                                                                    //    setContent();

                                                                        setupList();
                                                                        attachListeners();
                                                                    }

                                                                    @Override
                                                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                                                    }
                                                                });


                                                    }

                                                    @Override
                                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                                    }
                                                });

                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError databaseError) {

                                            }
                                        });
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });


                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

    }



    private void attachListeners() {
        mCalculateGpaButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // if grade weight is updated, open calculator
                if (mWeightsUpdated) {
                    if (mCourseList.size() == 0) {
                        Toast.makeText(ResultTrackerActivity.this, "No Courses Added. Please Add courses First", Toast.LENGTH_SHORT).show();
                    } else {
                        CalculateGPABottomSheet bottomSheet = new CalculateGPABottomSheet(
                                mResultList.get(mResultList.size() - 1),
                                mCourseList, mWeightList, mResultList);
                        bottomSheet.show(getSupportFragmentManager(), "exampleBottomSheet");
                    }
                } else {
                    // open dialog to prompt user to update grade weights
                    EditWeightUpdatePromptDialog editGradeWeightBottomSheet =
                            new EditWeightUpdatePromptDialog(mWeightList, mResultList, mCourseList);
                    editGradeWeightBottomSheet.show(getSupportFragmentManager(), "bla bla");
                }

            }
        });
    }

    private void setupList() {
        mRecycleView.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        mAdapter = new ResultTrackerRecycleAdapter(mResultList, getApplicationContext(), mCourseList.size(), mCgpa);
        mRecycleView.setLayoutManager(mLayoutManager);
        mRecycleView.setAdapter(mAdapter);

        mAdapter.setOnItemClickListener(new ResultTrackerRecycleAdapter.AdapterResultTrackerOnItemClickListener() {
            @Override
            public void onEditClick(int position) {
                mSelectedItemPosition = position;
                Toast.makeText(ResultTrackerActivity.this, "clicked at " + position, Toast.LENGTH_SHORT).show();
                // open bottom sheet to edit semester result
                SemesterGPAInputBottomSheet bottomSheet = new SemesterGPAInputBottomSheet();
                bottomSheet.show(getSupportFragmentManager(), "exampleBottomSheet");
            }
        });

        mAdapter.setOnItemClickListener2(new ResultTrackerRecycleAdapter.AdapterResultTrackerOnItemClickListener2() {
            @Override
            public void onItemClick(int position) {
                // open semester details dialog
                if (position == mResultList.size() - 1) {
                    if (mResultList.get(mResultList.size() - 1).getGpa() == 0) {
                        Toast.makeText(ResultTrackerActivity.this, "You haven't calculated anything yet", Toast.LENGTH_SHORT).show();
                    } else {
                        LastSemesterDisplayDialog dialogOfCalculator = new LastSemesterDisplayDialog(mResultList, mResultList.get(mResultList.size() - 1).getGpa(), mResultList.get(mResultList.size() - 1).getGradesObtained().size());
                        dialogOfCalculator.show(getSupportFragmentManager(), "bla bla");
                    }
                } else {
                    if (!(mResultList.get(position).getGpa() == 0.0)) {
                        semesterResultDisplay(mResultList.get(position).getGpa(), mResultList.get(position).getCoursesTaken());
                    } else {
                        Toast.makeText(ResultTrackerActivity.this, "Semester details not entered", Toast.LENGTH_SHORT).show();
                    }

                }
            }

        });

    }

    private void initializeViews() {
        mRecycleView = findViewById(R.id.recyclerView);
     //   mCGPAText = findViewById(R.id.gpaText);
        mCalculateGpaButton = findViewById(R.id.current_semester_result_input);
    }

    private void calculateCGPALastSemester() {
        double cgpa = 0;
        double totalWeight = 0;
       /* System.out.println(mCourseList.get(0).getCode() + " ohoo ");
        System.out.println(mResultList.get(mResultList.size() - 1).getGradesObtainedValue(mCourseList.get(0).getCode())+ " ohhooo");*/

        for (int i = 0; i < mCourseList.size(); i++) {
            String mCourseGrade = mResultList.get(mResultList.size() - 1).getGradesObtainedValue(mCourseList.get(i).getCode());
            Double mGetGradePoint = mWeightList.get(pickGradeIndex(mCourseGrade)).getWeight();
            cgpa += mCourseList.get(i).getCredit() * mGetGradePoint;
            totalWeight += mCourseList.get(i).getCredit();
            System.out.println("PRINTING LIST IN TRACKER MAIN " + mCourseGrade + "  " + mGetGradePoint);
        }

        cgpa = cgpa / totalWeight;
        //mGPA.setText("GPA : " +String.format("%.3f", cgpa));
        //Toast.makeText(this, cgpa+"", Toast.LENGTH_SHORT).show();
        //mNumberOfCourse.setText("Courses Taken : " + obtainedGrades.length);
        mLastSemesterGPA = cgpa;
        mLastSemesterCoursesTaken = totalWeight;
        System.out.println(mLastSemesterGPA);
        System.out.println(mLastSemesterCoursesTaken);
    }

    private int pickGradeIndex(String s) {
        int mReturnValue = 0;

        for (int i = 0; i < mWeightList.size(); i++) {
            if (s.equals(mWeightList.get(i).getGrade()))
                mReturnValue = i;
        }

        return mReturnValue;
    }

    private void calculateCGPA() {
        double cgpa = 0.0;
        double totalWeight = 0;

        for (int i = 0; i < mResultList.size() - 1; i++) {
            if (mResultList.get(i).getGpa() != 0.0) {
                cgpa += mResultList.get(i).getGpa() * mResultList.get(i).getTotalCredits();
                totalWeight += mResultList.get(i).getTotalCredits();
            }
        }
        cgpa = cgpa / totalWeight;
       // mCgpa = cgpa;

        if (totalWeight == 0) {
         //   mCGPAText.setText(getString(R.string.PlaceHolder_CGPA, "N/A"));

        } else {
           // mCGPAText.setText(getString(R.string.PlaceHolder_CGPA, Double.toString(cgpa)));
        }

    }

    /*
     * open bottom sheet dialog J
     * called from bottomSheet to take cgpa and courses taken input
     */
    @Override
    public void onButtonCLicked(double mGPAEntered, double mTotalCredit, Integer mCoursesTaken) {
        updateAdapter(mGPAEntered, mTotalCredit);
        mResultList.get(mSelectedItemPosition).setCoursesTaken(mCoursesTaken);
        semesterResultDisplay(mGPAEntered, mCoursesTaken);
    }

    private void semesterResultDisplay(double mGPAEntered, Integer mCoursesTaken) {
        SemesterResultDisplayDialog dialogK = new SemesterResultDisplayDialog(mGPAEntered + "", mCoursesTaken + "");
        dialogK.show(getSupportFragmentManager(), "bla bla");
    }

    public void updateAdapter(double mGPAEntered, double mTotalCredits) {
        mResultList.get(mSelectedItemPosition).setGpa(mGPAEntered);
        mResultList.get(mSelectedItemPosition).setTotalCredits(mTotalCredits);
        //  mResultList.get(mSelectedItemPosition).setSemesterNumber(mSelectedItemPosition);
        calculateCGPA();
        mAdapter.notifyItemChanged(mSelectedItemPosition+1);
    }

    /*
     * @ interface method
     *  called from bottom sheet G to set semester gpa value after
     *  changing value of grades
     */
    @Override
    public double calculateSemesterGpa() {
        int position = mResultList.size();
        mSelectedItemPosition = position - 1;
        calculateCGPALastSemester();
        updateAdapter(mLastSemesterGPA, mLastSemesterCoursesTaken);
        mAdapter.notifyItemChanged(mSelectedItemPosition);
        mResultList.get(mResultList.size() - 1).setGpa(mLastSemesterGPA);
        return mLastSemesterGPA;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.options_menu_result_tracker, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.more_grade_weights_id:
                startActivityForResult(new Intent(this, EditWeightActivity.class), RESULT_WEIGHTS_UPDATED);
                return true;
            case R.id.more_update_semester_id:
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == RESULT_WEIGHTS_UPDATED) {
            if (data != null) {
                mWeightsUpdated = data.getBooleanExtra("weightsUpdated", false);
            }
        }
    }
}
