package com.valhalla.studiac.activities;


import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.Group;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.valhalla.studiac.R;
import com.valhalla.studiac.adapters.dashboard.ViewCoursesRecycleAdapter;
import com.valhalla.studiac.database.Firebase;
import com.valhalla.studiac.fragments.dialogs.AddCourseBottomSheetDialog;
import com.valhalla.studiac.fragments.dialogs.CourseDetailsDialog;
import com.valhalla.studiac.fragments.dialogs.CourseEditBottomSheetDialog;
import com.valhalla.studiac.models.Course;
import com.valhalla.studiac.models.Result;
import com.valhalla.studiac.models.Routine;
import com.valhalla.studiac.models.Schedule;
import com.valhalla.studiac.models.Student;
import com.valhalla.studiac.toolbars.NavigationToolbarWhite;
import com.valhalla.studiac.utility.Common;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;


public class ViewCoursesActivity extends NavigationToolbarWhite implements AddCourseBottomSheetDialog.BottomSheetListener {


    private ArrayList<Course> mCourses;
    private ViewCoursesRecycleAdapter mAdapter;
    private ArrayList<Routine> mRoutines;
    private Set<Integer> newRoutineIndexes = new HashSet<>();
    private Group mPandaGroup;
    private Result mCurrentSemesterResult;
    private FirebaseUser mUser;
    private Student mBasicInfo;
    private final static String TAG = "ViewCoursesActivity";

    // checks if the data is changed. if changed, uploads to database
    private boolean mDataChanged;
    private boolean mCourseAdded;
    private ShimmerFrameLayout mShimmerViewContainer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContent(R.layout.activity_viewcourses);
        //[START] firebase_authentication_instance
        mUser = FirebaseAuth.getInstance().getCurrentUser();
        //[END] firebase_authentication_instance


        if (isUserAuthenticated(mUser)) {
            // [START] shimmer_effect
            mShimmerViewContainer = findViewById(R.id.bulletin_shimmer_view_container);
            mPandaGroup = findViewById(R.id.view_courses_panda_group);
            mShimmerViewContainer.startShimmerAnimation();
            importData();

        }

    }

    /**
     * @imports: currentSemester: Result
     * @imports: courseList: List<Course>
     * @imports: basicInfo: Student
     */
    private void importData() {
        Firebase.getDatabaseReference().
                child(Firebase.USERS).
                child(mUser.getUid()).
                child(Firebase.BASIC_INFO).
                addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        mBasicInfo = dataSnapshot.getValue(Student.class);
                        // import the current semester Result instance
                        Firebase.getDatabaseReference().
                                child(Firebase.USERS).
                                child(mUser.getUid()).
                                child(Firebase.RESULTS).
                                child(mBasicInfo.getCurrentSemester() - 1 + "").
                                addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        mCurrentSemesterResult = dataSnapshot.getValue(Result.class);
                                        if (mCurrentSemesterResult != null) {
                                            if (mCurrentSemesterResult.getGradesObtained() == null) {
                                                mCurrentSemesterResult.setGradesObtained(new HashMap<String, String>());
                                            }
                                            importCourses();
                                        } else {
                                            Log.d("BUG", "error creating profile in database.");
                                            Toast.makeText(ViewCoursesActivity.this, "Profile Setup Error.", Toast.LENGTH_SHORT).show();
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


    /*
     * imports the list of courses from db.
     */
    private void importCourses() {
        Firebase.getDatabaseReference().
                child(Firebase.USERS).
                child(mUser.getUid()).
                child(Common.COURSES).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mCourses = new ArrayList<>();
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    mCourses.add(data.getValue(Course.class));
                }

                // [END] shimmer_effect
                mShimmerViewContainer.stopShimmerAnimation();
                mShimmerViewContainer.setVisibility(View.GONE);

                // initAnimation();
                if (mCourses.size() == 0) {

                    mPandaGroup.setVisibility(View.VISIBLE);
                    mRoutines = new ArrayList<>();
                    // initializing the list up to 7 days to avoid importing the data
                    for (int i = 0; i < 7; i++) {
                        mRoutines.add(new Routine(Common.GET_DAY_FROM_INDEX[i]));
                    }
                    // startThrobbingEffect();
                } else {
                    importRoutine();
                    mPandaGroup.setVisibility(View.GONE);

                    // stopThrobbingEffect();
                }
                setupList();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.i("DB ERROR", databaseError.getDetails());

            }

        });
    }

    /*
     * the list of routine will be imported from db in the background
     */
    private void importRoutine() {
        mRoutines = new ArrayList<>();
        Firebase.getDatabaseReference().
                child(Firebase.USERS).
                child(mUser.getUid()).
                child(Common.ROUTINE).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    mRoutines.add(data.getValue(Routine.class));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }


    private void setupList() {
        RecyclerView recyclerView = findViewById(R.id.view_courses_recycle_view_id);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);

        mAdapter = new ViewCoursesRecycleAdapter(getApplicationContext(), mCourses);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(mAdapter);
        handleUserEvents();

    }

    /**
     * handles recycler item events
     *
     * @events: edit course
     * @events: select course details
     * @events: delete the course
     */
    private void handleUserEvents() {

        mAdapter.setOnItemClickListener(new ViewCoursesRecycleAdapter.OnItemClickListener() {
            @Override
            public void onButtonClick(final int position) {
                // edit course event
                CourseEditBottomSheetDialog dialog = new CourseEditBottomSheetDialog(mCourses.get(position));
                dialog.show(getSupportFragmentManager(), "viewCoursesActivity");

                dialog.setOnUpdateClickListener(new CourseEditBottomSheetDialog.OnUpdateClickListener() {
                    @Override
                    public void onUpdateClick(boolean codeChanged, String oldCode) {
                        //updates the course details. Since, Im already passing the course object in the constructor,
                        // the data will be updated from there without needing to return anything
                        mAdapter.notifyItemChanged(position);
                        if (codeChanged) {
                            // if the course code is changed, update it in the semester result map as well
                            String grade = mCurrentSemesterResult.getGradesObtained().get(oldCode);
                            mCurrentSemesterResult.getGradesObtained().remove(oldCode);
                            mCurrentSemesterResult.getGradesObtained().put(mCourses.get(position).getCode(), grade);

                            // update the course code in the routine as well
                            for (String day : mCourses.get(position).getDays()) {
                                Integer index = Common.GET_INDEX_FROM_DAY.get(day);
                                if (index != null) {
                                    Routine routine = mRoutines.get(index);
                                    ArrayList<Schedule> schedules = routine.getItems();
                                    if (schedules != null) {
                                        for (Schedule schedule : schedules) {
                                            if (schedule.getName().equals(oldCode)) {
                                                schedule.setName(mCourses.get(position).getCode());
                                            }
                                        }
                                    }
                                }

                            }

                        }

                        mDataChanged = true;


                    }

                    @Override
                    public void onDeleteClick() {
                        // delete the course from current semester result
                        mCurrentSemesterResult.getGradesObtained().remove(mCourses.get(position).getCode());
                        // reduce number of courses taken
                        int coursesTaken = mCurrentSemesterResult.getCoursesTaken();
                        if (coursesTaken != 0) {
                            mCurrentSemesterResult.setCoursesTaken(coursesTaken - 1);
                        }

                        // reduce the credits
                        double credits = mCurrentSemesterResult.getTotalCredits();
                        mCurrentSemesterResult.setTotalCredits(credits - mCourses.get(position).getCredit());
                        removeRoutine(mCourses.get(position));
                        mCourses.remove(position);
                        mAdapter.notifyItemRemoved(position);
                        if (mCourses.size() == 0) {
                            mPandaGroup.setVisibility(View.VISIBLE);
                            //  startThrobbingEffect();
                        }

                        mDataChanged = true;
                    }
                });

            }

            @Override
            public void onItemClick(int position) {
                Course course = mCourses.get(position);
                CourseDetailsDialog dialog = new CourseDetailsDialog(course);
                dialog.show(getSupportFragmentManager(), "viewCoursesActivity");

            }
        });
    }

    private void removeRoutine(Course course) {
        for (String day : course.getDays()) {
            Integer index = Common.GET_INDEX_FROM_DAY.get(day);
            if (index != null) {
                Routine routine = mRoutines.get(index);
                ArrayList<Schedule> schedules = routine.getItems();
                if (schedules != null) {
                    for (int i = 0; i < schedules.size(); i++) {
                        if (schedules.get(i) != null) {
                            Schedule schedule = schedules.get(i);
                            if (schedule != null) {
                                if (schedule.getName().equals(course.getCode())) {
                                    mRoutines.get(index).getItems().remove(i);
                                    break; // breaking the loop since each day there will be only one unique course
                                }
                            }
                        }
                    }
                }
            }

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.options_menu_course, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        } else if (item.getItemId() == R.id.more_delete_id) {
            mCourses.clear();
            mRoutines.clear();
            // initializing the list for 7 days.
            for (int i = 0; i < 7; i++) {
                mRoutines.add(new Routine(Common.GET_DAY_FROM_INDEX[i]));
            }

            HashMap<String, Object> childUpdates = new HashMap<>();
            childUpdates.put(Common.COURSES, mCourses);
            childUpdates.put(Common.ROUTINE, mRoutines);

            FirebaseDatabase.getInstance().getReference().
                    child(Firebase.USERS).
                    child(mUser.getUid()).
                    updateChildren(childUpdates).
                    addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(ViewCoursesActivity.this, "Courses Deleted Successfully!", Toast.LENGTH_SHORT).show();

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    createToast("Data couldn't be uploaded. Try again later.");
                }
            });

            mAdapter.notifyDataSetChanged();
            return true;

        }
        return super.onOptionsItemSelected(item);
    }


    private void createToast(String message) {
        Toast toast = Toast.makeText(ViewCoursesActivity.this, message, Toast.LENGTH_SHORT);
        TextView v = toast.getView().findViewById(android.R.id.message);
        if (v != null) {
            v.setGravity(Gravity.CENTER);
        }
        toast.show();
    }

    /*
     * all the changes are made locally. When the user exits from the activity, the data is updated in the firebase
     * replacing the existing object.
     */
    @Override
    protected void onPause() {
        super.onPause();

        if (mDataChanged) {
            if (mCourseAdded) {
                // a sort is required only when a new course is added
                sortRoutine();
            }
            int currentSemester = mBasicInfo.getCurrentSemester() - 1;

            HashMap<String, Object> childUpdates = new HashMap<>();
            childUpdates.put(Common.COURSES, mCourses);
            childUpdates.put(Common.ROUTINE, mRoutines);
            childUpdates.put(Firebase.RESULTS + "/" + currentSemester, mCurrentSemesterResult);

            FirebaseDatabase.getInstance().getReference().
                    child(Firebase.USERS).
                    child(mUser.getUid()).
                    updateChildren(childUpdates).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    //  Toast.makeText(ViewCoursesActivity.this, "data uploaded successfully", Toast.LENGTH_SHORT).show();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    // Toast.makeText(ViewCoursesActivity.this, "data couldn't be uploaded", Toast.LENGTH_SHORT).show();

                }
            });

        }


    }

    /*
     * sort the list of routines at specific days (pointed by the set "newRoutineIndexes"
     * and then upload it in db
     */
    private void sortRoutine() {
        Log.d(TAG, "sorting routine");
        for (Integer dayIndex : newRoutineIndexes) {
            final DateFormat dateFormat = new SimpleDateFormat("hh:mma", Locale.US);
            // sort the items for the particular day
            ArrayList<Schedule> schedules = mRoutines.get(dayIndex).getItems();
            if (schedules != null) {
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
                        System.out.println(date1 + " " + date2);
                        assert date1 != null;
                        return date1.compareTo(date2);
                    }
                });
            }


        }

        System.out.println("sorted routine: " + mRoutines.toString());

    }

    /**
     * Adds a new course to the course list and the current semester's result map.
     * interface method
     *
     * @param course   the new course that is added from the bottom sheet dialog
     * @param schedule the time tables of the course
     */
    @Override
    public void onAddPressed(Course course, ArrayList<Schedule> schedule) {
        // if course with the same code already exists, then don't input anything
        if (!courseExists(course)) {
            mAdapter.notifyItemInserted(mCourses.size());
            mCourses.add(course);
            updateRoutine(course.getCode(), schedule);

            // [START] current_semester_input
            // adding default grade A+
            mCurrentSemesterResult.getGradesObtained().put(course.getCode(), "A+");
            // increment courses taken
            int courses = mCurrentSemesterResult.getCoursesTaken();
            courses += 1;
            mCurrentSemesterResult.setCoursesTaken(courses);
            mCurrentSemesterResult.setIsGpaCalculated(false);
            double credits = mCurrentSemesterResult.getTotalCredits();
            credits += course.getCredit();
            mCurrentSemesterResult.setTotalCredits(credits);
            // [STOP] current_semester_input

            mDataChanged = true;
            mCourseAdded = true;

            mPandaGroup.setVisibility(View.GONE);
            //stopThrobbingEffect();
        } else {
            Toast.makeText(this, "Course already exists!", Toast.LENGTH_SHORT).show();
        }

    }

    /*
     * returns true if a course with the same course code already exists
     * else returns false
     */
    private boolean courseExists(Course course) {
        for (Course course1 : mCourses) {
            if (course1.getCode().equals(course.getCode())) {
                return true;
            }
        }
        return false;
    }

    /*
     * iterates over the list of schedules and fetches the day.
     * the day is then converted into it's corresponding index number and then using that
     * a new schedule object is added at the end of the list of routine [not sorted]
     */
    private void updateRoutine(String courseCode, ArrayList<Schedule> schedule) {
        for (Schedule schedule1 : schedule) {
            Integer index = Common.GET_INDEX_FROM_DAY.get(schedule1.getName()); // o(1)
            newRoutineIndexes.add(index);
            if (index != null) {
                if (mRoutines.get(index).getItems() == null) {
                    mRoutines.get(index).setItems(new ArrayList<Schedule>());
                }
                Schedule schedule2 = new Schedule(
                        courseCode,
                        schedule1.getStartTime(),
                        schedule1.getEndTime(),
                        schedule1.getType(),
                        schedule1.getDescription());

                mRoutines.get(index).getItems().add(schedule2);
            }

        }
    }

    /*
     * the FAB at the bottom. Clicking it, opens up the bottom sheet dialog to add courses
     */
    public void onAddCourseClick(View view) {
        // open bottom sheet to add course
        AddCourseBottomSheetDialog dialog = new AddCourseBottomSheetDialog();
        dialog.show(getSupportFragmentManager(), "VIEW_COURSES_ACTIVITY");
        //  stopThrobbingEffect();

    }


}
