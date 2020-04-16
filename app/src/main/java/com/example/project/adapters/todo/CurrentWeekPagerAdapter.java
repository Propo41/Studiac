package com.example.project.adapters.todo;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.project.fragments.todo.CurrentWeekTabFragment;
import com.example.project.utility.todo.Days;

import java.util.ArrayList;

/*
 * Adapter to communicate with the FragmentStateAdapter for swiping views
 * on the createFragment() method, the views for each day is passed using the mDays array's position
 * by doing this, we're are recycling only one fragment again and again instead of creating multiple
 * fragments.
 */
public class CurrentWeekPagerAdapter extends FragmentStateAdapter {


    private ArrayList<Days> mDays;
    final private int NUMBER_OF_DAYS = 7;

    public CurrentWeekPagerAdapter(@NonNull Fragment fragment, ArrayList<Days> days) {
        super(fragment);
        mDays = days;
    }


    @NonNull
    @Override
    public Fragment createFragment(int position) {
        CurrentWeekTabFragment tabFragment = new CurrentWeekTabFragment(mDays.get(position));
        //position+=1;
        //Bundle bundle = new Bundle();
        //bundle.putString("message", "Pos: " + position);
        //tabFragment.setArguments(bundle);
        return tabFragment;
    }

    @Override
    public int getItemCount() {
        return NUMBER_OF_DAYS;
    }



}
