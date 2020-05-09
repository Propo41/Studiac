package com.valhalla.studiac.toolbars;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import com.valhalla.studiac.R;
import com.valhalla.studiac.utility.common.Common;

/*
Contains the up button along with the more icon on the toolbar.
Each items can be updated from their corresponding layout files.
Activities/Fragments which inherit this class will automatically contain the custom toolbar with the
navigation facilities.
 */
public class NavigationToolbarWhite extends AppCompatActivity {


    protected int childActivity; // used to control what happens in the toolbar actions for each different activity
    protected FrameLayout mFrameLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.toolbar_back_white);
        Toolbar mToolbar = findViewById(R.id.toolbar_white);
        mFrameLayout = findViewById(R.id.view_container);
        setSupportActionBar(mToolbar);
        // changes the default back button
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.toolbar_ic_back);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }


    public void setContent(int layout) {
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        //inflate the activity layout
        View contentView = inflater.inflate(layout, null, false);
        mFrameLayout.addView(contentView, 0);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.options_menu, menu);
        // changing the colour of the options menu to blue
        menu.getItem(0).setIcon(ContextCompat.getDrawable(this, R.drawable.toolbar_ic_more_blue));
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                if (childActivity == Common.VIEW_COURSES) {
                    onBackPressed();
                } else {
                    finish(); // go back to parent activity
                }
                return true;
            case R.id.main_menu_id:
                Toast.makeText(this, "Default: Main Menu", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.settings_id:
                Toast.makeText(this, "Default: Settings", Toast.LENGTH_SHORT).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);


        }
    }
}
