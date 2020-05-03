package com.example.project.activities;

import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.viewpager2.widget.ViewPager2;
import com.example.project.R;
import com.example.project.adapters.bulletinboard.BulletinPagerAdapter;
import com.example.project.toolbars.NavigationToolbarWhite;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class BulletinBoardActivity extends NavigationToolbarWhite {

    private int[] tabIcons = {R.drawable.bulletin_ic_official, R.drawable.bulletin_ic_advertisement, R.drawable.bulletin_ic_help};
    private FloatingActionButton fab_main, fab_official, fab_ad, fab_help;
    private Animation fab_open_anim, fab_close_anim, fab_rotate_clockwise_anim, fab_rotate_anticlockwise_anim;
    private TextView fab_add_prompt, fab_official_prompt, fab_ad_prompt, fab_help_prompt ;
    private boolean isPopUpOpen = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContent(R.layout.activity_bulletin_board);

        initViews();
        initAnimations();
        handlePopUpEvents();
        handleEvents();
        setupTabLayout();

    }

    private void handlePopUpEvents() {
        fab_main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // if pop up menu is open, close it, else open it
                if(isPopUpOpen){
                    // closing animation for prompts
                    fab_add_prompt.startAnimation(fab_close_anim);
                    fab_official_prompt.startAnimation(fab_close_anim);
                    fab_ad_prompt.startAnimation(fab_close_anim);
                    fab_help_prompt.startAnimation(fab_close_anim);

                    // closing animation for buttons
                    fab_official.startAnimation(fab_close_anim);
                    fab_ad.startAnimation(fab_close_anim);
                    fab_help.startAnimation(fab_close_anim);
                    fab_main.startAnimation(fab_rotate_anticlockwise_anim);

                    // setting the popup buttons to un-clickable
                    fab_official.setClickable(false);
                    fab_ad.setClickable(false);
                    fab_help.setClickable(false);
                    isPopUpOpen = false;

                }else{
                    // opening animation for prompts
                    fab_add_prompt.startAnimation(fab_open_anim);
                    fab_official_prompt.startAnimation(fab_open_anim);
                    fab_ad_prompt.startAnimation(fab_open_anim);
                    fab_help_prompt.startAnimation(fab_open_anim);

                    // opening animation for buttons
                    fab_official.startAnimation(fab_open_anim);
                    fab_ad.startAnimation(fab_open_anim);
                    fab_help.startAnimation(fab_open_anim);
                    fab_main.startAnimation(fab_rotate_clockwise_anim);

                    // setting the popup buttons to clickable
                    fab_official.setClickable(true);
                    fab_ad.setClickable(true);
                    fab_help.setClickable(true);

                    isPopUpOpen = true;
                }
            }
        });
    }

    private void initAnimations() {
        fab_open_anim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.view_scale_up);
        fab_close_anim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.view_scale_down);
        fab_rotate_clockwise_anim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_rotate_clockwise);
        fab_rotate_anticlockwise_anim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_rotate_anticlockwise);

    }

    private void initViews() {
        fab_main = findViewById(R.id.bulletin_add_post_button_id);
        fab_official = findViewById(R.id.bulletin_add_post_official_button_id);
        fab_ad = findViewById(R.id.bulletin_add_post_advertisement_button_id);
        fab_help = findViewById(R.id.bulletin_add_post_help_button_id);

        fab_add_prompt = findViewById(R.id.bulletin_add_post_add_prompt_id);
        fab_official_prompt = findViewById(R.id.bulletin_add_post_official_prompt_id);
        fab_ad_prompt = findViewById(R.id.bulletin_add_post_advertisement_prompt_id);
        fab_help_prompt = findViewById(R.id.bulletin_add_post_help_prompt_id);
    }

    private void handleEvents() {
        // when button - official is pressed
        fab_official.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // todo: open dialog 19
                Toast.makeText(getApplicationContext(), "official", Toast.LENGTH_SHORT).show();
            }
        });

        // when button - advertisement is pressed
        fab_ad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // todo: open dialog 19
                Toast.makeText(getApplicationContext(), "ad", Toast.LENGTH_SHORT).show();
            }
        });

        // when button - help is pressed
        fab_help.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // todo: open dialog 19
                Toast.makeText(getApplicationContext(), "ad", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setupTabLayout() {

        BulletinPagerAdapter currentWeekPagerAdapter = new BulletinPagerAdapter(this);
        ViewPager2 viewPager = findViewById(R.id.bulletin_viewpager2_id);
        TabLayout tabLayout = findViewById(R.id.bulletin_tab_layout_id);

        viewPager.setAdapter(currentWeekPagerAdapter);
        // attach the tab layout with the view pager
        new TabLayoutMediator(tabLayout, viewPager,
                new TabLayoutMediator.TabConfigurationStrategy() {
                    @Override
                    public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                        tab.setIcon(tabIcons[position]);

                    }
                }
        ).attach();


    }

}
