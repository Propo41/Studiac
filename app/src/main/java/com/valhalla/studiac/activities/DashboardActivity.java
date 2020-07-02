package com.valhalla.studiac.activities;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.valhalla.studiac.R;
import com.valhalla.studiac.activities.settings.SettingsActivity;
import com.valhalla.studiac.database.Firebase;
import com.valhalla.studiac.utility.Common;
import com.valhalla.studiac.models.Student;
import com.valhalla.studiac.utility.SharedPreference;

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
    private FirebaseUser mUser;
    private FirebaseAuth mAuth;
    private ShimmerFrameLayout mShimmerViewContainer;
    private final String TAG = "DashboardActivity";

    // setting content for navigation drawer
    private View mHeaderView;
    private TextView mNavName;
    private TextView mNavEmail;
    private ImageView mNavImage;
    // setting content for the dashboard views
    private ImageView mUserImage;
    private TextView mUsername;
    private TextView mUniversityName;
    private TextView mDepartmentName;
    private TextView mSemester;
    private TextView mCgpa;
    private final static boolean ONLINE = true;
    private final static boolean OFFLINE = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        findViewById(R.id.dashboard_content_group).setVisibility(View.INVISIBLE);

        // [START] start_shimmer_effect
        mShimmerViewContainer = findViewById(R.id.dashboard_shimmer_view_container);
        mShimmerViewContainer.startShimmerAnimation();

        mAuth = FirebaseAuth.getInstance();

        mUser = mAuth.getCurrentUser();
        if (isUserAuthenticated(mUser)) {
            initialiseViews();
            checkNetworkStatus();
          //  changeStatus(ONLINE);
          //  changeStatus(OFFLINE);

            setupDrawer();
            setStudentContent();
            animateDashboard();
        }

    }

    private void changeStatus(boolean status) {
        if (status == ONLINE) {
            Firebase.getDatabaseReference().
                    child(Firebase.USERS).
                    child(mUser.getUid()).
                    child("device-status").
                    child("status").
                    setValue(true);
        } else {
            Firebase.getDatabaseReference().
                    child(Firebase.USERS).
                    child(mUser.getUid()).
                    child("device-status").
                    child("status").onDisconnect().setValue(false);
        }
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

    /*
     * uses database to set the contents
     */
    private void setStudentContent() {

        Firebase.getDatabaseReference().
                child(Firebase.USERS).
                child(mUser.getUid()).
                child(Firebase.BASIC_INFO).
                addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        Student student = dataSnapshot.getValue(Student.class);
                        assert student != null;
                        int imgId = getImageDrawableId(student.getImageResName(), getApplicationContext());

                        mNavImage.setImageResource(imgId);
                        mNavName.setText(student.getName());
                        mNavEmail.setText(mUser.getEmail());

                        mUserImage.setBackground(getDrawable(imgId));
                        // userImage.setImageResource(imgId);
                        mUsername.setText(student.getName());
                        mUniversityName.setText(student.getUniversityName());
                        mDepartmentName.setText(student.getDepartmentName());
                        int currentSemester = student.getCurrentSemester();
                        mSemester.setText("Semester: " + currentSemester);
                        mCgpa.setText("CGPA: N/A");

                        mShimmerViewContainer.stopShimmerAnimation();
                        mShimmerViewContainer.setVisibility(View.GONE);
                        findViewById(R.id.dashboard_content_group).setVisibility(View.VISIBLE);
                        // [END] start_shimmer_effect

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        databaseError.toException().printStackTrace();
                    }
                });


    }

    private int getImageDrawableId(String resName, Context context) {
        Resources resources = context.getResources();
        return resources.getIdentifier(resName, "drawable", Common.PACKAGE_NAME);
    }

    private void setupDrawer() {

        Toolbar toolbar = findViewById(R.id.dashboard_toolbar);
        toolbar.setTitle(" ");
        setSupportActionBar(toolbar);
        mDrawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, mDrawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawer.addDrawerListener(toggle);
        toggle.syncState(); // this will handle the animation of the icon
        // mNavigationView = findViewById(R.id.nav_view);
        mNavigationView.setNavigationItemSelectedListener(this);
        mNavigationView.setCheckedItem(R.id.nav_dashboard); // initially this will be checked


    }

    @Override
    protected void onResume() {
        super.onResume();
        // retrieve data from sharedPreferences to check if there are any changes made to profile settings
        //SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
        SharedPreference preference = SharedPreference.getInstance(getApplicationContext());
        Boolean changes = (Boolean) preference.getData(Boolean.class.getSimpleName(), getString(R.string.profile_changes));


        //  boolean changes = sharedPref.getBoolean(getString(R.string.profile_changes), false);

        // if any changes are made, then update ui
        if (changes != null && changes) {
            // reset the value to false
            Log.d(TAG, "profile changed. updating values");
            // set changes to false

            preference.saveData(Boolean.class.getSimpleName(), getString(R.string.profile_changes), false);

          /*  SharedPreferences.Editor editor = sharedPref.edit();
            editor.putBoolean(getString(R.string.profile_changes), false);
            editor.apply();*/
            // update the ui
            setStudentContent();

        }


    }

    private void initialiseViews() {

        dashboardHeader = findViewById(R.id.dashboard_card_view_id);
        viewRoutineCardView = findViewById(R.id.dashboard_view_routine_button_id);
        viewCoursesCardView = findViewById(R.id.dashboard_view_courses_button_id);
        viewMessagesCardView = findViewById(R.id.dashboard_view_messages_button_id);
        bulletinBoardCardView = findViewById(R.id.dashboard_bulletin_button_id);
        todoTasksCardView = findViewById(R.id.dashboard_todo_tasks_button_id);
        resultTrackerCardView = findViewById(R.id.dashboard_result_tracker_button_id);

        // setting content for navigation drawer
        mNavigationView = findViewById(R.id.nav_view);
        mHeaderView = mNavigationView.getHeaderView(0);
        mNavName = mHeaderView.findViewById(R.id.nav_drawer_user_name_id);
        mNavEmail = mHeaderView.findViewById(R.id.nav_drawer_user_email_id);
        mNavImage = mHeaderView.findViewById(R.id.nav_drawer_user_image_id);
        // setting content for the dashboard views
        mUserImage = findViewById(R.id.dashboard_user_image_id);
        mUsername = findViewById(R.id.dashboard_user_name_id);
        mUniversityName = findViewById(R.id.dashboard_university_id);
        mDepartmentName = findViewById(R.id.dashboard_department_id);
        mSemester = findViewById(R.id.dashboard_semester_id);
        // todo: calculate cgpa up to current semester
        mCgpa = findViewById(R.id.dashboard_cgpa_id);


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

    /**
     * checks when the user is active or disconnected.
     * firebase automatically removes the listener and disconnects after inactivity of 60 seconds
     */
    private void checkNetworkStatus() {
        DatabaseReference connectedRef = FirebaseDatabase.getInstance().getReference(".info/connected");
        connectedRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Boolean connected = snapshot.getValue(Boolean.class);
                if (connected != null && connected) {
                    Toast.makeText(getApplicationContext(), "Connected!", Toast.LENGTH_SHORT).show();
                    changeStatus(ONLINE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.i("TAG: ", "Listener was cancelled");
            }
        });

        changeStatus(OFFLINE);

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
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.nav_settings:
                startActivity(new Intent(this, SettingsActivity.class));
                break;
            case R.id.nav_update_semester:
                // startActivity(new Intent(this, MessengerActivity.class));
                break;
            case R.id.nav_quit:
                signOut();
                break;
        }
        mDrawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void signOut() {
        final Intent intent = new Intent(this, LoginActivity.class);
        // adding flags to intent so that in the new activity, pressing back button doesn't bring this activity back
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

        for (UserInfo user : mUser.getProviderData()) {
            if (user.getProviderId().equals(GoogleAuthProvider.GOOGLE_SIGN_IN_METHOD)) {
                // Google sign out
                GoogleSignIn.getClient(
                        getApplicationContext(),
                        new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).build()
                ).signOut().addOnCompleteListener(this,
                        new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Toast.makeText(DashboardActivity.this, "Signing Out", Toast.LENGTH_SHORT).show();
                                startActivity(intent);
                            }
                        });
            }
        }
        // sign out from firebase
        mAuth.signOut();
        Toast.makeText(DashboardActivity.this, "Signing Out", Toast.LENGTH_SHORT).show();
        startActivity(intent);
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
                    mUserImage.animate().alpha(0.0f);

                    Common.animateScaleDown(mUserImage, 300);
                    Common.animateScaleDown(dashboardHeader, 300);

                } else if (verticalOffset == 0) {

                    Common.animateScaleUp(mUserImage, 300);
                    Common.animateScaleUp(dashboardHeader, 300);

                    // if expanded, then do this
                    // headers
                    dashboardHeader.animate().alpha(1.0f);
                    dashboardHeader.animate().translationY(0);
                    mUserImage.animate().alpha(1.0f);
                    mUserImage.animate().translationY(0);

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
                    mUserImage.animate().translationY(-mUserImage.getHeight()).setDuration(animationDuration);

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
        startActivity(new Intent(this, ViewRoutineActivity.class));
    }

    public void onCourseClick(View v) {
        startActivity(new Intent(this, ViewCoursesActivity.class));
    }

    public void onBulletinBoardClick(View v) {
        startActivity(new Intent(this, BulletinBoardActivity.class));

    }

    public void onTodoTasksClick(View v) {
        startActivity(new Intent(this, TodoTaskActivity.class));
    }


    public void onResultTrackerClick(View v) {
        // gson diye hashmap gula serialize korb. Kore intent use kore string diye pathaba
        //  mStudent.getUniversity().getGradeWeights();
        //  mStudent.getUniversity().getSemesterResults();
        startActivity(new Intent(this, ResultTrackerActivity.class));

    }

    public void onViewMessagesClick(View v) {
        startActivity(new Intent(this, MessengerActivity.class));


    }


}
