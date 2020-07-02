package com.valhalla.studiac.activities.settings;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthRecentLoginRequiredException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.valhalla.studiac.R;
import com.valhalla.studiac.activities.LoginActivity;
import com.valhalla.studiac.adapters.SettingsRecycleAdapter;
import com.valhalla.studiac.database.Firebase;
import com.valhalla.studiac.fragments.dialogs.DeleteAccountPromptDialog;
import com.valhalla.studiac.holders.HeaderItem;
import com.valhalla.studiac.holders.ImageItem;
import com.valhalla.studiac.holders.ListItem;
import com.valhalla.studiac.models.Student;
import com.valhalla.studiac.toolbars.NavigationToolbarWhite;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public class SettingsActivity extends NavigationToolbarWhite {

    private FirebaseUser mUser;
    private FirebaseAuth mAuth;
    private final int RESULT_ACC_VERIFIED = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContent(R.layout.activity_settings);

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        if (super.isUserAuthenticated(mUser)) {
            ArrayList<ListItem> listItems = initContent(); // initializes the content for the settings window
            setupList(listItems); // setups the recycler view

            CardView signOutButton = findViewById(R.id.settings_log_out_id);
            signOutButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    signOut();
                }
            });


        }

    }

    /*
     * logs out the user from google or email which ever registered and returns the user to the
     * login activity
     */
    private void signOut() {
        final Intent intent = new Intent(this, LoginActivity.class);
        // adding flags to intent so that in the new activity, pressing back button doesn't bring this activity back
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

        for (UserInfo user : mUser.getProviderData()) {
            if (user.getProviderId().equals(GoogleAuthProvider.GOOGLE_SIGN_IN_METHOD)) {
                // Google sign out
                GoogleSignIn.getClient(
                        getApplicationContext(),
                        new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).build()
                ).signOut().addOnCompleteListener(this,
                        new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Toast.makeText(SettingsActivity.this, "Signing Out", Toast.LENGTH_SHORT).show();
                                startActivity(intent);
                            }
                        });
            }
        }
        // sign out from firebase
        mAuth.signOut();
        Toast.makeText(SettingsActivity.this, "Signing Out", Toast.LENGTH_SHORT).show();
        startActivity(intent);
    }

    private ArrayList<ListItem> initContent() {
        ArrayList<ListItem> listItems = new ArrayList<>();
        listItems.add(new HeaderItem("Account"));
        listItems.add(new ImageItem("Edit Profile", R.drawable.settings_ic_user_edit));
        listItems.add(new ImageItem("Change Password", R.drawable.settings_ic_key));
        listItems.add(new ImageItem("Deactivate Account", R.drawable.settings_ic_delete_user));
        listItems.add(new HeaderItem("Support"));
        listItems.add(new ImageItem("Feedback", R.drawable.settings_ic_feedback_small));
        listItems.add(new ImageItem("Privacy Policy", R.drawable.settings_ic_policy));
        listItems.add(new ImageItem("Report A bug", R.drawable.settings_ic_bug));

        return listItems;
    }

    private void setupList(ArrayList<ListItem> listItems) {
        RecyclerView recyclerView = findViewById(R.id.settings_recycler_view_id);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);

        Toast.makeText(this, isGoogleUser() + "", Toast.LENGTH_SHORT).show();
        SettingsRecycleAdapter adapter = new SettingsRecycleAdapter(listItems, isGoogleUser(), getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        handleEvents(adapter);
        recyclerView.setAdapter(adapter);

    }

    private boolean isGoogleUser() {
        GoogleSignInAccount googleSignInAccount = GoogleSignIn.getLastSignedInAccount(getApplicationContext());
        return googleSignInAccount != null;
    }

    private void handleEvents(SettingsRecycleAdapter adapter) {
        adapter.setOnItemClickListener(new SettingsRecycleAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                switch (position) {
                    case 1:
                        // edit profile clicked
                        startActivity(new Intent(SettingsActivity.this, EditProfileActivity.class));
                        break;
                    case 2:
                        // change password clicked
                        startActivity(new Intent(SettingsActivity.this, ChangePasswordActivity.class));
                        break;
                    case 3:
                        // delete user
                        // open dialog prompt
                        deleteUserPrompt();
                        break;
                    case 5:
                        // feedback clicked
                        startActivity(new Intent(SettingsActivity.this, FeedbackActivity.class));
                        break;
                    case 6:
                        // policy clicked
                        break;
                    case 7:
                        // report bug clicked
                        startActivity(new Intent(SettingsActivity.this, ReportBugActivity.class));
                        break;
                }

            }
        });
    }

    /**
     * fetches the result after verifying user account from activity VerifyAccountActivity
     * if data passed is true, then continue on deleting the account
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        SettingsActivity.super.startLoadingAnimation();
        if (requestCode == RESULT_ACC_VERIFIED && resultCode == RESULT_OK) {
            if (data != null) {
                Bundle bundle = data.getExtras();
                if (bundle != null) {
                    if (bundle.getBoolean("verified")) {
                        mUser.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                deleteUserData();
                                Toast.makeText(SettingsActivity.this, "Deleting user data", Toast.LENGTH_SHORT).show();
                                SettingsActivity.super.stopLoadingAnimation();

                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(SettingsActivity.this, "Couldn't Delete Account", Toast.LENGTH_SHORT).show();
                                Log.d("FIREBASE", e.toString());
                                SettingsActivity.super.stopLoadingAnimation();

                            }
                        });
                    }
                }

            }
        }else{
            SettingsActivity.super.stopLoadingAnimation();
        }
    }

    private void deleteUserPrompt() {
        final DeleteAccountPromptDialog dialog = new DeleteAccountPromptDialog();
        dialog.setOnButtonClickListener(new DeleteAccountPromptDialog.OnButtonClickListener() {
            @Override
            public void onYesClick() {
                // delete account and log out
                dialog.dismiss();
                // re-authenticate user
                startActivityForResult(new Intent(SettingsActivity.this, VerifyAccountActivity.class), RESULT_ACC_VERIFIED);
            }


            @Override
            public void onNoClick() {
                dialog.dismiss();

            }
        });
        dialog.show(getSupportFragmentManager(), "SettingsActivity");
    }

    /*
     * deletes all user related info from real time database
     */
    private void deleteUserData() {
        final DatabaseReference reference = Firebase.getDatabaseReference();
        // retrieve user's university name and user's post keys
        reference.
                child(Firebase.USERS).
                child(mUser.getUid()).
                child(Firebase.BASIC_INFO).
                addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        Student student = dataSnapshot.getValue(Student.class);
                        assert student != null;
                        final String university = student.getUniversityName();

                        System.out.println("university obtained: " + university);
                        reference.
                                child(Firebase.USER_POSTS).
                                child(mUser.getUid()).
                                addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        // retrieve the user's post keys from user-posts/$userUid/
                                        // and then put them in a hash map

                                        // [START] delete all user related info from database
                                        final HashMap<String, Object> childUpdates = new HashMap<>();
                                        for (DataSnapshot data : dataSnapshot.getChildren()) {
                                            String key = data.getKey();
                                            // delete the posts with the keys from universities/university/posts/...
                                            childUpdates.put("/universities/" + university + "/posts/" + key, null);
                                            // delete the posts with the keys from user-posts/$userUid/...
                                        }

                                        // delete all posts made by the user in user-posts/$userUid/...
                                        childUpdates.put("/user-posts/" + mUser.getUid(), null);
                                        // delete the user from the /university/users/$userUid
                                        // but first need to fetch the uid using query

                                        reference.child(Firebase.UNIVERSITIES).
                                                child(university).
                                                child(Firebase.USERS).
                                                addListenerForSingleValueEvent(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                        String key = null;
                                                        for (DataSnapshot data : dataSnapshot.getChildren()) {
                                                            if (Objects.equals(data.getValue(), mUser.getUid())) {
                                                                key = data.getKey();
                                                                break;
                                                            }
                                                        }

                                                        if (key != null) {
                                                            childUpdates.put("/universities/" + university + "/users/" + key, null);

                                                            reference.updateChildren(childUpdates).addOnCompleteListener(
                                                                    new OnCompleteListener<Void>() {
                                                                        @Override
                                                                        public void onComplete(@NonNull Task<Void> task) {
                                                                            // delete basic info of user from path: /users/$userUid/basicInfo/...
                                                                            reference.
                                                                                    child(Firebase.USERS).
                                                                                    child(mUser.getUid()).
                                                                                    setValue(null).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                @Override
                                                                                public void onComplete(@NonNull Task<Void> task) {
                                                                                    Toast.makeText(SettingsActivity.this,
                                                                                            "Account Deleted Successfully",
                                                                                            Toast.LENGTH_SHORT).show();
                                                                                    SettingsActivity.super.stopLoadingAnimation();

                                                                                    signOut();
                                                                                }
                                                                            }).addOnFailureListener(new OnFailureListener() {
                                                                                @Override
                                                                                public void onFailure(@NonNull Exception e) {
                                                                                    Log.d("FIREBASE ACC DELETE", e.toString());
                                                                                    Toast.makeText(SettingsActivity.this,
                                                                                            "Something went wrong",
                                                                                            Toast.LENGTH_SHORT).show();
                                                                                }
                                                                            }).addOnFailureListener(new OnFailureListener() {
                                                                                @Override
                                                                                public void onFailure(@NonNull Exception e) {
                                                                                    Log.d("FIREBASE ACC DELETE", e.toString());

                                                                                    Toast.makeText(SettingsActivity.this,
                                                                                            "Something went wrong",
                                                                                            Toast.LENGTH_SHORT).show();

                                                                                }
                                                                            });
                                                                        }
                                                                    });
                                                        } else {
                                                            Log.d("DeactivateActivity", "error. no key found ");
                                                        }


                                                    }

                                                    @Override
                                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                                    }
                                                });


                                        // [END] delete all user related info from database

                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {
                                        Log.d("FIREBASE", databaseError.getMessage());
                                        Toast.makeText(SettingsActivity.this, "Connection Error", Toast.LENGTH_SHORT).show();
                                    }
                                });
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Log.d("FIREBASE", databaseError.getMessage());
                        Toast.makeText(SettingsActivity.this, "Connection Error", Toast.LENGTH_SHORT).show();

                    }
                });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // don't inflate anything here
        return true;
    }
}
