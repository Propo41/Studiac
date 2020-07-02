package com.valhalla.studiac.activities.setup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

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

public class ProfileSetup2Activity extends AppCompatActivity implements View.OnClickListener {

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
    private ProgressBar mProgressBar;



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
        mUser = FirebaseAuth.getInstance().getCurrentUser();
        Spinner courseType = findViewById(R.id.setup_course_type_spinner_id);
        populateSpinner(courseType);
        if (isUserAuthenticated(mUser)) {
            mCurrentSemesterResult = new Result("init");
            importBasicInfo(); // after importing basic info, initialize the views and listeners
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
        mCourseNameField = findViewById(R.id.setup_course_name_input_id);
        mCourseCodeField = findViewById(R.id.setup_course_code_input_id);
        mCourseCreditField = findViewById(R.id.setup_course_credit_name_id);
        mAddRoutine = findViewById(R.id.setup_add_routine_button_id);
        mAddCourse = findViewById(R.id.setup_add_course_button_id);
        mAddLater = findViewById(R.id.setup_add_later_button_id);
        mProgressBar = findViewById(R.id.profile_setup_2_progress_bar);

    }



    public void startLoadingAnimation() {
        mProgressBar.setVisibility(View.VISIBLE);
    }


    public void stopLoadingAnimation() {
        mProgressBar.setVisibility(View.INVISIBLE);
    }

    /*
     * after all successful entries, clicking it will add the course the main list
     */
    public void addCourse() {
        startLoadingAnimation();

        if (isInputValid()) {
            openDialog();
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

    /*
     * a dialog that prompts user to either continue or add more courses
     */
    private void openDialog() {
        final Dialog courseAddedDialog = new Dialog(context);
        courseAddedDialog.setContentView(R.layout.dialog_setupprofile2_prompt_added);
        Button addMoreBv = courseAddedDialog.findViewById(R.id.setup_dialog_course_added_add_id);
        Button continueBv = courseAddedDialog.findViewById(R.id.setup_dialog_course_added_continue_id);

        // when add more button is clicked, dismiss the dialog
        // and add the course with its schedule to the list mRoutine
        addMoreBv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v0) {
                newEntry = true;
                stopLoadingAnimation();
                courseAddedDialog.dismiss();
            }
        });

        // when continue button is clicked, open dashboard and register the user
        continueBv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v0) {
                uploadData("Profile Setup Complete!"); // upload data (the courses and routines) to database
                courseAddedDialog.dismiss();
                newEntry = false;


            }
        });

        courseAddedDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        courseAddedDialog.show();
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
                        mAddCourse.setEnabled(true);
                        mAddLater.setEnabled(true);

                        // adding flags to intent so that in the new activity, pressing back button doesn't bring this activity back
                        Intent intent = new Intent(ProfileSetup2Activity.this, DashboardActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);

                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(context, "Something's Wrong!", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
                mAddCourse.setEnabled(true);
                mAddLater.setEnabled(true);
                stopLoadingAnimation();

            }
        });

        // the following lines are for debugging.
        // populateContentForDebug();


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

    private void populateContentForDebug() {
        Routine routine;
        ArrayList<Routine> routines = new ArrayList<>();
        routine = new Routine("sun"); // sun
        routine.getItems().add(new Schedule("CSE 2201", "15:30", "16:20"));
        routine.getItems().add(new Schedule("MATH 2203", "16:20", "17:10"));
        routine.getItems().add(new Schedule("CSE 2208", "13:00", "15:30"));
        routine.getItems().add(new Schedule("CSE 2209", "17:10", "18:00"));
        routines.add(routine);

        routine = new Routine("mon"); // mon
        routine.getItems().add(new Schedule("CSE 2213", "09:40", "10:30"));
        routine.getItems().add(new Schedule("CSE 2201", "08:00", "08:50"));
        routine.getItems().add(new Schedule("CSE 2207", "08:50", "09:40"));
        routines.add(routine);

        routine = new Routine("tues"); // tues
        routine.getItems().add(new Schedule("CSE 2207", "11:20", "12:10"));
        routine.getItems().add(new Schedule("CSE 2200", "08:00", "10:30"));
        routine.getItems().add(new Schedule("MATH 2203", "12:10", "13:00"));
        routine.getItems().add(new Schedule("CSE 2209", "10:30", "11:20"));

        routines.add(routine);

        routine = new Routine("wed"); // wed
        routine.getItems().add(new Schedule("CSE 2213", "13:00", "13:50"));
        routine.getItems().add(new Schedule("CSE 2209", "13:50", "14:40"));
        routine.getItems().add(new Schedule("CSE 2214", "10:30", "13:00"));
        routine.getItems().add(new Schedule("MATH 2203", "14:40", "15:30"));
        routines.add(routine);

        routine = new Routine("thurs"); // thurs
        routine.getItems().add(new Schedule("CSE 2207", "09:40", "10:30"));
        routine.getItems().add(new Schedule("CSE 2210", "10:30", "13:00"));
        routine.getItems().add(new Schedule("CSE 2201", "08:00", "08:50"));
        routine.getItems().add(new Schedule("CSE 2213", "08:50", "09:40"));
        routines.add(routine);

        routine = new Routine("fri"); // fri
        routines.add(routine);

        routine = new Routine("sat"); // sat
        routines.add(routine);

        for (int i = 0; i < routines.size(); i++) {
            // sort the items for the particular day
            Collections.sort(routines.get(i).getItems(), new Comparator<Schedule>() {
                @Override
                public int compare(Schedule first, Schedule second) {
                    return first.getStartTime().compareTo(second.getStartTime());
                }
            });

            // after sorting it out, convert the values to 12hr format and save it
            changeTo12hrFormat(routines.get(i).getItems());
        }

        ArrayList<Course> courses = new ArrayList<>();

        ArrayList<String> str = new ArrayList<>();
        str.add("Sunday");
        str.add("Tuesday");
        str.add("Wednesday");
        courses.add(new Course("Mathematics", "MATH 2203", 3.0, str));

        str = new ArrayList<>();
        str.add("Tuesday");
        courses.add(new Course("Software Development-III", "CSE 2200", 0.75, str));

        str = new ArrayList<>();
        str.add("Thursday");
        str.add("Sunday");
        str.add("Monday");
        courses.add(new Course("Numerical Methods", "CSE 2201", 3.0, str));

        str = new ArrayList<>();
        str.add("Monday");
        str.add("Tuesday");
        str.add("Thursday");
        courses.add(new Course("Algorithms", "CSE 2207", 3.0, str));

        str = new ArrayList<>();
        str.add("Sunday");
        courses.add(new Course("Algorithms Lab", "CSE 2208", 1.5, str));

        str = new ArrayList<>();
        str.add("Thursday");
        courses.add(new Course("Digital Electronics and Pulse Techniques Lab", "CSE 2210", 0.75, str));

        str = new ArrayList<>();
        str.add("Sunday");
        str.add("Tuesday");
        str.add("Wednesday");
        courses.add(new Course("Digital Electronics and Pulse Techniques", "CSE 2209", 3.0, str));

        str = new ArrayList<>();
        str.add("Monday");
        str.add("Wednesday");
        str.add("Thursday");
        courses.add(new Course("Computer Architecture", "CSE 2213", 3.0, str));

        str = new ArrayList<>();
        str.add("Tuesday");
        courses.add(new Course("Assembly Language Programing", "CSE 2214", 1.5, str));

        TodoTasks todoTasks = new TodoTasks("init");

        Student student = new Student("Ahnaf",
                setSelectedImageRes(),
                "Ahsanullah University of Science & Technology",
                "CSE", 8, 3, 0.0);

        HashMap<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("courses", courses);
        childUpdates.put("routine", routines);
        childUpdates.put("todoTasks", todoTasks);
        childUpdates.put("basicInfo", student); // for debug only. DELETE IT MUST

        Firebase.getDatabaseReference().
                child(Firebase.USERS).
                child(mUser.getUid()).
                updateChildren(childUpdates).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(context, "Profile Setup Complete!", Toast.LENGTH_SHORT).show();
            }
        });
    }


    // for debug only. don't use it here
    private void changeTo12hrFormat(ArrayList<Schedule> items) {
        for (Schedule item : items) {
            item.setStartTime(Common.convertTo12hFormat(item.getStartTime()));
            item.setEndTime(Common.convertTo12hFormat(item.getEndTime()));
        }
    }

    // for debug only. don't use it here
    private String setSelectedImageRes() {
        Resources resources = getResources();
        return resources.getResourceEntryName(R.drawable.profile_picture_eight);
    }


    /*
     * checks if all input is valid and at least 1 course has been entered
     */
    public boolean isInputValid() {
        boolean mReturnValue = true;
        // todo: add stroke and more functionality, such as when texts are too long, or etc
        if (mCourseNameField.getText().length() == 0) {
            mCourseNameField.setError("This field cannot be empty");
            mReturnValue = false;
        }

        if (mCourseCodeField.getText().length() == 0) {
            mCourseCodeField.setError("This field cannot be empty");
            mReturnValue = false;
        }

        if (mCourseCreditField.getText().length() == 0) {
            mCourseCreditField.setError("This field cannot be empty");
            mReturnValue = false;
        }

        if (mSchedules.size() == 0) {
            Toast.makeText(context, "No Schedule Added!", Toast.LENGTH_SHORT).show();
            mReturnValue = false;
        }

        Toast.makeText(context, "valid: " + mReturnValue, Toast.LENGTH_SHORT).show();
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
            Toast.makeText(context, "add course clicked", Toast.LENGTH_SHORT).show();

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
