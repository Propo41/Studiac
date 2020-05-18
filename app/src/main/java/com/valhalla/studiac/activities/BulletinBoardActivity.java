package com.valhalla.studiac.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.HorizontalScrollView;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.valhalla.studiac.R;
import com.valhalla.studiac.adapters.bulletinboard.BulletinMyPostsRecycleAdapter;
import com.valhalla.studiac.adapters.bulletinboard.BulletinRecycleAdapter;
import com.valhalla.studiac.database.Firebase;
import com.valhalla.studiac.database.FirebaseListener;
import com.valhalla.studiac.fragments.dialogs.AddPostBottomSheetDialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.valhalla.studiac.models.Post;
import com.valhalla.studiac.models.Student;
import com.valhalla.studiac.utility.Common;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class BulletinBoardActivity extends AppCompatActivity implements AddPostBottomSheetDialog.OnAddPostListener, CompoundButton.OnClickListener {

    private BulletinRecycleAdapter mAdapter; // we need to change it to ExampleAdapter object
    private String userUid = "student"; // for debug. use actual value later
    private FirebaseListener mFirebaseListener;
    private Student mUserInfo;
    private CompoundButton mHeaderToggledButton; // holds the toggle button that is currently selected
    private HorizontalScrollView mHeaderScrollView;

    private ArrayList<Post> mAllPosts;
    private ArrayList<String> mAllPostsKeys; // contains the keys of the posts: for firebase

    private ArrayList<Post> mCurrentPosts;

    private static int mPostCounter = 0; // since we're using both child and singleValue listener, we need to keep track of the number of posts imported from single value listener, since we don't want to duplicate them
    private FloatingActionButton mAddPostButton;
    private ShimmerFrameLayout mShimmerViewContainer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bulletin_board);
        initViews();
        // [START] shimmer_effect
        mShimmerViewContainer.startShimmerAnimation();
        setupToolbar();
        setupHeaderViews();
        checkDbStatus();

        mAllPosts = new ArrayList<>();
        mAllPostsKeys = new ArrayList<>();

        mCurrentPosts = new ArrayList<>();

        // import userInfo -> import all posts from database based on user's university name.
        // then add a child listener to the posts dir
        importUserInfo();
        onAddPostClicked();


    }

    private void initViews() {
        mHeaderScrollView = findViewById(R.id.header_bulletin_sort_buttons_id);
        mShimmerViewContainer = findViewById(R.id.bulletin_shimmer_view_container);

    }

    private void setupHeaderViews() {
        ToggleButton toggleHeaderAll = findViewById(R.id.bulletin_header_button_all_id);
        ToggleButton toggleHeaderOfficial = findViewById(R.id.bulletin_header_button_official_id);
        ToggleButton toggleHeaderAd = findViewById(R.id.bulletin_header_button_advertisement_id);
        ToggleButton toggleHeaderHelp = findViewById(R.id.bulletin_header_button_help_id);
        ToggleButton toggleHeaderOthers = findViewById(R.id.bulletin_header_button_others_id);

        mHeaderToggledButton = toggleHeaderAll;

        toggleHeaderAll.setOnClickListener(this);
        toggleHeaderOfficial.setOnClickListener(this);
        toggleHeaderAd.setOnClickListener(this);
        toggleHeaderHelp.setOnClickListener(this);
        toggleHeaderOthers.setOnClickListener(this);
    }


    private void checkDbStatus() {
        DatabaseReference connectedRef = FirebaseDatabase.getInstance().getReference(".info/connected");
        connectedRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                boolean connected = snapshot.getValue(Boolean.class);
                if (connected) {
                    Toast.makeText(getApplicationContext(), "Connection Established", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), "No Connection", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.i("TAG: ", "Listener was cancelled");
            }
        });
    }

    private void setupToolbar() {
        Toolbar mToolbar = findViewById(R.id.bulletin_toolbar_white);
        setSupportActionBar(mToolbar);
        // changes the default back button
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.toolbar_ic_back);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.options_menu_white, menu);
        menu.getItem(0).setVisible(true);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                finish(); // go back to parent activity
                return true;
            case R.id.main_menu_id:
                Toast.makeText(this, "Default: Main Menu", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.settings_id:
                Toast.makeText(this, "Default: Settings", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.more_view_posts_id:
                Intent intent = new Intent(this, BulletinBoardMyPostsActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);


        }
    }

    private void setupList() {
        RecyclerView recyclerView = findViewById(R.id.bulletin_recycle_view_id);
     /*   recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(),
                DividerItemDecoration.VERTICAL));*/
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);

        mAdapter = new BulletinRecycleAdapter(mCurrentPosts);
        handleListEvents();
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(mAdapter);

    }

    private void handleListEvents() {
        mAdapter.setOnItemClickListener(new BulletinRecycleAdapter.OnItemClickListener() {
            @Override
            public void onButtonClick(int position) {
                // fetch userUid from the list based on the position and then send a message to the user
                // todo: add animation effect of plane flying off
                Toast.makeText(BulletinBoardActivity.this, mCurrentPosts.get(position).getUid(), Toast.LENGTH_SHORT).show();

            }
        });

    }


    private void importUserInfo() {

        Firebase.getDatabaseReference().child(userUid).child(Firebase.BASIC_INFO).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mUserInfo = dataSnapshot.getValue(Student.class);
                importAllPosts();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void importAllPosts() {
        Firebase.getDatabaseReference().
                child(mUserInfo.getUniversityName()).
                child(Firebase.POSTS).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    Post post = data.getValue(Post.class);
                    mAllPosts.add(post);
                    mAllPostsKeys.add(data.getKey());
                    mPostCounter += 1;
                }

                mCurrentPosts.addAll(mAllPosts);


                // [STOP] shimmer_effect
                mShimmerViewContainer.stopShimmerAnimation();
                mShimmerViewContainer.setVisibility(View.GONE);
                mHeaderScrollView.setVisibility(View.VISIBLE); // set visibility on for the toggle buttons
                mAddPostButton.setVisibility(View.VISIBLE); // set visibility on for the FAB

                setupList(); // create the recycler view
                attachChildListener(); // attach child listener to university "posts"
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    private void attachChildListener() {
        Firebase.getDatabaseReference().
                child(mUserInfo.getUniversityName()).
                child(Firebase.POSTS).
                addChildEventListener(new ChildEventListener() {
                    int counter = 0;

                    @Override
                    public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                        counter += 1;
                        if (counter > mPostCounter) {
                            Post post = dataSnapshot.getValue(Post.class);
                            mAllPosts.add(post);
                            mAllPostsKeys.add(dataSnapshot.getKey());
                            mCurrentPosts.add(post);
                            mAdapter.notifyItemInserted(mCurrentPosts.size() - 1);

                            Log.i("adding child", String.valueOf(counter));

                        }

                        // mRecyclerView.smoothScrollToPosition(0);
                        //mAdapter.notifyItemInserted(mPosts.size());


                    }

                    @Override
                    public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                    }

                    @Override
                    public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                        int index = mAllPostsKeys.indexOf(dataSnapshot.getKey());
                        mAllPostsKeys.remove(index);
                        Post post = mAllPosts.get(index);
                        mAllPosts.remove(index);
                        mCurrentPosts.remove(post);
                        mAdapter.notifyItemRemoved(index);

                    }

                    @Override
                    public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }


    /*
     * when the FAB at the bottom is pressed, open up the bottom sheet dialog to add posts
     */
    private void onAddPostClicked() {
        mAddPostButton = findViewById(R.id.bulletin_add_post_button_id);
        mAddPostButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // open dialog 19: bottom sheet dialog to add post
                // the result will be passed into onActivityResult() method
                AddPostBottomSheetDialog dialog = new AddPostBottomSheetDialog();
                dialog.show(getSupportFragmentManager(), "BulletinActivity");

            }
        });
    }

    /*
     * when user posts a new post from the bottom sheet dialog, this method will be triggered
     */

    @Override
    public void writePost(String category, String topic, String description) {

        Calendar calendar = Calendar.getInstance();
        // formatting the date to the format DAY MONTH
        SimpleDateFormat formatter = new SimpleDateFormat("dd MMMM", Locale.getDefault());
        // create a new post and upload it to database
        Post post = new Post(description, category, topic, userUid, mUserInfo.getName(), mUserInfo.getImageResName(), formatter.format(calendar.getTime()));

        // get the key that is pushed into the database
        String key = Firebase.getDatabaseReference().
                child(mUserInfo.getUniversityName()).
                child("posts").
                push().getKey();

        Map<String, Object> postValues = post.toMap();
        Map<String, Object> childUpdates = new HashMap<>();
        // adding the post to both the main post feed and also the user's feed
        childUpdates.put("/" + mUserInfo.getUniversityName() + "/posts/" + key, postValues);
        childUpdates.put("/user-posts/" + userUid + "/" + key, postValues);
        // adding a completion listener.
        Firebase.getDatabaseReference().updateChildren(childUpdates).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(BulletinBoardActivity.this, "Post Created Successfully", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(BulletinBoardActivity.this, "Post Creation Failed", Toast.LENGTH_SHORT).show();

            }
        });

       /* Firebase.getDatabaseReference().
                child(mUserInfo.getUniversityName()).
                child(Firebase.POSTS).
                child(key).setValue(post);*/

    }


    /*
     * when the toggle buttons are clicked, this will be called
     */
    @Override
    public void onClick(View v) {
        CompoundButton compoundButton = (CompoundButton) v;
        // if a button is clicked, check if the button is already toggled.
        // if it is, then keep it checked, else uncheck the previous toggle and replace the toggle button
        // with the new one
        if (mHeaderToggledButton != compoundButton) {

            mHeaderToggledButton.setChecked(false);
            mHeaderToggledButton = compoundButton;

            String toggled = mHeaderToggledButton.getText().toString();
            // Toast.makeText(this, "toggled: " + toggled, Toast.LENGTH_SHORT).show();

            mCurrentPosts.clear();
            // when the All button is toggled
            if (toggled.equals(Common.POST_CATEGORY_5)) {
                mCurrentPosts.addAll(mAllPosts);
            } else {
                // if the remaining buttons are toggled, then put them in the currentList based
                // on their categories.
                for (Post post : mAllPosts) {
                    if (post.getCategory().equals(toggled)) {
                        mCurrentPosts.add(post);
                    }
                }
            }

            mAdapter.notifyDataSetChanged();


        } else {
            mHeaderToggledButton.setChecked(true); // if button is already toggled. clicking it won't remove the toggle effect

        }


    }

}
