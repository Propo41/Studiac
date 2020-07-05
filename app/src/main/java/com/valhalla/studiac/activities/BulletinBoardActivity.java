package com.valhalla.studiac.activities;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.HorizontalScrollView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.valhalla.studiac.R;
import com.valhalla.studiac.adapters.bulletinboard.BulletinRecycleAdapter;
import com.valhalla.studiac.database.Firebase;
import com.valhalla.studiac.fragments.dialogs.AddPostBottomSheetDialog;
import com.valhalla.studiac.fragments.dialogs.ProfileViewDialog;
import com.valhalla.studiac.fragments.dialogs.SendMessageBottomSheetDialog;
import com.valhalla.studiac.models.Message;
import com.valhalla.studiac.models.Messenger;
import com.valhalla.studiac.models.Post;
import com.valhalla.studiac.models.Student;
import com.valhalla.studiac.utility.Common;
import com.valhalla.studiac.utility.SharedPreference;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


public class BulletinBoardActivity extends AppCompatActivity implements
        AddPostBottomSheetDialog.OnAddPostListener,
        CompoundButton.OnClickListener,
        SendMessageBottomSheetDialog.OnButtonClickListener {

    private BulletinRecycleAdapter mAdapter; // we need to change it to ExampleAdapter object
    private Student mUserInfo;
    private CompoundButton mHeaderToggledButton; // holds the toggle button that is currently selected
    private HorizontalScrollView mHeaderScrollView;
    private ArrayList<Post> mAllPosts;
    private ArrayList<String> mAllPostsKeys; // contains the keys of the posts: for firebase
    // private ArrayList<String> mMyPostsKeys; // contains the keys of the posts: for firebase
    private ArrayList<Post> mCurrentPosts;
    private HashMap<String, Boolean> mLikedPosts;
    private FloatingActionButton mAddPostButton;
    private ShimmerFrameLayout mShimmerViewContainer;
    private AppBarLayout mAppBar;
    private RecyclerView.LayoutManager mLayoutManager;
    private RecyclerView mRecyclerView;
    private FirebaseUser mUser;
    private ChildEventListener mChildEventListener;
    private HashMap<String, Boolean> mImported; // used to keep track of posts that are imported using SingleValueEventListener
    private final String TAG = "BulletinBoardActivity";
    private ConstraintLayout mEmptyPosts;
    private int mLikedPos;
    private final int RESULT_DELETED_POSTS = 1;
    private int mIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bulletin_board);

        mImported = new HashMap<>();
        mUser = FirebaseAuth.getInstance().getCurrentUser();

        if (isUserAuthenticated(mUser)) {
            initViews();
            // [START] shimmer_effect
            mShimmerViewContainer.startShimmerAnimation();
            setupToolbar();
            setupHeaderViews();

            // initializing lists
            //  mMyPostsKeys = new ArrayList<>();
            mAllPosts = new ArrayList<>();
            mAllPostsKeys = new ArrayList<>();
            mCurrentPosts = new ArrayList<>();
            // import userInfo -> import all posts from database based on user's university name.
            // then add a child listener to the posts dir
            importUserInfo();
            onAddPostClicked();

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

    private void initViews() {
        mHeaderScrollView = findViewById(R.id.header_bulletin_sort_buttons_id);
        mShimmerViewContainer = findViewById(R.id.bulletin_shimmer_view_container);
        mAppBar = findViewById(R.id.bulletin_appBar);
        mEmptyPosts = findViewById(R.id.bulletin_empty_id);

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

    private void setupToolbar() {
        Toolbar mToolbar = findViewById(R.id.bulletin_toolbar_white);
        setSupportActionBar(mToolbar);
        // changes the default back button
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeAsUpIndicator(R.drawable.toolbar_ic_back);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.options_menu_bulletin, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish(); // go back to parent activity
                return true;
            case R.id.more_view_posts_id:
                startActivityForResult(new Intent(this,
                        BulletinBoardMyPostsActivity.class), RESULT_DELETED_POSTS);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == RESULT_DELETED_POSTS) {
            if (data != null) {
                Bundle bundle = data.getExtras();
                if (bundle != null) {
                    ArrayList<String> deletedPosts = bundle.getStringArrayList("deletedPosts");
                    // delete the posts from the ui
                    if (deletedPosts != null) {
                        for (String key : deletedPosts) {
                            int index = mAllPostsKeys.indexOf(key);
                            mAllPostsKeys.remove(index);
                            Post post = mAllPosts.get(index);
                            mAllPosts.remove(index);
                            mCurrentPosts.remove(post);
                            mAdapter.notifyItemRemoved(index);
                            mImported.remove(key); // remove the key from the importedList

                        }
                        if (mAllPosts.size() == 0) {
                            mEmptyPosts.setVisibility(View.VISIBLE);
                            mHeaderScrollView.setVisibility(View.GONE); // set visibility OFF for the toggle buttons
                        }
                    }

                }

            }

        }
    }

    private void setupList() {
        mRecyclerView = findViewById(R.id.bulletin_recycle_view_id);
        mLayoutManager = new LinearLayoutManager(this);
        mAdapter = new BulletinRecycleAdapter(mCurrentPosts);
        handleListEvents();
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);

    }


    private void handleListEvents() {

        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy > 0 && mAddPostButton.getVisibility() == View.VISIBLE) {
                    mAddPostButton.hide();
                } else if (dy < 0 && mAddPostButton.getVisibility() != View.VISIBLE) {
                    mAddPostButton.show();
                }
            }
        });

        mAdapter.setOnItemClickListener(new BulletinRecycleAdapter.OnItemClickListener() {
            @Override
            public void onButtonClick(int position) {
                // fetch userUid from the list based on the position and then send a message to the user
                likePost(position);

            }

            @Override
            public void onProfileClick(int position, View view) {
                ProfileViewDialog profileViewDialog = new ProfileViewDialog(mUserInfo);
                profileViewDialog.show(getSupportFragmentManager(), TAG);


            }
        });

    }

    /**
     * opens a dialog to write a message to the user iff the post is not liked already
     */
    private void likePost(int position) {
        mLikedPos = position;
        mIndex = mAllPosts.indexOf(mCurrentPosts.get(mLikedPos));
        // check if the post is already liked or not
        Boolean isLiked = mLikedPosts.get(mAllPostsKeys.get(mIndex));
        if (isLiked != null) {
            createToast();
        } else {
            // open dialog to write the message
            SendMessageBottomSheetDialog dialog = new SendMessageBottomSheetDialog();
            dialog.show(getSupportFragmentManager(), TAG);
            // could be bug present in the post
        }


    }


    private void createToast() {
        Toast toast = Toast.makeText(this, "You've already shown interest in this post!", Toast.LENGTH_SHORT);
        TextView v = toast.getView().findViewById(android.R.id.message);
        if (v != null) {
            v.setGravity(Gravity.CENTER);
        }
        toast.show();
    }


    private String generateRandomKey() {
        return "randomKey" + Math.random();
    }

    /**
     * @return a long corresponding to the current time in ms
     */
    private long getCurrentTime() {
        return new Date().getTime();
    }

    private void importUserInfo() {
        Firebase.getDatabaseReference().
                child(Firebase.USERS).
                child(mUser.getUid()).
                child(Firebase.BASIC_INFO).
                addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        mUserInfo = dataSnapshot.getValue(Student.class);
                        importLikedPosts(); // after importing liked posts, imports all posts
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

    }

    private void importAllPosts() {
        Firebase.getDatabaseReference().
                child(Firebase.UNIVERSITIES).
                child(mUserInfo.getUniversityName()).
                child(Firebase.POSTS).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    Post post = data.getValue(Post.class);
                    mAllPosts.add(0, post);
                    mAllPostsKeys.add(0, data.getKey());
                    mImported.put(data.getKey(), true);
                }

                mCurrentPosts.addAll(mAllPosts);
                // [STOP] shimmer_effect
                mShimmerViewContainer.stopShimmerAnimation();

                if (mAllPosts.size() == 0) {
                    mEmptyPosts.setVisibility(View.VISIBLE);

                } else {
                    mHeaderScrollView.setVisibility(View.VISIBLE); // set visibility on for the toggle buttons
                }
                mAppBar.setBackgroundColor(getResources().getColor(R.color.colorWhite));
                mShimmerViewContainer.setVisibility(View.GONE);
                mAddPostButton.setVisibility(View.VISIBLE); // set visibility on for the FAB

                setupList(); // create the recycler view
                mRecyclerView.scheduleLayoutAnimation();
                attachChildListener(); // attach child listener to university "posts"


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    /*
     * imports the keys of the posts liked
     * liked posts are imported here to keep track of repeated likes in a single post
     */
    private void importLikedPosts() {
        mLikedPosts = new HashMap<>();
        Firebase.getDatabaseReference().
                child(Firebase.USERS).
                child(mUser.getUid()).
                child("likedPosts").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    mLikedPosts.put(data.getValue(String.class), true);
                }

                importAllPosts();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }


    private void attachChildListener() {
        mChildEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                if (!mImported.containsKey(dataSnapshot.getKey())) {
                    Post post = dataSnapshot.getValue(Post.class);
                    mAllPosts.add(0, post);
                    mAllPostsKeys.add(0, dataSnapshot.getKey());
                    mCurrentPosts.add(0, post);
                    mAdapter.notifyItemInserted(0);
                    //  mRecyclerView.smoothScrollToPosition(0);

                    mLayoutManager.scrollToPosition(0);


                    mImported.put(dataSnapshot.getKey(), true);
                    // mAdapter.notifyDataSetChanged();
                    Log.i("adding child", mImported.toString());


                    mEmptyPosts.setVisibility(View.GONE);
                    mHeaderScrollView.setVisibility(View.VISIBLE); // set visibility on for the toggle buttons

                }

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
                mImported.remove(dataSnapshot.getKey()); // remove the key from the importedList

                if (mAllPosts.size() == 0) {
                    mEmptyPosts.setVisibility(View.VISIBLE);
                    mHeaderScrollView.setVisibility(View.GONE); // set visibility on for the toggle buttons
                }

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };

        Firebase.getDatabaseReference().
                child(Firebase.UNIVERSITIES).
                child(mUserInfo.getUniversityName()).
                child(Firebase.POSTS).addChildEventListener(mChildEventListener);
    }

    private void detachChildListener() {
        if (mChildEventListener != null) {
            Firebase.getDatabaseReference().
                    child(Firebase.UNIVERSITIES).
                    child(mUserInfo.getUniversityName()).
                    child(Firebase.POSTS).removeEventListener(mChildEventListener);
        }

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

    /**
     * writes a new post and uploads it to the database in the directories:
     * 1. universities/.../posts/..
     * 2. user-posts/.../...
     * when user posts a new post from the bottom sheet dialog, this method will be triggered
     */
    @Override
    public void writePost(String category, String topic, String description) {
        Post post = new Post(description, category, topic, mUser.getUid(), mUserInfo.getName(),
                mUserInfo.getImageResName(), getCurrentTime());

        // get the key that is pushed into the database
        String key = Firebase.getDatabaseReference().
                child(Firebase.UNIVERSITIES).
                child(mUserInfo.getUniversityName()).
                child(Firebase.POSTS).
                push().getKey();

        Map<String, Object> postValues = post.toMap();
        Map<String, Object> childUpdates = new HashMap<>();
        // adding the post to both the main post feed and also the user's feed
        childUpdates.put("/universities/" + mUserInfo.getUniversityName() + "/posts/" + key, postValues);
        childUpdates.put("/user-posts/" + mUser.getUid() + "/" + key, postValues);
        // adding a completion listener.
        Firebase.getDatabaseReference().updateChildren(childUpdates).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(BulletinBoardActivity.this, "Post Created!", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(BulletinBoardActivity.this, "Post Creation Failed", Toast.LENGTH_SHORT).show();

            }
        });

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
            mRecyclerView.scheduleLayoutAnimation();


        } else {
            mHeaderToggledButton.setChecked(true); // if button is already toggled. clicking it won't remove the toggle effect

        }


    }

    @Override
    protected void onPause() {
        super.onPause();
        detachChildListener();

        // mark the user's current activity as bulletin Board Activity
        SharedPreference preference = SharedPreference.getInstance(getApplicationContext());
        preference.saveData(String.class.getSimpleName(), getString(R.string.current_activity), null);

    }

    @Override
    protected void onResume() {
        super.onResume();

        // mark the user's current activity as bulletin Board Activity
        SharedPreference preference = SharedPreference.getInstance(getApplicationContext());
        preference.saveData(String.class.getSimpleName(), getString(R.string.current_activity), BulletinBoardActivity.class.getSimpleName());

        if (mUserInfo != null) {
            attachChildListener();
        }
    }


    /**
     * sends a message to the user from the bottom sheet dialog
     *
     * @param message the message to be sent
     */
    @Override
    public void sendMessage(final String message) {
        // pushing the liked post's key to the user's child node, so that a user cannot like a post more than once
        Firebase.getDatabaseReference().
                child(Firebase.USERS).
                child(mUser.getUid()).
                child("likedPosts").push().setValue(mAllPostsKeys.get(mIndex));
        mLikedPosts.put(mAllPostsKeys.get(mIndex), true);


        final Post post = mCurrentPosts.get(mLikedPos);

        // check if the uid exists already in users/$userUID/chats/
        Firebase.getDatabaseReference().
                child(Firebase.USERS).
                child(mUser.getUid()).
                child("chats").
                child(post.getUid()).
                addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        String threadKey;
                        long currentTime = getCurrentTime();
                        Message messageInstance = new Message(message, currentTime, mUser.getUid());
                        String chatKey;


                        if (dataSnapshot.exists()) {
                            // it means, the users have already communicated before and has a thread already
                            // in that case, fetch the thread key and push the message into the thread
                            threadKey = dataSnapshot.getKey();
                            if (threadKey != null) {
                                chatKey = Firebase.getDatabaseReference().child("chat-messages").child(threadKey).push().getKey();
                            } else {
                                chatKey = generateRandomKey();
                            }

                            // create a new Message instance
                            HashMap<String, Object> childUpdates = new HashMap<>();
                            childUpdates.put("/chat-messages/" + threadKey + "/" + chatKey, messageInstance);
                            childUpdates.put("/messenger/" + threadKey + "/info/lastMessage", message);
                            childUpdates.put("/messenger/" + threadKey + "/info/time", currentTime);

                            Firebase.getDatabaseReference().
                                    updateChildren(childUpdates).
                                    addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Toast.makeText(BulletinBoardActivity.this,
                                                    "Message sent", Toast.LENGTH_SHORT).show();

                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(BulletinBoardActivity.this,
                                            e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });

                        } else {
                            // else, create a new thread, and fetch its key
                            threadKey = Firebase.getDatabaseReference().
                                    child(Firebase.USERS).
                                    child("chats").
                                    push().getKey();
                            HashMap<String, Object> childUpdates = new HashMap<>();
                            // and upload the messenger instance and the thread key in appropriate places

                            // putting the thread key in the user's nodes under chats
                            childUpdates.put("/users/" + mUser.getUid() + "/chats/" + post.getUid(), threadKey);
                            childUpdates.put("/users/" + post.getUid() + "/chats/" + mUser.getUid(), threadKey);

                            // pushing the very first message into the database
                            if (threadKey != null) {
                                chatKey = Firebase.getDatabaseReference().
                                        child("chat-messages").
                                        child(threadKey).
                                        push().getKey();
                            } else {
                                chatKey = generateRandomKey();
                            }
                            childUpdates.put("/chat-messages/" + threadKey + "/" + chatKey, messageInstance);

                            // updating the participant list in the messenger node
                            childUpdates.put("/messenger/" + threadKey + "/participants/" + mUser.getUid(), true);
                            childUpdates.put("/messenger/" + threadKey + "/participants/" + post.getUid(), true);

                            // create a new messenger instance
                            Messenger messenger = new Messenger(mUserInfo.getImageResName(),
                                    mUserInfo.getName(), post.getAuthorImageRes(),
                                    post.getAuthor(), mUser.getUid(), post.getUid(), currentTime, message);
                            childUpdates.put("/messenger/" + threadKey + "/info", messenger);

                            Firebase.getDatabaseReference().
                                    updateChildren(childUpdates).
                                    addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Toast.makeText(BulletinBoardActivity.this,
                                                    "Message sent", Toast.LENGTH_SHORT).show();
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(BulletinBoardActivity.this,
                                            e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }
}
