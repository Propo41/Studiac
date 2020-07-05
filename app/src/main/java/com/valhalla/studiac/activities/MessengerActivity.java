package com.valhalla.studiac.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.Group;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.valhalla.studiac.R;
import com.valhalla.studiac.adapters.messenger.MessengerRecycleAdapter;
import com.valhalla.studiac.database.Firebase;
import com.valhalla.studiac.fragments.dialogs.ProfileViewDialog;
import com.valhalla.studiac.models.Messenger;
import com.valhalla.studiac.models.Student;
import com.valhalla.studiac.toolbars.NavigationToolbarWhite;

import java.util.ArrayList;
import java.util.HashMap;

public class MessengerActivity extends NavigationToolbarWhite {

    private final String TAG = "MessengerActivity";
    private FirebaseUser mUser;
    private ArrayList<Messenger> mMessengerList;
    private MessengerRecycleAdapter mAdapter;
    private HashMap<String, Boolean> mVisited;
    private ArrayList<String> mThreadKeyList;
    private ArrayList<String> mDeletedUsers;

    private ShimmerFrameLayout mShimmerViewContainer;
    private Group mEmptyContent;
    private Student mBasicInfo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContent(R.layout.activity_messenger);
        // use super.isUserAuth...
        mUser = FirebaseAuth.getInstance().getCurrentUser();
        if (super.isUserAuthenticated(mUser)) {
            mVisited = new HashMap<>();
            mEmptyContent = findViewById(R.id.messenger_empty_group_id);
            mShimmerViewContainer = findViewById(R.id.messenger_shimmer_container);
            mShimmerViewContainer.startShimmerAnimation();

            importData();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    private void importData() {
        Firebase.getDatabaseReference().
                child("users").
                child(mUser.getUid()).
                child("basicInfo").
                addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        mBasicInfo = dataSnapshot.getValue(Student.class);

                        // use query to fetch only the messages that the current user belongs to
                        Query query = Firebase.getDatabaseReference().child("messenger")
                                .orderByChild("participants/" + mUser.getUid()).equalTo(true);
                        query.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                mMessengerList = new ArrayList<>();
                                mThreadKeyList = new ArrayList<>();
                                mDeletedUsers = new ArrayList<>();
                                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                    mThreadKeyList.add(snapshot.getKey()); // the $threadKey
                                    Messenger messenger = snapshot.child("info").getValue(Messenger.class);
                                    mMessengerList.add(messenger);
                                    mVisited.put(snapshot.getKey(), true);

                                    if (snapshot.child("deleted-users").exists()) {
                                        mDeletedUsers.add(
                                                snapshot.child("deleted-users").
                                                        getValue(String.class));
                                    } else {
                                        mDeletedUsers.add(null);
                                    }

                                }

                                if (mMessengerList.size() == 0) {
                                    mEmptyContent.setVisibility(View.VISIBLE);
                                }
                                mShimmerViewContainer.stopShimmerAnimation();
                                mShimmerViewContainer.setVisibility(View.GONE);
                                setupList();
                                attachChildListener();

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

    private void attachChildListener() {
        FirebaseDatabase.getInstance().getReference().
                child("messenger").
                addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                        if (!mVisited.containsKey(dataSnapshot.getKey())) {
                            // if a new thread is added, check if the thread contains the current user
                            if (dataSnapshot.child("participants").child(mUser.getUid()).exists()) {
                                mThreadKeyList.add(dataSnapshot.getKey());
                                Messenger messenger = dataSnapshot.child("info").getValue(Messenger.class);
                                mMessengerList.add(messenger);
                                mAdapter.notifyItemInserted(mMessengerList.size() - 1);

                                mEmptyContent.setVisibility(View.GONE);

                            }
                        }
                    }

                    @Override
                    public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                        // if a new thread is modified, check if the thread key exists in the current
                        // user's active chat list
                        if (mThreadKeyList.contains(dataSnapshot.getKey())) {
                            // then update the messenger item by using the thread Key list as a reference
                            // in finding the index
                            Messenger messenger = dataSnapshot.child("info").getValue(Messenger.class);
                            int index = mThreadKeyList.indexOf(dataSnapshot.getKey());
                            mMessengerList.set(index, messenger);
                            mAdapter.notifyItemChanged(mMessengerList.size() - 1);

                            if (dataSnapshot.child("deleted-users").exists()) {
                                mDeletedUsers.add(index, dataSnapshot.child("deleted-users").getValue(String.class));
                            }
                        }


                    }

                    @Override
                    public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

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
     * setups the recycler view on the current activity
     */
    private void setupList() {
        RecyclerView recyclerView = findViewById(R.id.messenger_recycle_view_id);
        // adding separator between the items
        recyclerView.addItemDecoration(new DividerItemDecoration(MessengerActivity.this,
                DividerItemDecoration.VERTICAL));
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(MessengerActivity.this);
        mAdapter = new MessengerRecycleAdapter(mMessengerList);
        handleEvents();
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(mAdapter);

    }

    /**
     * @events: profile select
     * @events: select message
     */
    private void handleEvents() {
        mAdapter.setOnItemClickListener(new MessengerRecycleAdapter.OnItemClickListener() {
            @Override
            public void onSelectMessage(int position) {
                // pass the user images and message list to the message activity
                Intent intent = new Intent(MessengerActivity.this, MessageActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("threadKey", mThreadKeyList.get(position));
                if (mUser.getUid().equals(mMessengerList.get(position).getUser1Uid())) {
                    // user 1 is current user, so send the info of user 2 to the bundle
                    bundle.putString("userImage", mMessengerList.get(position).getUser2Image());
                    bundle.putString("userName", mMessengerList.get(position).getUser2Name());
                } else {
                    bundle.putString("userImage", mMessengerList.get(position).getUser1Image());
                    bundle.putString("userName", mMessengerList.get(position).getUser1Name());
                }

                bundle.putString("deletedUser", mDeletedUsers.get(position));

                intent.putExtras(bundle);
                startActivity(intent);
            }

            /*  @Override
              public boolean onLongClick(int position) {
                  if (mActionMode != null) {
                      return false;
                  }

                  mDeleteIndex = position;
                  mActionMode = startSupportActionMode(mActionModeCallback);
                  return true;

              }
  */
            @Override
            public void onProfileClick(int position, final View view) {
                if (mDeletedUsers.get(position) == null) {
                    String senderUid;
                    if (mUser.getUid().equals(mMessengerList.get(position).getUser1Uid())) {
                        // then the sender is user2Uid
                        senderUid = mMessengerList.get(position).getUser2Uid();
                    } else {
                        // then the sender is user1Uid
                        senderUid = mMessengerList.get(position).getUser1Uid();
                    }

                    Firebase.getDatabaseReference().
                            child("users").
                            child(senderUid).
                            child("basicInfo").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            Student senderBasicInfo = dataSnapshot.getValue(Student.class);
                            if (senderBasicInfo != null) {
                                ProfileViewDialog profileViewDialog = new ProfileViewDialog(senderBasicInfo);
                                profileViewDialog.show(getSupportFragmentManager(), TAG);
                            } else {
                                Toast toast = Toast.makeText(MessengerActivity.this,
                                        "Trouble fetching user information. Try again later",
                                        Toast.LENGTH_SHORT);
                                TextView v = toast.getView().findViewById(android.R.id.message);
                                if (v != null) {
                                    v.setGravity(Gravity.CENTER);
                                }
                                toast.show();
                            }

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

                } else {
                    Toast toast = Toast.makeText(MessengerActivity.this, "User account is either de-activated or removed.", Toast.LENGTH_SHORT);
                    TextView v = toast.getView().findViewById(android.R.id.message);
                    if (v != null) {
                        v.setGravity(Gravity.CENTER);
                    }
                    toast.show();
                }


            }
        });
    }


/*
    private ActionMode.Callback mActionModeCallback = new ActionMode.Callback() {
        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            mode.getMenuInflater().inflate(R.menu.messenger_click_menu, menu);
            mode.setTitle("Choose Option");
            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return false;
        }

        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            if (item.getItemId() == R.id.messenger_context_delete) {
                deleteMessage();
                Toast.makeText(MessengerActivity.this, "deleted", Toast.LENGTH_SHORT).show();
                mode.finish();
                return true;
            }
            return false;
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {
            mActionMode = null;

        }
    };

    private void deleteMessage() {
        //mDeleteIndex;
    }*/
}
