package com.valhalla.studiac.activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.BaseTransientBottomBar;
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
    private EditGradeWeightRecycleAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private ConstraintLayout constraintLayout;
    private Button mUpdateButton;
    private ArrayList<Weight> mWeightList;
    private Boolean mWeightUpdated;
    private FirebaseUser mUser;
    private int clickCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContent(R.layout.activity_edit_weight);
        mUser = FirebaseAuth.getInstance().getCurrentUser();
        if (isUserAuthenticated(mUser)) {
            super.startLoadingAnimation();
            importData();
        }


    }

    private void importData() {
        // import data from firebase
        // if grade weights already edited, then user cant edit anymore
        Firebase.getDatabaseReference().
                child(Firebase.USERS).
                child(mUser.getUid()).
                child(Firebase.WEIGHTS).
                child("updated").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mWeightUpdated = dataSnapshot.getValue(Boolean.class);

                Firebase.getDatabaseReference().
                        child(Firebase.USERS).
                        child(mUser.getUid()).
                        child(Firebase.WEIGHTS).
                        child("list").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        mWeightList = new ArrayList<>();
                        for (DataSnapshot data : dataSnapshot.getChildren()) {
                            mWeightList.add(data.getValue(Weight.class));
                        }

                        EditWeightActivity.super.stopLoadingAnimation();

                        //initializeData();
                        initializeViews();
                        initializeRecyclerViews();
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
            clickCount = -5;
        }else{
            clickCount = 0;
        }

    }

    private void initializeRecyclerViews() {
        mRecycleView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getApplicationContext());
        mRecycleView.setLayoutManager(mLayoutManager);
        if (mWeightUpdated) {
            mAdapter = new EditGradeWeightRecycleAdapter(mWeightList, getApplicationContext(), false);
        } else {
            mAdapter = new EditGradeWeightRecycleAdapter(mWeightList, getApplicationContext(), true);
        }
        mRecycleView.setAdapter(mAdapter);

        mUpdateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(clickCount==0) {
                    final Snackbar snackbar = Snackbar.make(constraintLayout, " You won't be able to make any changes after you " +
                            "update the values. Make sure you provide the " +
                            "correct values.", Snackbar.LENGTH_LONG);
                    View snackView = snackbar.getView();
                    TextView textView = snackView.findViewById(com.google.android.material.R.id.snackbar_text);
                    textView.setMaxLines(4);
                    textView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_result_question, 0, 0, 0);
                    textView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                        }
                    });
                    //textView.getGravity(Gravity.CENTER);
                    textView.setCompoundDrawablePadding(30);
                    textView.setPadding(30, 0, 0, 0);
                    snackbar.show();
                    clickCount++;
                }else if(clickCount==1){
                    mWeightUpdated = true;
                    HashMap<String, Object> childUpdates = new HashMap<>();
                    childUpdates.put("updated", true);
                    childUpdates.put("list", mWeightList);

                    Firebase.getDatabaseReference().
                            child(Firebase.USERS).
                            child(mUser.getUid()).
                            child(Firebase.WEIGHTS).updateChildren(childUpdates).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(EditWeightActivity.this, "Grade Weights Updated!", Toast.LENGTH_SHORT).show();
                            Intent resultIntent = new Intent();
                            resultIntent.putExtra("weightsUpdated", true);
                            setResult(RESULT_OK, resultIntent);
                            finish();
                        }
                    });
                }
            }

        });
    }
}

