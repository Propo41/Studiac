package com.valhalla.studiac.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
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

/*
 * no routine will be edited from here. Only shown
 */
public class ViewRoutineActivity extends NavigationToolbarWhite {

    private Context mContext;
    private CustomViewPager mViewPager;
    private ViewRoutinePagerAdapter mRoutineAdapter;
    private FirebaseUser mUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContent(R.layout.activity_view_routine);

        mUser = FirebaseAuth.getInstance().getCurrentUser();
        if (isUserAuthenticated(mUser)) {
            mContext = this;
            importData();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    private void importData() {
        // [START] shimmer_effect
        Firebase.getDatabaseReference().
                child(Firebase.USERS).
                child(mUser.getUid()).
                child(Common.ROUTINE).
                addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
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
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Log.i("DB ERROR", databaseError.getDetails());
                    }

                });

        // [END] shimmer_effect

    }


}



