package com.valhalla.studiac.fragments.todo;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.valhalla.studiac.R;
import com.valhalla.studiac.adapters.todo.CurrentWeekPagerAdapter;
import com.valhalla.studiac.holders.ListItem;
import com.valhalla.studiac.models.Course;
import com.valhalla.studiac.models.TasksUtil;
import com.valhalla.studiac.utility.Common;

import java.util.ArrayList;

/*
 * the base fragment of the "Current Week" tab.
 * The tab layouts and view pagers are setup from this fragment.
 * parent Activity: TodoTaskActivity
 */
public class CurrentWeekFragment extends Fragment {

    private ArrayList<TasksUtil> mCurrentWeek;
    private ArrayList<ListItem> mCurrentTasks;
    private ArrayList<Course> mCourses;
   // private DatabaseReference mDatabaseReference;


    // we can pass data through this constructor
    public CurrentWeekFragment(ArrayList<TasksUtil> currentWeek, ArrayList<ListItem> currentTasks, ArrayList<Course> courses) {
        mCurrentTasks = currentTasks;
        mCurrentWeek = currentWeek;
        mCourses = courses;

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_todo_week, container, false);

        CurrentWeekPagerAdapter currentWeekPagerAdapter = new CurrentWeekPagerAdapter(this, mCurrentWeek, mCurrentTasks, mCourses);
        final ViewPager2 viewPager = view.findViewById(R.id.todo_week_viewpager2_id);
        TabLayout tabLayout = view.findViewById(R.id.todo_week_tab_layout_id);
        viewPager.setAdapter(currentWeekPagerAdapter);

        viewPager.setOffscreenPageLimit(1); // sets the number of pages to load in advance to 1
        // attach the tab layout with the view pager
        new TabLayoutMediator(tabLayout, viewPager,
                new TabLayoutMediator.TabConfigurationStrategy() {
                    @Override
                    public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                        tab.setText(Common.GET_DAY_FROM_INDEX[position]);
                    }
                }
        ).attach();

        return view;

    }

    @Override
    public void onPause() {
        super.onPause();
      //  mDatabaseReference.setValue(mCurrentWeek);

    }
}
