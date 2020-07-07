package com.valhalla.studiac.toolbars;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.res.ResourcesCompat;

import com.google.firebase.auth.FirebaseUser;
import com.valhalla.studiac.R;
import com.valhalla.studiac.activities.LoginActivity;
import com.valhalla.studiac.fragments.dialogs.CustomProgressDialog;

/*
Contains the up button along with the more icon on the toolbar.
Each items can be updated from their corresponding layout files.
Activities/Fragments which inherit this class will automatically contain the custom toolbar with the
navigation facilities.
 */
public class NavigationToolbarWhite extends AppCompatActivity {
    protected FrameLayout mFrameLayout;
    protected CustomProgressDialog mProgressDialog;
    protected Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.toolbar_back_white);

        mProgressDialog = new CustomProgressDialog(NavigationToolbarWhite.this);
        mToolbar = findViewById(R.id.toolbar_white);

        //mToolbar.setElevation(0);
        mFrameLayout = findViewById(R.id.view_container);
        setSupportActionBar(mToolbar);

        // changing font family of toolbar title to semi bold
        TextView titleView = (TextView) mToolbar.getChildAt(0);
        Typeface typeface = ResourcesCompat.getFont(getApplicationContext(), R.font.montserrat_semi_bold);
        titleView.setTypeface(typeface);

        // changes the default back button
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.toolbar_ic_back);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

    }

    protected boolean isUserAuthenticated(FirebaseUser user) {
        if (user == null) {
            Toast.makeText(this, "Session expired. Log in again!", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, LoginActivity.class);
            // adding flags to intent so that in the new activity, pressing back button doesn't bring this activity back
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            return false;
        }
        return true;

    }

    /*
     * activates loading animation from child activities
     */
    public void startLoadingAnimation() {
        mProgressDialog.startLoadingAnimation();
    }

    /*
     * stops loading animation from child activities
     */
    public void stopLoadingAnimation() {
        mProgressDialog.stopLoadingAnimation();
    }


    protected void setContent(int layout) {
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        //inflate the activity layout
        View contentView = inflater.inflate(layout, null, false);
        mFrameLayout.addView(contentView, 0);

    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {// finish(); // go back to parent activity
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
