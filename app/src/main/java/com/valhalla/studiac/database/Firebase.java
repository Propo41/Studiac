package com.valhalla.studiac.database;

import android.app.Application;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.valhalla.studiac.utility.common.Student;

import java.util.ArrayList;
import java.util.HashMap;

public class Firebase{

    private FirebaseDatabase mDatabase;
    private DatabaseReference mDatabaseReference;

    private void read() {

        mDatabaseReference.child("Eeevee").child("list").addValueEventListener(new ValueEventListener() {
            ArrayList<Object> list = new ArrayList<>();
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    list.add(postSnapshot.getValue());
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void write() {

     //   Student student = new Student(mEditText.getText().toString(), 12, "Male", list, map);
     //   mDatabaseReference.child(student.getName()).setValue(student);
        // mDatabaseReference.child("users").setValue(name);

    }

    private void checkDbStatus() {
        DatabaseReference connectedRef = FirebaseDatabase.getInstance().getReference(".info/connected");
        connectedRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                boolean connected = snapshot.getValue(Boolean.class);
                if (connected) {
                 //   Toast.makeText(getApplicationContext(), "connected", Toast.LENGTH_SHORT).show();
                } else {
                   // Toast.makeText(getApplicationContext(), "not connected", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.i("TAG: ", "Listener was cancelled");
            }
        });
    }
}
