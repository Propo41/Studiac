package com.example.project.adapters.todo;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.project.fragments.todo.CurrentWeekTabFragment;
import com.example.project.utility.todo.Task;
import com.example.project.utility.todo.TasksUtil;

import java.util.ArrayList;

/*
 * Adapter to communicate with the FragmentStateAdapter for swiping views
 * on the createFragment() method, the views for each day is passed using the mDays array's position
 * by doing this, we're are recycling only one fragment again and again instead of creating multiple
 * fragments.
 */
public class CurrentWeekPagerAdapter extends FragmentStateAdapter {

    private ArrayList<TasksUtil> mCurrentWeek; // this will be a global variable which will be stored in TodoTasksClass and later will be saved in phone memory
    private ArrayList<Task> mCurrentTasks;

    public CurrentWeekPagerAdapter(@NonNull Fragment fragment, ArrayList<TasksUtil> currentWeek, ArrayList<Task> currentTasks) {
        super(fragment);
        mCurrentWeek = currentWeek;
        mCurrentTasks = currentTasks;
    }


    @NonNull
    @Override
    public Fragment createFragment(int position) {
        return new CurrentWeekTabFragment(mCurrentWeek.get(position), mCurrentTasks);
    }

    @Override
    public int getItemCount() {
        return 7;
    }


}
