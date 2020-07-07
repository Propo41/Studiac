package com.valhalla.studiac.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.valhalla.studiac.R;
import com.valhalla.studiac.adapters.result_tracker.ResultTrackerRecycleAdapter;
import com.valhalla.studiac.database.Firebase;
import com.valhalla.studiac.fragments.dialogs.CalculateGPABottomSheet;
import com.valhalla.studiac.fragments.dialogs.EditWeightUpdatePromptDialog;
import com.valhalla.studiac.fragments.dialogs.FinalSemesterCompletedDialog;
import com.valhalla.studiac.fragments.dialogs.SemesterGPAInputBottomSheet;
import com.valhalla.studiac.fragments.dialogs.SemesterResultDisplayDialog;
import com.valhalla.studiac.fragments.dialogs.UpdateSemesterCalculateGPAPromptDialog;
import com.valhalla.studiac.fragments.dialogs.UpdateSemesterConfirmDialog;
import com.valhalla.studiac.fragments.dialogs.UpdateSemesterSuccessfulDialog;
import com.valhalla.studiac.models.Course;
import com.valhalla.studiac.models.Pojo;
import com.valhalla.studiac.models.Result;
import com.valhalla.studiac.models.Routine;
import com.valhalla.studiac.models.Weight;
import com.valhalla.studiac.toolbars.NavigationToolbarWhite;
import com.valhalla.studiac.utility.Common;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

public class ResultTrackerActivity extends NavigationToolbarWhite implements SemesterGPAInputBottomSheet.BottomSheetListener,
        CalculateGPABottomSheet.calculateGPAButtonListener,
        UpdateSemesterConfirmDialog.UpdateSemesterListener,
        UpdateSemesterCalculateGPAPromptDialog.UpdateGpaPromptListener,
        EditWeightUpdatePromptDialog.EditWeightPromptListener {

    private FirebaseUser mUser;
    private RecyclerView mRecycleView;
    private ResultTrackerRecycleAdapter mAdapter;
    private FloatingActionButton mCalculateGpaButton;
    private int mSelectedItemPosition;
    private final int RESULT_WEIGHTS_UPDATED = 1;
    private int mCurrentSemester;
    private int mTotalSemester;
    private ShimmerFrameLayout mShimmerViewContainer;

    private final String TAG = "ResultTrackerActivity";
    public static double mLastSemesterGPA;
    public static double mLastSemesterCoursesTaken;
    public double cumulativeGPA = 0;

    private ArrayList<Course> mCourseList;
    private ArrayList<Result> mResultList;
    private ArrayList<Weight> mWeightList;
    public Boolean mWeightsUpdated;
    private Double mCgpa;
    private Pojo passGPA;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContent(R.layout.activity_result_tracker);
        mUser = FirebaseAuth.getInstance().getCurrentUser();

        if (super.isUserAuthenticated(mUser)) {
            mShimmerViewContainer = findViewById(R.id.result_tracker_shimmer_layout);
            mShimmerViewContainer.startShimmerAnimation();
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

    @Override
    public void onBackPressed() {
        // when the back button is pressed, simply pass the cgpa to the parent activity
        Intent resultIntent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putDouble("cgpa", mCgpa);
        resultIntent.putExtras(bundle);
        setResult(RESULT_OK, resultIntent);
        finish();
    }

    private void importData() {
        final DatabaseReference ref = Firebase.getDatabaseReference().
                child(Firebase.USERS).
                child(mUser.getUid());

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
                        }
                        // [STOP] import_results

                        // [START] import_grade_weight
                        ref.child(Firebase.WEIGHTS).child("list").addListenerForSingleValueEvent(
                                new ValueEventListener() {
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
                                                                addListenerForSingleValueEvent(
                                                                        new ValueEventListener() {
                                                                            @Override
                                                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                                                mCourseList = new ArrayList<>();
                                                                                for (DataSnapshot data : dataSnapshot.getChildren()) {
                                                                                    mCourseList.add(data.getValue(Course.class));
                                                                                }
                                                                                // [STOP] import_courses
                                                                                // [STOP] IMPORT_DATA

                                                                                ref.child(Firebase.CGPA).
                                                                                        addListenerForSingleValueEvent(new ValueEventListener() {
                                                                                            @Override
                                                                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                                                                mCgpa = dataSnapshot.getValue(Double.class);
                                                                                                if (mCgpa == null) {
                                                                                                    mCgpa = 0.0;
                                                                                                }
                                                                                                passGPA = new Pojo(mCgpa);
                                                                                                initializeViews();
                                                                                                setupList();
                                                                                                attachListeners();

                                                                                                Bundle bundle = getIntent().getExtras();
                                                                                                if (bundle != null) {
                                                                                                    String str = bundle.getString("className");
                                                                                                    if (str != null) {
                                                                                                        if (str.equals("DashboardActivity")) {
                                                                                                            updateSemester();
                                                                                                        }
                                                                                                    }
                                                                                                }

                                                                                                ref.child(Firebase.BASIC_INFO).
                                                                                                        child("currentSemester").
                                                                                                        addListenerForSingleValueEvent(new ValueEventListener() {
                                                                                                            @Override
                                                                                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                                                                                if (dataSnapshot.exists()) {
                                                                                                                    Integer current = dataSnapshot.getValue(Integer.class);
                                                                                                                    if (current != null) {
                                                                                                                        mCurrentSemester = current;
                                                                                                                        ref.child(Firebase.BASIC_INFO).
                                                                                                                                child("totalSemester").
                                                                                                                                addListenerForSingleValueEvent(new ValueEventListener() {
                                                                                                                                    @Override
                                                                                                                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                                                                                                        if (dataSnapshot.exists()) {
                                                                                                                                            Integer total = dataSnapshot.getValue(Integer.class);
                                                                                                                                            if (total != null) {
                                                                                                                                                mTotalSemester = total;
                                                                                                                                            }

                                                                                                                                            mShimmerViewContainer.stopShimmerAnimation();
                                                                                                                                            mShimmerViewContainer.setVisibility(View.GONE);

                                                                                                                                        }


                                                                                                                                    }

                                                                                                                                    @Override
                                                                                                                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                                                                                                                    }
                                                                                                                                });
                                                                                                                    }


                                                                                                                }
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
                        Toast toast = Toast.makeText(ResultTrackerActivity.this,
                                "Nothing to calculate as no courses are added yet!",
                                Toast.LENGTH_SHORT);
                        TextView textView = toast.getView().findViewById(android.R.id.message);
                        if (textView != null) {
                            textView.setGravity(Gravity.CENTER);
                        }
                        toast.show();

                    } else {
                        CalculateGPABottomSheet bottomSheet = new CalculateGPABottomSheet(mResultList.get(mResultList.size() - 1),
                                mCourseList, mWeightList, mResultList);
                        bottomSheet.show(getSupportFragmentManager(), "exampleBottomSheet");
                    }
                } else {
                    // open dialog to prompt user to update grade weights
                    EditWeightUpdatePromptDialog editGradeWeightBottomSheet =
                            new EditWeightUpdatePromptDialog();
                    // new EditWeightUpdatePromptDialog(mWeightList, mResultList, mCourseList);
                    editGradeWeightBottomSheet.show(getSupportFragmentManager(), "bla bla");
                }

            }
        });
    }

    private void setupList() {
        mRecycleView.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        mAdapter = new ResultTrackerRecycleAdapter(mResultList, getApplicationContext(), passGPA);
        mRecycleView.setLayoutManager(mLayoutManager);
        mRecycleView.setAdapter(mAdapter);

        handleListEvents();
    }

    /**
     * @events: semester item clicked
     * @events: semester item edit button clicked
     */
    private void handleListEvents() {
        mAdapter.setOnItemClickListener(new ResultTrackerRecycleAdapter.OnItemClickListener() {
            @Override
            public void onEditClick(int position) {
                mSelectedItemPosition = position;
                // open bottom sheet to edit semester result
                SemesterGPAInputBottomSheet bottomSheet = new SemesterGPAInputBottomSheet();
                bottomSheet.show(getSupportFragmentManager(), "exampleBottomSheet");
            }

            @Override
            public void onItemClick(int position) {
                int currentSemester = mResultList.size() - 1;

                // if clicked item doesn't have its gpa calculated
                if (!mResultList.get(position).getIsGpaCalculated()) {
                    // if clicked item is the last item in the list
                    if (currentSemester == position) {
                        createToast("You haven't calculated anything yet");
                    } else {
                        createToast("Semester details not entered");
                    }
                } else {
                    // if the clicked item has its gpa calculated, open a result display dialog
                    SemesterResultDisplayDialog resultDialog = new SemesterResultDisplayDialog(mResultList.get(position));
                    resultDialog.show(getSupportFragmentManager(), "ResultTrackerActivity");
                }
            }
        });

    }

    private void createToast(String message) {
        Toast toast = Toast.makeText(ResultTrackerActivity.this,
                message,
                Toast.LENGTH_SHORT);
        TextView v = toast.getView().findViewById(android.R.id.message);
        if (v != null) {
            v.setGravity(Gravity.CENTER);
        }
        toast.show();
    }

    private void initializeViews() {
        mRecycleView = findViewById(R.id.recyclerView);
        mCalculateGpaButton = findViewById(R.id.current_semester_result_input);
    }

    private void calculateCGPALastSemester() {
        double cgpa = 0;
        double totalWeight = 0;

        for (int i = 0; i < mCourseList.size(); i++) {
            String mCourseGrade = mResultList.get(mResultList.size() - 1).getGradesObtainedValue(mCourseList.get(i).getCode());
            Double mGetGradePoint = mWeightList.get(pickGradeIndex(mCourseGrade)).getWeight();
            cgpa += mCourseList.get(i).getCredit() * mGetGradePoint;
            totalWeight += mCourseList.get(i).getCredit();
            //System.out.println(" ACTIVITY TRACKER " + mCourseGrade + "  " + mGetGradePoint);
        }
        cumulativeGPA = cgpa;
        cgpa = cgpa / totalWeight;


        mLastSemesterGPA = cgpa;
        mLastSemesterCoursesTaken = totalWeight;

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
            if (mResultList.get(i).getGpa() != 0.0 && mResultList.get(i).getGpa() != null) {
                cgpa += mResultList.get(i).getGpa() * mResultList.get(i).getTotalCredits();
                totalWeight += mResultList.get(i).getTotalCredits();
            }
        }

        if (cumulativeGPA != 0.0) {
            cgpa += cumulativeGPA;
        }
        totalWeight += mLastSemesterCoursesTaken;

        cgpa = cgpa / totalWeight;

        String sValue = String.format(Locale.getDefault(), "%.3f", cgpa);
        mCgpa = Double.parseDouble(sValue);

        Firebase.getDatabaseReference().
                child(Firebase.USERS).
                child(mUser.getUid()).
                child("cgpa").
                setValue(mCgpa).
                addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        // Toast.makeText(ResultTrackerActivity.this, "CGPA updated", Toast.LENGTH_SHORT).show();

                    }
                });

        passGPA.setGPA(mCgpa);

        mAdapter.notifyItemChanged(0);


    }

    /*
     * open bottom sheet dialog J
     * called from bottomSheet to take cgpa and courses taken input
     *
     */

    @Override
    public void onButtonCLicked(double mGPAEntered, double mTotalCredit, Integer mCoursesTaken) {
        updateAdapter(mGPAEntered, mTotalCredit);
        mResultList.get(mSelectedItemPosition).setCoursesTaken(mCoursesTaken);
        mResultList.get(mSelectedItemPosition).setIsGpaCalculated(true);
        // if the clicked item has its gpa calculated, open a result display dialog
        SemesterResultDisplayDialog resultDialog = new SemesterResultDisplayDialog(mResultList.get(mSelectedItemPosition));
        resultDialog.show(getSupportFragmentManager(), "ResultTrackerActivity");
    }


    public void updateAdapter(double mGPAEntered, double mTotalCredits) {
        mResultList.get(mSelectedItemPosition).setGpa(mGPAEntered);
        mResultList.get(mSelectedItemPosition).setTotalCredits(mTotalCredits);
        //  mResultList.get(mSelectedItemPosition).setSemesterNumber(mSelectedItemPosition);
        calculateCGPA();
        mAdapter.notifyItemChanged(mSelectedItemPosition + 1);

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
        mResultList.get(mResultList.size() - 1).setIsGpaCalculated(true);
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
                updateSemester();
                return true;
            case android.R.id.home:
                onBackPressed();
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void updateSemester() {
        // if the current semester is not the
        if (mCurrentSemester != mTotalSemester) {
            if (mWeightsUpdated) {
                if (mCourseList.size() != 0) {
                    if (mResultList.get(mResultList.size() - 1).getIsGpaCalculated()) {
                        UpdateSemesterConfirmDialog updateSemesterConfirmDialog =
                                new UpdateSemesterConfirmDialog();
                        updateSemesterConfirmDialog.show(getSupportFragmentManager(), "Result Tracker");
                    } else {
                        UpdateSemesterCalculateGPAPromptDialog updateSemesterCalculateGPAPromptDialog =
                                new UpdateSemesterCalculateGPAPromptDialog();
                        updateSemesterCalculateGPAPromptDialog.show(getSupportFragmentManager(), "Result Tracker");
                    }
                } else {

                    Toast toast = Toast.makeText(ResultTrackerActivity.this,
                            "Nothing to calculate as no courses are added yet!",
                            Toast.LENGTH_SHORT);
                    TextView v = toast.getView().findViewById(android.R.id.message);
                    if (v != null) {
                        v.setGravity(Gravity.CENTER);
                    }
                    toast.show();

                }
            } else {
                Toast.makeText(this, "Please Update Grade Weights", Toast.LENGTH_SHORT).show();
            }
        } else {
            FinalSemesterCompletedDialog finalSemesterCompletedDialog
                    = new FinalSemesterCompletedDialog();
            finalSemesterCompletedDialog.show(getSupportFragmentManager(), "Result Tracker");
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


    /**
     * Deletes all current semester courses and routine and creates a new item list
     * for a new semester
     */
    @Override
    public void deleteCourses() {
        ArrayList<Routine> routines = new ArrayList<>();
        // initializing the list for 7 days.
        for (int i = 0; i < 7; i++) {
            routines.add(new Routine(Common.GET_DAY_FROM_INDEX[i]));
        }

        HashMap<String, Object> childUpdates = new HashMap<>();
        childUpdates.put(Common.COURSES, null);
        childUpdates.put(Common.ROUTINE, routines);

        FirebaseDatabase.getInstance().getReference().
                child(Firebase.USERS).
                child(mUser.getUid()).
                updateChildren(childUpdates).
                addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        mResultList.add(new Result("init"));
                        //  mAdapter.notifyItemChanged(mResultList.size()-2);
                        mAdapter.notifyItemInserted(mResultList.size() - 1);
                        mRecycleView.scrollToPosition(mResultList.size());
                        mRecycleView.scrollToPosition(mResultList.size() - 1);
                        mAdapter.notifyDataSetChanged();
                        Firebase.getDatabaseReference().
                                child(Firebase.USERS).
                                child(mUser.getUid()).
                                child(Firebase.BASIC_INFO).
                                child("currentSemester").
                                setValue(mResultList.size());

                        UpdateSemesterSuccessfulDialog updateSemesterSuccessfulDialog
                                = new UpdateSemesterSuccessfulDialog(mCgpa);
                        updateSemesterSuccessfulDialog.show(getSupportFragmentManager(), "ResultTrackerActivity");

                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(ResultTrackerActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
                e.printStackTrace();

            }
        });


    }


    /*
     *
     *called from UpdateSemesterCalculateGPAPrompt
     */
    @Override
    public void openBottomSheetGPACalculation() {
        //startActivity(new Intent(getContext(), ResultTrackerActivity.class));
        CalculateGPABottomSheet bottomSheet = new CalculateGPABottomSheet(mResultList.get(mResultList.size() - 1),
                mCourseList, mWeightList, mResultList);
        bottomSheet.show(getSupportFragmentManager(), "exampleBottomSheet");
    }

    /*
     *
     * Called from edit weight prompt
     */
    @Override
    public void ignoreBtnClicked() {
        Firebase.getDatabaseReference().
                child(Firebase.USERS).
                child(mUser.getUid()).
                child(Firebase.WEIGHTS).
                child("updated").setValue(true).
                addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        mWeightsUpdated = true;
                        if (mCourseList.size() != 0) {
                            CalculateGPABottomSheet bottomSheet = new CalculateGPABottomSheet(
                                    mResultList.get(mResultList.size() - 1),
                                    mCourseList, mWeightList, mResultList);
                            bottomSheet.show(getSupportFragmentManager(), "ResultTrackerActivity");
                        } else {
                            Toast toast = Toast.makeText(ResultTrackerActivity.this,
                                    "Nothing to calculate as no courses are added yet!",
                                    Toast.LENGTH_SHORT);
                            TextView v = toast.getView().findViewById(android.R.id.message);
                            if (v != null) {
                                v.setGravity(Gravity.CENTER);
                            }
                            toast.show();
                        }

                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(), "Failed to update", Toast.LENGTH_SHORT).show();
            }
        });


    }

    @Override
    public void updateBtnClicked() {
        Intent intent = new Intent(this, EditWeightActivity.class);
        startActivity(intent);
    }
}
