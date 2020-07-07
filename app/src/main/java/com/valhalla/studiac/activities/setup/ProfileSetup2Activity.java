package com.valhalla.studiac.activities.setup;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.Group;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.valhalla.studiac.R;
import com.valhalla.studiac.activities.DashboardActivity;
import com.valhalla.studiac.activities.LoginActivity;
import com.valhalla.studiac.adapters.SpinnerAdapter;
import com.valhalla.studiac.database.Firebase;
import com.valhalla.studiac.fragments.dialogs.CourseAddedPromptDialog;
import com.valhalla.studiac.fragments.dialogs.SetupAddRoutineDialog;
import com.valhalla.studiac.holders.ImageItem;
import com.valhalla.studiac.models.Course;
import com.valhalla.studiac.models.Result;
import com.valhalla.studiac.models.Routine;
import com.valhalla.studiac.models.Schedule;
import com.valhalla.studiac.models.Student;
import com.valhalla.studiac.models.TodoTasks;
import com.valhalla.studiac.models.Weight;
import com.valhalla.studiac.utility.Common;
import com.valhalla.studiac.utility.ErrorStyle;

import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

public class ProfileSetup2Activity extends AppCompatActivity implements View.OnClickListener, CourseAddedPromptDialog.OnButtonClickListener {

    private EditText mCourseNameField;
    private EditText mCourseCreditField;
    private EditText mCourseCodeField;
    private FloatingActionButton mAddRoutine;
    private Button mAddCourse;
    private TextView mAddLater;
    final Context context = this;
    private ArrayList<Schedule> mSchedules = new ArrayList<>();
    private ArrayList<Routine> mRoutines;
    private boolean newEntry;
    private ArrayList<Course> mCourses = new ArrayList<>();
    private FirebaseUser mUser;
    private ArrayList<ImageItem> mSpinnerItemsCourseType;
    private String mCourseTypeSelected;
    private Result mCurrentSemesterResult;
    private boolean[] mVisited = new boolean[7];
    private Student mBasicInfo;
    private long mLastClickTime = 0;
    private ErrorStyle errorStyle;
    private Group mDataFoundGroup;
    private Button mQuickImportData;
    private ProgressBar mProgressBar;
    private boolean dialogState;
    private static final int TIMEOUT = 20000;


    public ProfileSetup2Activity() {
        // initializing the list for 7 days.
        mRoutines = new ArrayList<>();
        for (int i = 0; i < 7; i++) {
            mRoutines.add(new Routine(Common.GET_DAY_FROM_INDEX[i]));
        }

        initSpinnerItems();
    }

    private void initSpinnerItems() {
        mSpinnerItemsCourseType = new ArrayList<>();
        mSpinnerItemsCourseType.add(new ImageItem("Lecture", R.drawable.course_ic_type_lecture));
        mSpinnerItemsCourseType.add(new ImageItem("Practical", R.drawable.course_ic_type_practical));

        mCourseTypeSelected = "Lecture"; // default selected item
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profilesetup2);
        // firebase authentication
        mProgressBar = findViewById(R.id.profile_setup_2_progress_bar);

        mUser = FirebaseAuth.getInstance().getCurrentUser();
        Spinner courseType = findViewById(R.id.setup_course_type_spinner_id);
        mDataFoundGroup = findViewById(R.id.profile_setup_2_data_found_group);
        mQuickImportData = findViewById(R.id.button2);

        populateSpinner(courseType);
        if (isUserAuthenticated(mUser)) {

            Bundle bundle = getIntent().getExtras();
            if (bundle != null) {
                String university = bundle.getString("university");
                String department = bundle.getString("department");
                String currentSemester = bundle.getString("currentSemester");
                String section = bundle.getString("section");
                String group = bundle.getString("group");

                if (university != null && currentSemester != null && department != null && section != null && group != null) {
                    // if semester info exists for the particular semester and department, then import
                    // and notify user
                    Firebase.getDatabaseReference().
                            child("universities").
                            child(university).
                            child("semester-info").
                            child(currentSemester).
                            child(department).
                            child(section).
                            child(group).
                            addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    if (snapshot.exists()) {
                                        Log.d("DATA***", "data exist");
                                        importCourses(snapshot.child("courses"));
                                        importRoutine(snapshot.child("routine"));
                                        // after importing routine, notify user of available data
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });

                }

            }

            mCurrentSemesterResult = new Result("init");
            importBasicInfo(); // after importing basic info, initialize the views and listeners
        }

    }

    private void importRoutine(DataSnapshot snapshot) {

        mRoutines = new ArrayList<>();
        for (DataSnapshot snap : snapshot.getChildren()) {
            Routine routine = snap.getValue(Routine.class);
            if (routine != null) {
                mRoutines.add(routine);
            }

        }
        // notify user of available data
        mDataFoundGroup.setVisibility(View.VISIBLE);
        mQuickImportData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadData("Semester info imported!"); // upload data (the courses and routines) to database
            }
        });


    }

    private void importCourses(DataSnapshot snapshot) {
        for (DataSnapshot snap : snapshot.getChildren()) {
            Course course = snap.getValue(Course.class);
            if (course != null) {
                mCourses.add(course);
            }
        }
    }


    private void importBasicInfo() {
        Firebase.getDatabaseReference().
                child(Firebase.USERS).
                child(mUser.getUid()).
                child(Firebase.BASIC_INFO).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mBasicInfo = dataSnapshot.getValue(Student.class);
                initViews();
                attachListeners();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void populateSpinner(Spinner courseTypeSpinner) {
        SpinnerAdapter adapter = new SpinnerAdapter(getApplicationContext(), mSpinnerItemsCourseType, ProfileSetup2Activity.class.getSimpleName());
        courseTypeSpinner.setAdapter(adapter);
        handleSpinnerEvents(courseTypeSpinner);

    }

    private void handleSpinnerEvents(Spinner courseTypeSpinner) {
        courseTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ImageItem clickedItem = (ImageItem) parent.getItemAtPosition(position);
                mCourseTypeSelected = clickedItem.getName();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
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

    private void attachListeners() {
        mAddLater.setOnClickListener(this);
        mAddRoutine.setOnClickListener(this);
        mAddCourse.setOnClickListener(this);
    }

    private void initViews() {
        errorStyle = ErrorStyle.getInstance(getApplicationContext());
        mCourseNameField = findViewById(R.id.setup_course_name_input_id);
        mCourseCodeField = findViewById(R.id.setup_course_code_input_id);
        mCourseCreditField = findViewById(R.id.setup_course_credit_name_id);
        mAddRoutine = findViewById(R.id.setup_add_routine_button_id);
        mAddCourse = findViewById(R.id.setup_add_course_button_id);
        mAddLater = findViewById(R.id.setup_add_later_button_id);

    }


    /*
     * after all successful entries, clicking it will add the course the main list
     */
    public void addCourse() {

        if (isInputValid()) {
            CourseAddedPromptDialog courseAddedPromptDialog = new CourseAddedPromptDialog();
            courseAddedPromptDialog.show(getSupportFragmentManager(), "ProfileSetup2");

            ArrayList<String> days = addRoutine();
            // [START] current_semester_input
            // add the course code to the current semester result's field
            // A+ is the default
            mCurrentSemesterResult.getGradesObtained().put(mCourseCodeField.getText().toString(), "A+");
            // increment courses taken
            int courses = mCurrentSemesterResult.getCoursesTaken();
            courses += 1;
            mCurrentSemesterResult.setCoursesTaken(courses);
            double credits = mCurrentSemesterResult.getTotalCredits();
            credits += Double.parseDouble(mCourseCreditField.getText().toString());
            mCurrentSemesterResult.setTotalCredits(credits);
            // [STOP] current_semester_input

            mCourses.add(new Course(
                    mCourseNameField.getText().toString(),
                    mCourseCodeField.getText().toString(),
                    Double.parseDouble(mCourseCreditField.getText().toString()),
                    days,
                    mCourseTypeSelected));

            // resetting all fields after adding a course
            mCourseNameField.setText("");
            mCourseCodeField.setText("");
            mCourseCreditField.setText("");
            mSchedules.clear();

            // reset all visited values
            Arrays.fill(mVisited, false);

        }


    }

    @Override
    public void onAttachFragment(@NonNull Fragment fragment) {
        if (fragment instanceof CourseAddedPromptDialog) {
            CourseAddedPromptDialog headlinesFragment = (CourseAddedPromptDialog) fragment;
            headlinesFragment.setOnButtonClickListener(this);
        }
    }

    @Override
    public void onAddMoreClick() {
        newEntry = true;

    }

    @Override
    public void onContinueClick() {
        uploadData("Profile Setup Complete!"); // upload data (the courses and routines) to database
        newEntry = false;
    }

    /*
     * returns the number of days a particular course is held
     * populates the routine with the schedules. Ie: a course is held on sun and mon.
     * the loop iterates and add the particular schedules on that days
     */
    private ArrayList<String> addRoutine() {
        ArrayList<String> days = new ArrayList<>();
        for (int i = 0; i < mSchedules.size(); i++) {
            Integer dayIndex = Common.GET_INDEX_FROM_DAY.get(mSchedules.get(i).getName());
            if (dayIndex != null) {
                //  mRoutines.get(dayIndex).getItems().add(new Schedule(course, startTime, endTime));
                mSchedules.get(i).setType(mCourseTypeSelected); // sets the course type to the course schedule
                mSchedules.get(i).setDescription(mCourseNameField.getText().toString()); // sets the course name to the course schedule
                mSchedules.get(i).setName(mCourseCodeField.getText().toString());
                mRoutines.get(dayIndex).getItems().add(mSchedules.get(i));
                days.add(Common.GET_DAY_FROM_INDEX_FULL[i]);
            } else {
                Common.showExceptionPrompt(getBaseContext(), "Bug: Day index null");
            }
        }
        return days;
    }


    /*
     * uploads data to database. If done successfully, then continue to the Dashboard Activity
     */
    private void uploadData(final String status) {

        startLoadingAnimation();

        if (!status.equals("Semester info imported!")) {
            // sort the items for the particular day
            final DateFormat dateFormat = new SimpleDateFormat("hh:mma", Locale.US);
            for (int i = 0; i < mRoutines.size(); i++) {
                if (mRoutines.get(i).getItems().size() != 0) {
                    Collections.sort(mRoutines.get(i).getItems(), new Comparator<Schedule>() {
                        @Override
                        public int compare(Schedule first, Schedule second) {
                            Date date1 = null;
                            Date date2 = null;
                            try {
                                date1 = dateFormat.parse(first.getStartTime());
                                date2 = dateFormat.parse(second.getStartTime());
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                            assert date1 != null;
                            return date1.compareTo(date2);
                        }
                    });
                }

            }
        }


        // saving an instance of todoTasks to the local storage
        TodoTasks todoTasks = new TodoTasks("init");
        saveTodoTasks(todoTasks, mUser.getUid());

        // upload gradeWeights
        ArrayList<Weight> weights = initGradeWeights();
        HashMap<String, Object> weightChildUpdates = new HashMap<>();
        weightChildUpdates.put("list", weights);
        weightChildUpdates.put("updated", false);


        // upload SemesterResultList
        ArrayList<Result> results = initResults();

        // uploading courses, routine to database
        HashMap<String, Object> childUpdates = new HashMap<>();
        childUpdates.put(Common.COURSES, mCourses);
        childUpdates.put(Firebase.RESULTS, results);
        childUpdates.put(Firebase.WEIGHTS, weightChildUpdates);
        childUpdates.put(Common.ROUTINE, mRoutines);
        childUpdates.put(Firebase.CGPA, 0.0);
        //  childUpdates.put(Firebase.TO DO, todoTasks);

        Firebase.getDatabaseReference().
                child(Firebase.USERS).
                child(mUser.getUid()).
                updateChildren(childUpdates).
                addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(context, status, Toast.LENGTH_SHORT).show();

                        stopLoadingAnimation();
                        // adding flags to intent so that in the new activity, pressing back button doesn't bring this activity back
                        Intent intent = new Intent(ProfileSetup2Activity.this, DashboardActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);

                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                stopLoadingAnimation();
                Toast.makeText(context, "Something's Wrong!", Toast.LENGTH_SHORT).show();
                e.printStackTrace();


            }
        });

        // the following lines are for debugging.
        // populateContentForDebug();


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
                    Toast toast = Toast.makeText(ProfileSetup2Activity.this, "Connection timeout.", Toast.LENGTH_SHORT);
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
        mAddCourse.setEnabled(true);
        mAddLater.setEnabled(true);
    }


    private ArrayList<Result> initResults() {
        ArrayList<Result> results = new ArrayList<>();
        for (int i = 0; i < mBasicInfo.getCurrentSemester() - 1; i++) {
            results.add(new Result("init"));
        }
        results.add(mCurrentSemesterResult);

        return results;
    }

    private ArrayList<Weight> initGradeWeights() {
        ArrayList<Weight> weightList = new ArrayList<>();
        weightList.add(new Weight("A+", 4.0));
        weightList.add(new Weight("A", 3.75));
        weightList.add(new Weight("A-", 3.5));
        weightList.add(new Weight("B+", 3.25));
        weightList.add(new Weight("B", 3));
        weightList.add(new Weight("B-", 2.75));
        weightList.add(new Weight("C+", 2.5));
        weightList.add(new Weight("C", 2.25));
        weightList.add(new Weight("C-", 0));
        weightList.add(new Weight("D+", 0));
        weightList.add(new Weight("D", 2));
        weightList.add(new Weight("D-", 0));
        weightList.add(new Weight("F", 0));
        return weightList;
    }


    /*
     * saves the instance of TodoTasks to the local storage with filename FILENAME_TODO_TASKS + UID
     * @param todoTasks the instance of TodoTasks
     * @returns: an instance of TodoTasks that's saved in the local storage with file name FILENAME_TODO_TASKS
     */
    private void saveTodoTasks(TodoTasks todoTasks, String uid) {
        String json;
        String fileName;

        Gson gson = new Gson();
        json = gson.toJson(todoTasks);
        String FILENAME_TODO_TASKS = "todoTasks";
        fileName = FILENAME_TODO_TASKS + uid;
        FileOutputStream fileOutputStream;
        try {
            fileOutputStream = openFileOutput(fileName, MODE_PRIVATE);
            fileOutputStream.write(json.getBytes());
            //System.out.println("file dir: " + getFilesDir() + "/");
            fileOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    /*
     * checks if all input is valid and at least 1 course has been entered
     */
    public boolean isInputValid() {
        boolean mReturnValue = true;
        if (mCourseNameField.getText().length() == 0) {
            errorStyle.setError(getString(R.string.Error_EmptyField), mCourseNameField);
            mReturnValue = false;
        } else {
            errorStyle.resetError(mCourseNameField);
        }

        if (mCourseCodeField.getText().length() == 0) {
            errorStyle.setError(getString(R.string.Error_EmptyField), mCourseCodeField);
            mReturnValue = false;
        } else {
            errorStyle.resetError(mCourseCodeField);
        }

        if (mCourseCreditField.getText().length() == 0) {
            errorStyle.setError(getString(R.string.Error_EmptyField), mCourseCreditField);
            mReturnValue = false;
        } else {
            errorStyle.resetError(mCourseCreditField);
        }

        try {
            Double credit = Double.parseDouble(mCourseCreditField.getText().toString().trim());
            errorStyle.resetError(mCourseCreditField);
        } catch (Exception ex) {
            errorStyle.setError(getString(R.string.Error_InvalidConversion), mCourseCreditField);
            return false;
        }

        if (mSchedules.size() == 0) {
            Toast.makeText(context, "No Routine Added!", Toast.LENGTH_SHORT).show();
            mReturnValue = false;
        }

        return mReturnValue;
    }


    @Override
    public void onClick(View v) {
        int BUTTON_DELAY_TIME = 1000;
        if (SystemClock.elapsedRealtime() - mLastClickTime < BUTTON_DELAY_TIME) {
            return;
        }
        mLastClickTime = SystemClock.elapsedRealtime();

        if (v.getId() == mAddCourse.getId()) {
            addCourse();

        } else if (v.getId() == mAddRoutine.getId()) {
            // opens the dialog where user can add the schedules for their courses
            SetupAddRoutineDialog dialog = new SetupAddRoutineDialog(mSchedules, mVisited);
            dialog.show(getSupportFragmentManager(), "profileSetupActivity");

        } else if (v.getId() == mAddLater.getId()) {
            uploadData("Procrastination, eh?");
            mAddLater.setEnabled(false);
        }

    }


}
