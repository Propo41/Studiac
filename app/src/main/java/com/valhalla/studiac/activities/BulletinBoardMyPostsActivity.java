package com.valhalla.studiac.activities;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.valhalla.studiac.R;
import com.valhalla.studiac.adapters.bulletinboard.BulletinMyPostsRecycleAdapter;
import com.valhalla.studiac.database.Firebase;
import com.valhalla.studiac.fragments.dialogs.BulletinDeletePromptDialog;
import com.valhalla.studiac.models.Post;
import com.valhalla.studiac.models.Student;
import com.valhalla.studiac.toolbars.NavigationToolbarWhite;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class BulletinBoardMyPostsActivity extends NavigationToolbarWhite {

    private BulletinMyPostsRecycleAdapter mAdapter; // we need to change it to ExampleAdapter object
    private String userUid = "student"; // todo for debug. use actual value later
    private ArrayList<Post> mPosts;
    private ArrayList<String> mPostsKeys; // contains the keys of the posts: for firebase
    private ShimmerFrameLayout mShimmerViewContainer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContent(R.layout.activity_bulletin_board_my_posts);
        initViews();
        // [START] shimmer_effect
        mShimmerViewContainer.startShimmerAnimation();
        checkDbStatus();

        mPosts = new ArrayList<>();
        mPostsKeys = new ArrayList<>();
        importMyPosts();
    }

    private void initViews() {
        mShimmerViewContainer = findViewById(R.id.bulletin_my_posts_shimmer_view_container);
    }

    private void checkDbStatus() {
        DatabaseReference connectedRef = FirebaseDatabase.getInstance().getReference(".info/connected");
        connectedRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                boolean connected = Objects.requireNonNull(snapshot.getValue(Boolean.class), "snapshot cannot be null");
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

    private void setupList() {
        RecyclerView recyclerView = findViewById(R.id.bulletin_my_posts_recycle_view_id);
     /*   recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(),
                DividerItemDecoration.VERTICAL));*/
        // recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);

        mAdapter = new BulletinMyPostsRecycleAdapter(mPosts);
        handleListEvents();
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(mAdapter);

    }

    /*
     * events occurring when the list items are interacted with
     */
    private void handleListEvents() {
        mAdapter.setOnItemClickListener(new BulletinMyPostsRecycleAdapter.OnItemClickListener() {
            @Override
            public void onButtonClick(int position) {
                // open dialog prompt
                openDialog(position);
            }
        });

    }

    /*
     * opens the dialog window to prompt the user
     */
    private void openDialog(final int position) {
        final BulletinDeletePromptDialog dialog = new BulletinDeletePromptDialog();
        dialog.show(getSupportFragmentManager(), "BulletinMyPostActivity");

        dialog.setOnButtonClickListener(new BulletinDeletePromptDialog.OnButtonClickListener() {
            @Override
            public void onConfirmClick() {
                deletePost(position);
                dialog.dismiss();
            }

            @Override
            public void onCancelClick() {
                dialog.dismiss();
            }
        });
    }

    /*
     * deletes the post from both the user's feed and main feed
     * it first fetches the user's university name and then uses it to find the main feed
     */
    private void deletePost(final int position) {
        Firebase.getDatabaseReference().
                child(userUid).
                child(Firebase.BASIC_INFO).
                addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        Student basicInfo = dataSnapshot.getValue(Student.class);
                        Map<String, Object> childUpdates = new HashMap<>();
                        childUpdates.put("/" + basicInfo.getUniversityName() + "/posts/" + mPostsKeys.get(position), null);
                        childUpdates.put("/user-posts/" + userUid + "/" + mPostsKeys.get(position), null);
                        Firebase.getDatabaseReference().updateChildren(childUpdates).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(BulletinBoardMyPostsActivity.this, "Post Deleted Successfully", Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(BulletinBoardMyPostsActivity.this, "Post Deletion Failed", Toast.LENGTH_SHORT).show();

                            }
                        });

                        mPosts.remove(position);
                        mPostsKeys.remove(position);
                        mAdapter.notifyItemRemoved(position);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });


    }

    private void importMyPosts() {
        Firebase.getDatabaseReference().
                child("user-posts").
                child(userUid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    Post post = data.getValue(Post.class);
                    mPosts.add(post);
                    mPostsKeys.add(data.getKey());
                }

                // [STOP] shimmer_effect
                mShimmerViewContainer.stopShimmerAnimation();
                mShimmerViewContainer.setVisibility(View.GONE);
                setupList(); // create the recycler view
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

}
