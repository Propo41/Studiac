package com.example.project.adapters.bulletinboard;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.project.fragments.bulletinboard.BulletinFragment;

/*
 * Adapter to communicate with the FragmentStateAdapter for swiping views
 * on the createFragment() method, the views for each day is passed using the mDays array's position
 * by doing this, we're are recycling only one fragment again and again instead of creating multiple
 * fragments.
 */
public class BulletinPagerAdapter extends FragmentStateAdapter {


    final private int TABS = 3;

    public BulletinPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }


    @NonNull
    @Override
    public Fragment createFragment(int position) {
       BulletinFragment bulletinFragment = new BulletinFragment();
        //position+=1;
        //Bundle bundle = new Bundle();
        //bundle.putString("message", "Pos: " + position);
        //tabFragment.setArguments(bundle);
        return bulletinFragment;
    }

    @Override
    public int getItemCount() {
        return TABS;
    }



}
