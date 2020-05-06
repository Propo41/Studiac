package com.valhalla.studiac.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.navigation.NavigationView;
import com.valhalla.studiac.R;
import com.valhalla.studiac.activities.BulletinBoardActivity;
import com.valhalla.studiac.activities.ViewRoutineActivity;
import com.valhalla.studiac.utility.common.Common;
import com.valhalla.studiac.utility.common.Course;
import com.valhalla.studiac.utility.common.Student;

import java.io.IOException;
import java.util.ArrayList;

public class DashboardActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private int animationDuration = 400;
    private final int TRANSLATION_Y = 130;
    private CardView dashboardHeader;
    private ImageView dashboardUserImage;
    private CardView viewRoutineCardView;
    private CardView viewCoursesCardView;
    private CardView viewMessagesCardView;
    private CardView bulletinBoardCardView;
    private CardView todoTasksCardView;
    private CardView resultTrackerCardView;
    private int[] mTextViewIds = new int[6];
    private ArrayList<View> mTextViewsList;

    protected DrawerLayout mDrawer;
    protected NavigationView mNavigationView;
    private static Student mStudent;
    protected static boolean mDataLoadStatus = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        loadStudent();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        initialiseViews();
        setupDrawer();
        setStudentContent();
        animateDashboard();
    }

    @Override
    protected void onPause() {
        super.onPause();
        try {
            Common.saveToFile(mStudent, Common.STUDENT, null, getApplicationContext());
        } catch (IOException e) {
            e.printStackTrace();
        }
        Toast.makeText(getApplicationContext(), "saving data", Toast.LENGTH_SHORT).show();
    }

    private void setStudentContent() {

        // setting content for navigation drawer
        View headerView = mNavigationView.getHeaderView(0);
        TextView navName = headerView.findViewById(R.id.nav_drawer_user_name_id);
        navName.setText(mStudent.getFullName());
        TextView navEmail = headerView.findViewById(R.id.nav_drawer_user_email_id);
        navEmail.setText(mStudent.getEmail());
        //  ImageView navImage = headerView.findViewById(R.id.nav_drawer_user_image_id);
        //  navImage.setImageResource(mStudent.getImageResId());

        // setting content for the dashboard views
        TextView username = findViewById(R.id.dashboard_user_name_id);
        username.setText(mStudent.getFullName());
        TextView universityName = findViewById(R.id.dashboard_university_id);
        universityName.setText(mStudent.getUniversity().getName());
        TextView departmentName = findViewById(R.id.dashboard_department_id);
        departmentName.setText(mStudent.getUniversity().getDepartment());
        TextView semester = findViewById(R.id.dashboard_semester_id);
        int currentSemester = mStudent.getUniversity().getCurrentSemester();
        semester.setText("Semester: " + currentSemester);

        // todo: calculate cgpa up to current semester
        TextView cgpa = findViewById(R.id.dashboard_cgpa_id);
        //departmentName.setText("CGPA: " + mStudent.getUniversity().getSemesterResults().get(currentSemester).getGpa());
        cgpa.setText("CGPA: N/A");


    }


    private void setupDrawer() {

        Toolbar toolbar = findViewById(R.id.dashboard_toolbar);
        toolbar.setTitle(" ");
        setSupportActionBar(toolbar);
        mDrawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, mDrawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawer.addDrawerListener(toggle);
        toggle.syncState(); // this will handle the animation of the icon
        mNavigationView = findViewById(R.id.nav_view);
        mNavigationView.setNavigationItemSelectedListener(this);
        mNavigationView.setCheckedItem(R.id.nav_dashboard); // initially this will be checked


    }

    private void initialiseViews() {

        dashboardHeader = findViewById(R.id.dashboard_card_view_id);
        dashboardUserImage = findViewById(R.id.dashboard_user_image_id);

        viewRoutineCardView = findViewById(R.id.dashboard_view_routine_button_id);
        viewCoursesCardView = findViewById(R.id.dashboard_view_courses_button_id);
        viewMessagesCardView = findViewById(R.id.dashboard_view_messages_button_id);
        bulletinBoardCardView = findViewById(R.id.dashboard_bulletin_button_id);
        todoTasksCardView = findViewById(R.id.dashboard_todo_tasks_button_id);
        resultTrackerCardView = findViewById(R.id.dashboard_result_tracker_button_id);

        mTextViewIds[0] = R.id.textView23;
        mTextViewIds[1] = R.id.textView25;
        mTextViewIds[2] = R.id.textView26;
        mTextViewIds[3] = R.id.textView27;
        mTextViewIds[4] = R.id.textView28;
        mTextViewIds[5] = R.id.textView24;
        mTextViewsList = new ArrayList<>();

        for (int i : mTextViewIds) {
            mTextViewsList.add(findViewById(i));
        }


    }

    private void animateDashboard() {
        AppBarLayout appBarLayout = findViewById(R.id.dashboard_app_bar_id);

        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (Math.abs(verticalOffset) == appBarLayout.getTotalScrollRange()) {
                    // If collapsed, then do this
                    // headers
                    dashboardHeader.animate().alpha(0.0f);
                    dashboardUserImage.animate().alpha(0.0f);

                    Common.animateScaleDown(dashboardUserImage, 300);
                    Common.animateScaleDown(dashboardHeader, 300);

                } else if (verticalOffset == 0) {

                    Common.animateScaleUp(dashboardUserImage, 300);
                    Common.animateScaleUp(dashboardHeader, 300);

                    // if expanded, then do this
                    // headers
                    dashboardHeader.animate().alpha(1.0f);
                    dashboardHeader.animate().translationY(0);
                    dashboardUserImage.animate().alpha(1.0f);
                    dashboardUserImage.animate().translationY(0);

                    //     dashboardHeader.startAnimation(scaleUp);

                    // the card view buttons
                    viewRoutineCardView.animate().translationY(0); // 0 indicates, the views will return to their original position
                    viewCoursesCardView.animate().translationY(0);
                    viewMessagesCardView.animate().translationY(0);
                    bulletinBoardCardView.animate().translationY(0);
                    todoTasksCardView.animate().translationY(0);
                    resultTrackerCardView.animate().translationY(0);


                    for (View view : mTextViewsList) {
                        view.animate().translationY(0);
                    }

                } else {
                    // Somewhere in between.
                    dashboardHeader.animate().translationY(-dashboardHeader.getHeight()).setDuration(animationDuration);
                    dashboardUserImage.animate().translationY(-dashboardUserImage.getHeight()).setDuration(animationDuration);

                    // the card view buttons
                    viewRoutineCardView.animate().translationY(-TRANSLATION_Y).setDuration(animationDuration);
                    viewCoursesCardView.animate().translationY(-TRANSLATION_Y).setDuration(animationDuration);
                    viewMessagesCardView.animate().translationY(-TRANSLATION_Y).setDuration(animationDuration);
                    bulletinBoardCardView.animate().translationY(-TRANSLATION_Y).setDuration(animationDuration);
                    todoTasksCardView.animate().translationY(-TRANSLATION_Y).setDuration(animationDuration);
                    resultTrackerCardView.animate().translationY(-TRANSLATION_Y).setDuration(animationDuration);

                    for (View view : mTextViewsList) {
                        view.animate().translationY(-TRANSLATION_Y).setDuration(animationDuration);
                    }

                }
            }
        });
    }

    public void onRoutineClick(View v) {
        Intent intent = new Intent(this, ViewRoutineActivity.class);
        Bundle bundle = new Bundle();
        // Common.setRoutines(getStudent().getUniversity().getDailyRoutine());
        bundle.putParcelableArrayList("routine", mStudent.getUniversity().getDailyRoutine());
        intent.putExtras(bundle);
        startActivity(intent);
    }

    public void onCourseClick(View v) {
        Intent intent = new Intent(this, ViewCoursesActivity.class);
        Bundle bundle = new Bundle();
        // pass the courses list to the view courses activity
        bundle.putParcelableArrayList("courses", mStudent.getUniversity().getCourses());
        intent.putExtras(bundle);

        startActivityForResult(intent, Common.VIEW_COURSES);
    }

    public void onBulletinBoardClick(View v) {
        startActivity(new Intent(this, BulletinBoardActivity.class));


    }

    public void onTodoTasksClick(View v) {
        Intent intent = new Intent(this, TodoTaskActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("studentTag", mStudent.getEmail());
        bundle.putParcelableArrayList("courses", mStudent.getUniversity().getCourses());
        intent.putExtras(bundle);
        startActivity(intent);


    }


    public void onResultTrackerClick(View v) {
        // gson diye hashmap gula serialize korb. Kore intent use kore string diye pathaba
        //  mStudent.getUniversity().getGradeWeights();
        //  mStudent.getUniversity().getSemesterResults();

    }

    public void onViewMessagesClick(View v) {
        startActivity(new Intent(this, MessengerActivity.class));


    }

    protected void loadStudent() {
        // if the data is already loaded from the memory, then don't
        if (!mDataLoadStatus) {
            try {
                mStudent = (Student) Common.loadFromFile(Common.STUDENT, null, getApplicationContext());
            } catch (IOException e) {
                e.printStackTrace();
            }
            mDataLoadStatus = true;
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Common.VIEW_COURSES && resultCode == Activity.RESULT_OK) {
            // Toast.makeText(getApplicationContext(), "result ok", Toast.LENGTH_SHORT).show();
            Bundle bundle = data.getExtras();
            assert bundle != null;
            ArrayList<Course> courses = bundle.getParcelableArrayList("courses");
            mStudent.getUniversity().setCourses(courses);
        }

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.more_main_menu_id:
                Toast.makeText(this, "Main Menu", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.more_settings_id:
                Toast.makeText(this, "Settings", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.more_sync_id:
                Toast.makeText(this, "Sync", Toast.LENGTH_SHORT).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);


        }
    }

    @Override
    public void onBackPressed() {
        // if the drawer is open, close the drawer instead
        if (mDrawer.isDrawerOpen(GravityCompat.START)) {
            mDrawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.options_menu, menu);
        return true;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.nav_dashboard:
                // do nothing here since already in this activity
                break;
            case R.id.nav_edit_profile:
                // startActivity(new Intent(this, DashboardActivity.class));
                break;
            case R.id.nav_help:
                // startActivity(new Intent(this, TodoTaskActivity.class));
                break;
            case R.id.nav_settings:
                // startActivity(new Intent(this, BulletinBoardActivity.class));
                break;
            case R.id.nav_update_semester:
                // startActivity(new Intent(this, MessengerActivity.class));
                break;
            case R.id.nav_quit:
                break;

        }
        mDrawer.closeDrawer(GravityCompat.START);
        return true;
    }


}
