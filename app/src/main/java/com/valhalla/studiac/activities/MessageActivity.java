package com.valhalla.studiac.activities;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.constraintlayout.widget.Group;
import androidx.core.app.NotificationCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.valhalla.studiac.R;
import com.valhalla.studiac.adapters.messenger.MessageRecycleAdapter;
import com.valhalla.studiac.database.Firebase;
import com.valhalla.studiac.models.Message;
import com.valhalla.studiac.toolbars.NavigationToolbarWhite;
import com.valhalla.studiac.utility.SharedPreference;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class MessageActivity extends NavigationToolbarWhite implements View.OnClickListener {

    private FirebaseUser mUser;
    private static final int BROADCAST_NOTIFICATION_ID = 1;
    private static final String CHANNEL_ID = "channel_id";
    private static final int NOTIFICATION_ID = 2;
    private ArrayList<Message> mMessages;
    private String threadKey;
    private String mUserImage;
    private MessageRecycleAdapter mAdapter;
    private FloatingActionButton mSendMessageButton;
    private EditText mChatMessage;
    private final String TAG = "MessageActivity";
    private HashMap<String, Boolean> mVisited;
    private RecyclerView mRecyclerView;
    private String mUserName;
    private ShimmerFrameLayout mShimmerViewContainer;
    private String mDeletedUser;
    private Group mDeletedPromptGroup;
    private Group mInputGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContent(R.layout.activity_message);
        mUser = FirebaseAuth.getInstance().getCurrentUser();

        if (super.isUserAuthenticated(mUser)) {
            mVisited = new HashMap<>();
            mSendMessageButton = findViewById(R.id.message_send_message_button_id);
            mShimmerViewContainer = findViewById(R.id.message_shimmer_container);
            mDeletedPromptGroup = findViewById(R.id.message_deleted_notification_group);
            mInputGroup = findViewById(R.id.message_input_group);

            mSendMessageButton.setOnClickListener(this);
            mChatMessage = findViewById(R.id.message_input_text_id);

            Bundle bundle = getIntent().getExtras();
            if (bundle != null) {
                // mMessages = bundle.getParcelableArrayList("messages");
                // markVisited();
                threadKey = bundle.getString("threadKey");
                mUserImage = bundle.getString("userImage");
                mUserName = bundle.getString("userName");
                mDeletedUser = bundle.getString("deletedUser");
                ActionBar actionBar = getSupportActionBar();
                if (actionBar != null) {
                    getSupportActionBar().setTitle(mUserName);
                }
                mShimmerViewContainer.startShimmerAnimation();
                importData();

            } else {
                Log.d(TAG, "bundle is null");
            }


        }


    }

    @Override
    protected void onResume() {
        super.onResume();
        SharedPreference preference = SharedPreference.getInstance(getApplicationContext());
        preference.saveData(String.class.getSimpleName(), getString(R.string.current_activity), MessageActivity.class.getSimpleName());
    }

    @Override
    protected void onPause() {
        super.onPause();
        SharedPreference preference = SharedPreference.getInstance(getApplicationContext());
        preference.saveData(String.class.getSimpleName(), getString(R.string.current_activity), null);
    }


    private void importData() {
        Firebase.getDatabaseReference().
                child("chat-messages").
                child(threadKey).
                addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        mMessages = new ArrayList<>();
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            Message message = snapshot.getValue(Message.class);
                            mMessages.add(message);
                            mVisited.put(snapshot.getKey(), true);
                        }
                        mShimmerViewContainer.stopShimmerAnimation();
                        mShimmerViewContainer.setVisibility(View.GONE);

                        setupList();
                        //if it's null, it means there are no deleted users in the thread
                        if (mDeletedUser == null) {
                            attachListener();
                        } else {
                            // show the text at bottom that user is deleted
                            mInputGroup.setVisibility(View.GONE);
                            mDeletedPromptGroup.setVisibility(View.VISIBLE);
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

    }


    private void attachListener() {
        Firebase.getDatabaseReference().
                child("chat-messages").
                child(threadKey).
                addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                        Message message = dataSnapshot.getValue(Message.class);
                        if (!mVisited.containsKey(dataSnapshot.getKey())) {
                            mMessages.add(message);
                            // scrollToPosition(mMessages.size() - 1);
                            mAdapter.notifyItemInserted(mMessages.size() - 1);
                            // mRecyclerView.smoothScrollToPosition(mMessages.size() - 1);
                            mRecyclerView.scrollToPosition(mMessages.size() - 1);

                            // notify user of the new message iff the message is not sent from the current user
                            // and the user is not inside messageActivity

                            notifyUser(message);
                        }

                    }

                    @Override
                    public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

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

    /**
     * creates a notification
     *
     * @param message the new message that is received
     */
    private void notifyUser(Message message) {
        SharedPreference preference = SharedPreference.getInstance(getApplicationContext());
        String currentActivity = (String) preference.getData(String.class.getSimpleName(), getString(R.string.current_activity));
        if ((currentActivity == null || !currentActivity.equals(MessageActivity.class.getSimpleName()))
                && !message.getUserUid().equals(mUser.getUid())) {

            Intent intent = new Intent(this, MessengerActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            PendingIntent resultPendingIntent = PendingIntent.getActivity(this,
                    BROADCAST_NOTIFICATION_ID, intent, PendingIntent.FLAG_UPDATE_CURRENT);
            createNotification(mUserName, message.getText(), resultPendingIntent);


        }

    }


    private void createNotification(String title, String content, PendingIntent resultPendingIntent) {
        // build the notification
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle(title)
                .setContentText(content)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setStyle(new NotificationCompat.BigTextStyle())
                .setLargeIcon(BitmapFactory.decodeResource(getResources(),
                        R.mipmap.ic_launcher))
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                .setSmallIcon(R.mipmap.ic_launcher) // add logo here
                .setAutoCancel(true);

        if (resultPendingIntent != null) {
            notificationBuilder.setContentIntent(resultPendingIntent);
        }


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(createNotificationChannel());
            notificationManager.notify(NOTIFICATION_ID, notificationBuilder.build());

        } else {
            NotificationManager notificationManager =
                    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

            notificationManager.notify(NOTIFICATION_ID, notificationBuilder.build());
        }
    }

    /**
     * @return channel a channel for the "bulletin post" notification
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    private NotificationChannel createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library

        CharSequence name = getString(R.string.channel_name_post);
        String description = getString(R.string.channel_post_description);
        int importance = NotificationManager.IMPORTANCE_DEFAULT;
        NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
        channel.setDescription(description);
        channel.setShowBadge(true); // a small dot on the app icon if it has any notifications
        // Register the channel with the system; you can't change the importance
        // or other notification behaviors after this

        return channel;


    }

    /*
     * setups the recycler view on the current activity
     */
    private void setupList() {
        mRecyclerView = findViewById(R.id.message_recycle_view_id);
       /* recyclerView.addItemDecoration(new DividerItemDecoration(MessageActivity.this,
                DividerItemDecoration.VERTICAL));*/
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(MessageActivity.this);
        mAdapter = new MessageRecycleAdapter(mMessages, mUserImage, mUser.getUid(), getApplicationContext());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
        // scrollToPosition(mMessages.size()-1);

        mRecyclerView.smoothScrollToPosition(mMessages.size() - 1);
    }


    /**
     * @return a long corresponding to the current time in ms
     */
    private long getCurrentTime() {
        return new Date().getTime();
    }

    /**
     * sends a message to the user after the clicking the send button
     *
     * @param view the send button
     */
    @Override
    public void onClick(View view) {
        if (view.getId() == mSendMessageButton.getId()) {
            String messageText = mChatMessage.getText().toString();

            if (!messageText.equals("")) {
                // upload text to database
                mChatMessage.setText("");
                long currentTime = getCurrentTime();
                HashMap<String, Object> childUpdates = new HashMap<>();
                childUpdates.put("/messenger/" + threadKey + "/info/lastMessage", messageText);
                childUpdates.put("/messenger/" + threadKey + "/info/time", currentTime);

                String key = FirebaseDatabase.getInstance().getReference().
                        child("chat-messages").
                        child(threadKey).
                        push().getKey();

                final Message message = new Message(messageText, currentTime, mUser.getUid());
                childUpdates.put("/chat-messages/" + threadKey + "/" + key, message);
                FirebaseDatabase.getInstance().getReference().
                        updateChildren(childUpdates).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        //Toast.makeText(MessageActivity.this, "message uploaded", Toast.LENGTH_SHORT).show();
                        Log.d(TAG, "message sent");
                        // saving to local cache to show it
                       /* SharedPreference sharedPreference = SharedPreference.getInstance(getApplicationContext());
                        sharedPreference.saveData(String.class.getSimpleName(), getString(R.string.last_message_sent), messageText);*/
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(MessageActivity.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                    }
                });


            }
        }

    }


}
