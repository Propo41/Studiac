package com.valhalla.studiac.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.valhalla.studiac.R;
import com.valhalla.studiac.adapters.dashboard.ViewCoursesRecycleAdapter;
import com.valhalla.studiac.database.Firebase;
import com.valhalla.studiac.database.FirebaseListener;
import com.valhalla.studiac.fragments.dialogs.AddCourseBottomSheetDialog;
import com.valhalla.studiac.fragments.dialogs.AddQuickTaskBottomSheetDialog;
import com.valhalla.studiac.fragments.dialogs.CourseDetailsDialog;
import com.valhalla.studiac.fragments.dialogs.CourseEditBottomSheetDialog;
import com.valhalla.studiac.fragments.dialogs.SetupAddRoutineDialog;
import com.valhalla.studiac.fragments.todo.CurrentTasksFragment;
import com.valhalla.studiac.toolbars.NavigationToolbarWhite;
import com.valhalla.studiac.utility.common.Common;
import com.valhalla.studiac.utility.common.Course;
import com.valhalla.studiac.utility.common.Routine;
import com.valhalla.studiac.utility.common.Schedule;

import org.w3c.dom.Text;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;


public class ViewCoursesActivity extends NavigationToolbarWhite implements AddCourseBottomSheetDialog.BottomSheetListener {

    private ImageView pandaImage;
    private TextView pandaText;
    private ArrayList<Course> mCourses;
    private ViewCoursesRecycleAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private Context context; // for debug
    private String mUserUid;
    private FirebaseListener mFirebaseListener;
    private RecyclerView mRecyclerView;
    private DatabaseReference mDatabaseReferenceCourse;
    private DatabaseReference mDatabaseReferenceRoutine;
    private ArrayList<Routine> mRoutines;
    private Set<Integer> newRoutineIndexes = new HashSet<>();
    private Runnable runnableAnim;
    private ImageView mImageAnim;
    private ImageView mImageAnim2;
    private Handler mHandler;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContent(R.layout.activity_viewcourses);
        super.childActivity = Common.VIEW_COURSES;
        context = this;
        pandaImage = findViewById(R.id.view_courses_panda_image_id);
        pandaText = findViewById(R.id.view_courses_panda_text_id);
        fetchDataFromIntent();
        mDatabaseReferenceCourse = Firebase.getDatabaseReference().child(mUserUid).child(Common.COURSES);
        mDatabaseReferenceRoutine = Firebase.getDatabaseReference().child(mUserUid).child(Common.ROUTINE);
        initAnimation();


        if (mCourses.size() == 0) {
            pandaImage.setVisibility(View.VISIBLE);
            pandaText.setVisibility(View.VISIBLE);
            startThrobbingEffect();
        } else {
            importRoutine();
            pandaImage.setVisibility(View.GONE);
            pandaText.setVisibility(View.GONE);
            setupList();
            stopThrobbingEffect();

        }


    }

    /*
     * the list of routine will be imported from db in the background
     */
    private void importRoutine() {
        mRoutines = new ArrayList<>();
        mDatabaseReferenceRoutine.addListenerForSingleValueEvent(new ValueEventListener() {
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

    private void initAnimation() {
        mHandler = new Handler();
        mImageAnim = findViewById(R.id.view_courses_imageAnim_id);
        mImageAnim2 = findViewById(R.id.view_courses_imageAnim2_id);

        runnableAnim = new Runnable() {
            @Override
            public void run() {
                float scaleAmount = 16f;
                Log.i("runnable", "running thread");
                mImageAnim.animate().scaleX(scaleAmount).scaleY(scaleAmount).alpha(0f).setDuration(1000).withEndAction(new Runnable() {
                    @Override
                    public void run() {
                        mImageAnim.setScaleX(1f);
                        mImageAnim.setScaleY(1f);
                        mImageAnim.setAlpha(1f);

                    }
                });

                mImageAnim2.animate().scaleX(scaleAmount).scaleY(scaleAmount).alpha(0f).setDuration(700).withEndAction(new Runnable() {
                    @Override
                    public void run() {
                        mImageAnim2.setScaleX(1f);
                        mImageAnim2.setScaleY(1f);
                        mImageAnim2.setAlpha(1f);

                    }
                });

                mHandler.postDelayed(runnableAnim, 1500);
            }
        };
    }

    private void startThrobbingEffect() {
        this.runnableAnim.run();
    }

    private void stopThrobbingEffect() {
        mHandler.removeCallbacks(runnableAnim);

    }


    private void fetchDataFromIntent() {
        // fetch the transferred list of courses from the parent activity
        Bundle bundle = getIntent().getExtras();
        assert bundle != null;
        mCourses = bundle.getParcelableArrayList("courses");
        mUserUid = bundle.getString(Firebase.UID);

    }

    private void setupList() {
        mRecyclerView = findViewById(R.id.view_courses_recycle_view_id);
        mLayoutManager = new LinearLayoutManager(this);

        mAdapter = new ViewCoursesRecycleAdapter(mCourses);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
        handleUserEvents();

    }


    private void handleUserEvents() {

        mAdapter.setOnItemClickListener(new ViewCoursesRecycleAdapter.OnItemClickListener() {
            @Override
            public void onButtonClick(final int position) {
                CourseEditBottomSheetDialog dialog = new CourseEditBottomSheetDialog(mCourses.get(position));
                dialog.show(getSupportFragmentManager(), "viewCoursesActivity");

                dialog.setOnUpdateClickListener(new CourseEditBottomSheetDialog.OnUpdateClickListener() {
                    @Override
                    public void onUpdateClick() {
                        //updates the course details. Since, Im already passing the course object in the constructor,
                        // the data will be updated from there without needing to return anything
                        mAdapter.notifyItemChanged(position);
                    }

                    @Override
                    public void onDeleteClick() {
                        removeRoutine(mCourses.get(position));
                        mCourses.remove(position);
                        mAdapter.notifyItemRemoved(position);
                        if (mCourses.size() == 0) {
                            pandaImage.setVisibility(View.VISIBLE);
                            pandaText.setVisibility(View.VISIBLE);
                            startThrobbingEffect();
                        }

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
            Routine routine = mRoutines.get(index.intValue());
            int i = 0;
            for (Schedule schedule : routine.getItems()) {
                if (schedule.getName().equals(course.getCode())) {
                    mRoutines.get(index.intValue()).getItems().remove(i);
                    break; // breaking the loop since each day there will be only one unique course
                }
                i++;
            }
        }
    }

    @Override
    public void onBackPressed() {
        Toast.makeText(getApplicationContext(), "backPressed", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList("courses", mCourses);
        intent.putExtras(bundle);
        setResult(Activity.RESULT_OK, intent);
        finish();
    }

    /*
     * all the changes are made locally. When the user exits from the activity, the data is updated in the firebase
     * replacing the existing object.
     */
    @Override
    protected void onPause() {
        super.onPause();
        mDatabaseReferenceCourse.setValue(mCourses);
        sortRoutine();
        mDatabaseReferenceRoutine.setValue(mRoutines);


    }

    /*
     * sort the list of routines at specific days (pointed by the set "newRoutineIndexes"
     * and then upload it in db
     */
    private void sortRoutine() {
        for(Integer dayIndex : newRoutineIndexes){

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

        }
    }

    /*
     * when the add button is pressed from the bottom sheet dialog
     */
    @Override
    public void onAddPressed(Course course, ArrayList<Schedule> schedule) {
        mAdapter.notifyItemInserted(mCourses.size());
        mCourses.add(course);
        updateRoutine(course.getCode(), schedule);

        pandaImage.setVisibility(View.GONE);
        pandaText.setVisibility(View.GONE);
        stopThrobbingEffect();
    }

    /*
     * iterates over the list of schedules and fetches the day.
     * the day is then converted into it's corresponding index number and then using that
     * a new schedule object is added to the list of rountines
     */
    private void updateRoutine(String courseCode, ArrayList<Schedule> schedule) {

        for(Schedule schedule1: schedule){
            Integer index = Common.GET_INDEX_FROM_DAY.get(schedule1.getName()); // o(1)
            newRoutineIndexes.add(index);
            mRoutines.get(index).getItems().add(new Schedule(courseCode, Common.convertTo12hFormat(schedule1.getStartTime()), Common.convertTo12hFormat(schedule1.getEndTime())));
        }
    }

    /*
     * the FAB at the bottom. Clicking it, opens up the bottom sheet dialog to add courses
     */
    public void onAddCourseClick(View view) {
        // open bottom sheet to add course
        AddCourseBottomSheetDialog dialog = new AddCourseBottomSheetDialog();
        dialog.show(getSupportFragmentManager(), "VIEW_COURSES_ACTIVITY");
        stopThrobbingEffect();

    }


}
