package com.valhalla.studiac.activities;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.valhalla.studiac.R;
import com.valhalla.studiac.adapters.dashboard.CustomViewPager;
import com.valhalla.studiac.adapters.dashboard.ViewRoutinePagerAdapter;
import com.valhalla.studiac.database.Firebase;
import com.valhalla.studiac.database.FirebaseListener;
import com.valhalla.studiac.toolbars.NavigationToolbarWhite;
import com.valhalla.studiac.utility.Common;
import com.valhalla.studiac.models.Routine;

import java.util.ArrayList;

public class ViewRoutineActivity extends NavigationToolbarWhite implements FirebaseListener {

    Context mContext; // for debug
    private CustomViewPager mViewPager;
    private ViewRoutinePagerAdapter mRoutineAdapter;
    private ArrayList<Routine> mRoutine;
    private FirebaseListener mFirebaseListener;
    private String mUserUid;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContent(R.layout.activity_view_routine);
        mContext = this;
        mFirebaseListener = this;

        // fetch the transferred list of courses from the parent activity
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            mUserUid = bundle.getString(Common.UID);
        } else {
            Common.showExceptionPrompt(this, "bundle is null");
        }

        importData();
        // createCardView();

    }

    private void importData() {

        Firebase.getDatabaseReference().
                child(mUserUid).
                child(Common.ROUTINE).addListenerForSingleValueEvent(new ValueEventListener() {


            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mFirebaseListener.onLoadSuccess(dataSnapshot);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                mFirebaseListener.onLoadFailure(databaseError);

            }

        });
    }


    @Override
    public void onLoadSuccess(DataSnapshot dataSnapshot) {
        ArrayList<Routine> routine = new ArrayList<>();
        for (DataSnapshot data : dataSnapshot.getChildren()) {
            routine.add(data.getValue(Routine.class));
        }

        mRoutineAdapter = new ViewRoutinePagerAdapter(routine, mContext);
        mViewPager = findViewById(R.id.view_routine_viewPager);
        mViewPager.setAdapter(mRoutineAdapter);
        mViewPager.setPadding(50, 0, 50, 0);
    }

    @Override
    public void onLoadFailure(DatabaseError databaseError) {
        databaseError.toException().printStackTrace();


    }
}



