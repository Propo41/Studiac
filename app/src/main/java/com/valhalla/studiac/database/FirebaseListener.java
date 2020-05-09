package com.valhalla.studiac.database;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.valhalla.studiac.utility.common.Course;
import com.valhalla.studiac.utility.common.Routine;
import com.valhalla.studiac.utility.common.Student;

import java.util.ArrayList;

public interface FirebaseListener {
    void onLoadSuccess(DataSnapshot dataSnapshot);

    void onLoadFailure(DatabaseError databaseError);
}
