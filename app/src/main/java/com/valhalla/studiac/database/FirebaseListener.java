package com.valhalla.studiac.database;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;

public interface FirebaseListener {
    void onLoadSuccess(DataSnapshot dataSnapshot);

    void onLoadFailure(DatabaseError databaseError);
}
