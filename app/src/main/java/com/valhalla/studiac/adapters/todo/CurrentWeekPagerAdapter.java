package com.valhalla.studiac.adapters.todo;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.valhalla.studiac.fragments.todo.CurrentWeekTabFragment;
import com.valhalla.studiac.models.Course;
import com.valhalla.studiac.models.Task;
import com.valhalla.studiac.models.TasksUtil;

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
    private ArrayList<Course> mCourses;


    public CurrentWeekPagerAdapter(@NonNull Fragment fragment, ArrayList<TasksUtil> currentWeek, ArrayList<Task> currentTasks, ArrayList<Course> courses) {
        super(fragment);
        mCurrentWeek = currentWeek;
        mCurrentTasks = currentTasks;
        mCourses = courses;
    }


    @NonNull
    @Override
    public Fragment createFragment(int position) {
        return new CurrentWeekTabFragment(mCurrentWeek.get(position), mCurrentTasks, mCourses);
    }

    @Override
    public int getItemCount() {
        return 7;
    }


}
