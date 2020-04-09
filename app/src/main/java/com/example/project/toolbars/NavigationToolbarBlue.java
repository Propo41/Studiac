package com.example.project.toolbars;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.project.R;
import com.google.android.material.navigation.NavigationView;

/*
Contains both the navigation bar along with the hamburger icon and the more icon on the toolbar.
Each items can be updated from their corresponding layout files.

Activities/Fragments which inherit this class will automatically contain the custom toolbar with the
navigation facilities.
When inheriting this class, make sure to call the method setContent(layout_of_activity/fragment)
from the onCreate() method.
 */
public class NavigationToolbarBlue extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener  {

    protected FrameLayout mFrameLayout;
    protected DrawerLayout mDrawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.toolbar_navigation_blue);
        setupDrawer();
    }

    private void setupDrawer() {

        Toolbar toolbar = findViewById(R.id.toolbar_blue);
        mFrameLayout = findViewById(R.id.view_container_blue);
        setSupportActionBar(toolbar);

        mDrawer = findViewById(R.id.drawer_layout);
        // adding the builtin hamburger icon
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, mDrawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawer.addDrawerListener(toggle);
        toggle.syncState(); // this will handle the animation of the icon

        // to interact with the navigation items, we need to reference it first
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.nav_dashboard); // initially this will be checked

    }

    public void setContent(int layout){
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        //inflate the activity layout
        View contentView = inflater.inflate(layout, null, false);
        mFrameLayout.addView(contentView, 0);

    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.main_menu_id:
                Toast.makeText(this, "Main Menu", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.settings_id:
                Toast.makeText(this, "Settings", Toast.LENGTH_SHORT).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);


        }
    }

    @Override
    public void onBackPressed() {
        // if the drawer is open, close the drawer instead
        if(mDrawer.isDrawerOpen(GravityCompat.START)){
            mDrawer.closeDrawer(GravityCompat.START);
        }else{
            super.onBackPressed();

        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.options_menu, menu);
        return true;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_dashboard:
                // do nothing, as it's already here
                break;
            case R.id.nav_gpaCalculator:
                break;
            case R.id.nav_toDoTask:
                break;
            case R.id.nav_bulletinBoard:
                break;
            case R.id.nav_messages:
                break;
            case R.id.nav_editProfile:
                break;
            case R.id.nav_updateSemester:
                break;
            case R.id.nav_helpSupport:
                break;

        }

        mDrawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
