package com.valhalla.studiac.activities;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.valhalla.studiac.R;
import com.valhalla.studiac.adapters.result_tracker.EditGradeWeightRecycleAdapter;
import com.valhalla.studiac.database.Firebase;
import com.valhalla.studiac.models.Weight;
import com.valhalla.studiac.toolbars.NavigationToolbarWhite;

import java.util.ArrayList;
import java.util.HashMap;

public class EditWeightActivity extends NavigationToolbarWhite {

    private RecyclerView mRecycleView;
    private ConstraintLayout constraintLayout;
    private Button mUpdateButton;
    private ArrayList<Weight> mWeightList;
    private Boolean mWeightUpdated;
    private FirebaseUser mUser;
    private int clickCount;
    private ShimmerFrameLayout mShimmerViewContainer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContent(R.layout.activity_edit_weight);
        mUser = FirebaseAuth.getInstance().getCurrentUser();
        if (isUserAuthenticated(mUser)) {
            mShimmerViewContainer = findViewById(R.id.edit_weight_shimmer_layout);
            mShimmerViewContainer.startShimmerAnimation();
            importData();


        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    private void importData() {
        // import data from firebase
        // if grade weights already edited, then user cant edit anymore
        Firebase.getDatabaseReference().
                child(Firebase.USERS).
                child(mUser.getUid()).
                child(Firebase.WEIGHTS).
                child("updated").
                addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        mWeightUpdated = dataSnapshot.getValue(Boolean.class);
                        Firebase.getDatabaseReference().
                                child(Firebase.USERS).
                                child(mUser.getUid()).
                                child(Firebase.WEIGHTS).
                                child("list").
                                addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        mWeightList = new ArrayList<>();
                                        for (DataSnapshot data : dataSnapshot.getChildren()) {
                                            mWeightList.add(data.getValue(Weight.class));
                                        }

                                        mShimmerViewContainer.stopShimmerAnimation();
                                        mShimmerViewContainer.setVisibility(View.GONE);
                                        initializeViews();
                                        setupRecyclerView();
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

    private void initializeViews() {
        mRecycleView = findViewById(R.id.edit_weight_bottom_sheet_recyclerview);
        mUpdateButton = findViewById(R.id.edit_weight_bottom_sheet_updatebtn);
        constraintLayout = findViewById(R.id.constraint_Layout_edit_weight_activity);

        if (mWeightUpdated) {
            mUpdateButton.setVisibility(View.GONE);
            clickCount = -5; // we can use any negative values to flag clickCount
        } else {
            clickCount = 0;
            mUpdateButton.setVisibility(View.VISIBLE);

        }

    }

    private void setupRecyclerView() {
        mRecycleView.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        mRecycleView.setLayoutManager(mLayoutManager);
        EditGradeWeightRecycleAdapter mAdapter;
        if (mWeightUpdated) {
            mAdapter = new EditGradeWeightRecycleAdapter(mWeightList, false);
        } else {
            mAdapter = new EditGradeWeightRecycleAdapter(mWeightList, true);
        }
        mRecycleView.setAdapter(mAdapter);


        if (!mWeightUpdated) {
            mRecycleView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);
                    if (dy > 0) {
                        mUpdateButton.setVisibility(View.GONE);

                        mUpdateButton.animate().
                                scaleX(0.0f).
                                scaleY(0.0f).
                                alpha(0.0f).
                                setDuration(100);

                    } else if (dy < 0) {
                        mUpdateButton.setVisibility(View.VISIBLE);

                        mUpdateButton.animate().
                                scaleX(1.0f).
                                scaleY(1.0f).
                                alpha(1.0f).
                                setDuration(100);

                    }
                }
            });
        }


        mUpdateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (clickCount == 0) {
                    final Snackbar snackbar = Snackbar.make(
                            constraintLayout,
                            getString(R.string.snackbar_edit_weight_confirmation),
                            Snackbar.LENGTH_LONG);
                    View snackView = snackbar.getView();
                    TextView textView = snackView.findViewById(com.google.android.material.R.id.snackbar_text);
                    textView.setMaxLines(4);
                    textView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_result_question,
                            0, 0, 0);
                    textView.setCompoundDrawablePadding(30);
                    textView.setPadding(30, 0, 0, 0);
                    snackbar.show();
                    mUpdateButton.setText(getString(R.string.forgotPassword_Confirm_Btn));
                    clickCount++;

                } else if (clickCount == 1) {
                    mWeightUpdated = true;
                    HashMap<String, Object> childUpdates = new HashMap<>();
                    childUpdates.put("updated", true);
                    childUpdates.put("list", mWeightList);

                    Firebase.getDatabaseReference().
                            child(Firebase.USERS).
                            child(mUser.getUid()).
                            child(Firebase.WEIGHTS).updateChildren(childUpdates).
                            addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toast.makeText(EditWeightActivity.this,
                                            "Grade Weights Updated!",
                                            Toast.LENGTH_SHORT).show();
                                    Intent resultIntent = new Intent();
                                    resultIntent.putExtra("weightsUpdated", true);
                                    setResult(RESULT_OK, resultIntent);
                                    mUpdateButton.setVisibility(View.GONE);
                                    finish();
                                }
                            });

                }

            }

        });

    }


}

