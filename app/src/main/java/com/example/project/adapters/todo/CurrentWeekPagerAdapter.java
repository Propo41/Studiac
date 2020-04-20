package com.example.project.adapters.todo;

import android.content.DialogInterface;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.project.fragments.todo.CurrentWeekFragment;
import com.example.project.fragments.todo.CurrentWeekTabFragment;
import com.example.project.utility.common.Common;
import com.example.project.utility.todo.Day;
import com.example.project.utility.todo.Task;

import java.util.ArrayList;
import java.util.Objects;

/*
 * Adapter to communicate with the FragmentStateAdapter for swiping views
 * on the createFragment() method, the views for each day is passed using the mDays array's position
 * by doing this, we're are recycling only one fragment again and again instead of creating multiple
 * fragments.
 */
public class CurrentWeekPagerAdapter extends FragmentStateAdapter {

    private ArrayList<Day> mCurrentWeek; // this will be a global variable which will be stored in TodoTasksClass and later will be saved in phone memory

    public CurrentWeekPagerAdapter(@NonNull Fragment fragment, ArrayList<Day> currentWeek) {
        super(fragment);
        mCurrentWeek = currentWeek;
    }


    @NonNull
    @Override
    public Fragment createFragment(int position) {
        return new CurrentWeekTabFragment(mCurrentWeek.get(position));
    }

    @Override
    public int getItemCount() {
        return 7;
    }


}
