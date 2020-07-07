package com.valhalla.studiac.activities;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.valhalla.studiac.R;
import com.valhalla.studiac.fragments.WalkThroughFragment;
import com.valhalla.studiac.utility.SharedPreference;

public class WalkThroughActivity extends AppCompatActivity implements WalkThroughFragment.WalkThroughFragmentListener {
    private static final int NUM_PAGES = 5;

    /**
     * The pager widget, which handles animation and allows swiping horizontally to access previous
     * and next wizard steps.
     */
    private ViewPager mPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_walkthrough);

        // Instantiate a ViewPager and a PagerAdapter.
        mPager = findViewById(R.id.walkthrough_viewpager);
        PagerAdapter pagerAdapter = new WalkThroughPageAdapter(getSupportFragmentManager());
        mPager.setAdapter(pagerAdapter);
    }

    @Override
    public void onBackPressed() {
        if (mPager.getCurrentItem() == 0) {
            // If the user is currently looking at the first step, allow the system to handle the
            // Back button. This calls finish() on this activity and pops the back stack.
            super.onBackPressed();
        } else {
            // Otherwise, select the previous step.
            mPager.setCurrentItem(mPager.getCurrentItem() - 1);
        }
    }

    @Override
    public void onSkipClick() {
        // save new user to false. ie, this activity wont show anymore
        SharedPreference preference = SharedPreference.getInstance(getApplicationContext());
        preference.saveData(Boolean.class.getSimpleName(), getString(R.string.is_new_user), false);

        Intent intent = new Intent(this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    @Override
    public void onContinueClick(int pos) {
        if (pos == 4) {
            // save new user to false. ie, this activity wont show anymore
            SharedPreference preference = SharedPreference.getInstance(getApplicationContext());
            preference.saveData(Boolean.class.getSimpleName(), getString(R.string.is_new_user), false);

            Intent intent = new Intent(this, LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        } else {
            mPager.setCurrentItem(pos + 1, true);
        }

    }

    /**
     * A simple pager adapter that represents 5 ScreenSlidePageFragment objects, in
     * sequence.
     */
    private static class WalkThroughPageAdapter extends FragmentStatePagerAdapter {
        public WalkThroughPageAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        @NonNull
        public Fragment getItem(int position) {
            return new WalkThroughFragment(position);
        }

        @Override
        public int getCount() {
            return NUM_PAGES;
        }
    }
}
