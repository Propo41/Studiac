package com.valhalla.studiac.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.valhalla.studiac.R;
import com.valhalla.studiac.activities.DashboardActivity;
import com.valhalla.studiac.database.Firebase;
import com.valhalla.studiac.fragments.dialogs.SetupAddRoutineDialog;
import com.valhalla.studiac.models.Course;
import com.valhalla.studiac.models.Routine;
import com.valhalla.studiac.models.Schedule;
import com.valhalla.studiac.models.Student;
import com.valhalla.studiac.models.TodoTasks;
import com.valhalla.studiac.utility.Common;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

public class ProfileSetup2Activity extends AppCompatActivity {

    private EditText mCourseNameField;
    private EditText mCourseCodeField;
    private EditText mCourseCreditField;
    private FloatingActionButton mAddRoutine;
    private Button mAddCourse;
    private TextView mAddLater;
    private int mTotalCourses = 0;
    final Context context = this;
    private ArrayList<Schedule> mSchedules = new ArrayList<>();
    private ArrayList<Routine> mRoutines;
    private boolean newEntry;
    private ArrayList<Course> mCourses = new ArrayList<>();

    public ProfileSetup2Activity() {

        mRoutines = new ArrayList<>();
        mRoutines.add(new Routine("sun"));
        mRoutines.add(new Routine("mon"));
        mRoutines.add(new Routine("tues"));
        mRoutines.add(new Routine("wed"));
        mRoutines.add(new Routine("thurs"));
        mRoutines.add(new Routine("fri"));
        mRoutines.add(new Routine("sat"));

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profilesetup2);
        initViews();
    }

    private void initViews() {
        mCourseNameField = findViewById(R.id.setup_course_code_name_id);
        mCourseCodeField = findViewById(R.id.setup_course_code_input_id);
        mCourseCreditField = findViewById(R.id.setup_course_credit_input_id);
        mAddRoutine = findViewById(R.id.setup_add_routine_button_id);
        mAddCourse = findViewById(R.id.setup_add_course_button_id);
        mAddLater = findViewById(R.id.setup_add_later_button_id);
    }

    /*
     * opens the dialog where user can add the schedules for their courses
     */
    public void onRoutineClick(View v) {
        SetupAddRoutineDialog dialog = new SetupAddRoutineDialog(mSchedules, newEntry);
        dialog.show(getSupportFragmentManager(), "profileSetupActivity");
    }

    public void onAddCourseClick(View v) {

        if (isInputValid()) {
            mTotalCourses += 1;
            openDialog();
            ArrayList<String> days = addRoutine();

            mCourses.add(new Course(
                    mCourseNameField.getText().toString(),
                    mCourseCodeField.getText().toString(),
                    Double.parseDouble(mCourseCreditField.getText().toString()),
                    days));
            mCourseNameField.setText("");
            mCourseCodeField.setText("");
            mCourseCreditField.setText("");
            mSchedules.clear();

        }


    }

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
                courseAddedDialog.dismiss();
            }
        });

        // when continue button is clicked, open dashboard and register the user
        continueBv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v0) {
                registerStudent();
                courseAddedDialog.dismiss();
                newEntry = false;
                startActivity(new Intent(ProfileSetup2Activity.this, DashboardActivity.class));

            }
        });

        courseAddedDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        courseAddedDialog.show();
    }

    private ArrayList<String> addRoutine() {

        ArrayList<String> days = new ArrayList<>();
        for (int i = 0; i < mSchedules.size(); i++) {
            Integer dayIndex = Common.GET_INDEX_FROM_DAY.get(mSchedules.get(i).getName());

            if (dayIndex != null) {
                //  mRoutines.get(dayIndex).getItems().add(new Schedule(course, startTime, endTime));
                mRoutines.get(dayIndex).getItems().add(mSchedules.get(i));
                days.add(Common.GET_DAY_FROM_INDEX_FULL[i]);
            } else {
                Common.showExceptionPrompt(getBaseContext(), "Bug: Day index null");
            }
        }
        return days;
    }


    private void registerStudent() {

     /*

     // sort the routine object
        // iterate through the list of Routines and then sort the inner list
          *************************************************************
        // todo: use this sort instead of the one down below this. Use the following sorting technique. It requires the dates to be in 12hr format
         final DateFormat dateFormat = new SimpleDateFormat("hh:mma", Locale.US);
            // sort the items for the particular day
            Collections.sort(mRoutines.get(dayIndex).getItems(), new Comparator<Schedule>() {
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
                    return date1.compareTo(date2);
                }
            });

           *************************************************************

        for (int i = 0; i < mRoutines.size(); i++) {
            // sort the items for the particular day
            Collections.sort(mRoutines.get(i).getItems(), new Comparator<Schedule>() {
                @Override
                public int compare(Schedule first, Schedule second) {
                    return first.getStartTime().compareTo(second.getStartTime());
                }
            });
        }


         Bundle bundle = getIntent().getExtras();
      University university = new University(
                bundle.getString("universityName"),
                bundle.getString("departmentName"),
                bundle.getInt("currentSemester"),
                bundle.getInt("totalSemesters"),
                mRoutines,
                mCourses);

        Student student = new Student(
                bundle.getString("studentName"),
                bundle.getString("studentEmail"),
                bundle.getString("studentPassword"),
                bundle.getInt("studentImage"),
                university
        );
*/


        // the following lines are for debugging.
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

        TodoTasks todoTasks = new TodoTasks();

        Student student = new Student("Ahnaf",
                setSelectedImageRes(R.drawable.profile_picture_eight),
                "Ahsanullah University of Science & Technology",
                "CSE", 8, 3);

        // uid used here is just for debug. use actual uid after attaching authentication
        Firebase.writeObject(student, Firebase.STUDENT, "student");
        Firebase.writeObject(todoTasks, Firebase.TODO_TASKS, "student");
        Firebase.writeListCourse(courses, "student");
        Firebase.writeListRoutine(routines, "student");

        Log.i("UPLOAD STATUS", "UPLOAD SUCCESS");

    }


    private void changeTo12hrFormat(ArrayList<Schedule> items) {
        for (Schedule item : items) {
            item.setStartTime(Common.convertTo12hFormat(item.getStartTime()));
            item.setEndTime(Common.convertTo12hFormat(item.getEndTime()));
        }
    }

    // for debug only. dont use it here
    private String setSelectedImageRes(int imageDrawable) {
        Resources resources = getResources();
        return resources.getResourceEntryName(imageDrawable);
    }


    /*
     * checks if all input is valid and at least 1 course has been entered
     */
    public boolean isInputValid() {
        boolean mReturnValue = true;
        // todo: add stroke and more functionality, such as when texts are too long, or etc
        if (mTotalCourses < 1) {
            mReturnValue = false;
        }
        if (mCourseNameField.getText().length() == 0) {
            mCourseNameField.setError("This field cannot be empty");
            mReturnValue = false;
        }


        if (mCourseCodeField.getText().length() == 0 && mReturnValue) {
            mCourseCodeField.setError("This field cannot be empty");
            mReturnValue = false;
        }

        if (mCourseCreditField.getText().length() == 0 && mReturnValue) {
            mCourseCreditField.setError("This field cannot be empty");
            mReturnValue = false;
        }
        mReturnValue = true; // for debug
        return mReturnValue;
    }


}
