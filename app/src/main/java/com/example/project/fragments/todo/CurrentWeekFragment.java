package com.example.project.fragments.todo;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.example.project.R;
import com.example.project.adapters.todo.TabLayoutAdapter;
import com.example.project.utility.todo.CurrentWeekItems;
import com.example.project.utility.todo.Days;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.ArrayList;

/*
 * the base fragment of the "Current Week" tab.
 * The tab layouts and view pagers are setup from this fragment.
 * parent Activity: TodoTaskActivity
 */
public class CurrentWeekFragment extends Fragment {

    private String[] days = {"Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"};
    private ArrayList<Days> mDays;


    // we can pass data through this constructor
    public CurrentWeekFragment(){
        mDays = new ArrayList<>();
        initList();

    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_todo_week, container, false);

        TabLayoutAdapter tabLayoutAdapter = new TabLayoutAdapter(this, mDays);
        ViewPager2 viewPager = view.findViewById(R.id.viewpager2);

        TabLayout tabLayout = view.findViewById(R.id.tab_layout);

        viewPager.setAdapter(tabLayoutAdapter);
        new TabLayoutMediator(tabLayout, viewPager,
                new TabLayoutMediator.TabConfigurationStrategy() {
                    @Override
                    public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                        tab.setText(days[position]);
                    }
                }
        ).attach();

        return view;

    }


    private void initList() {

        // we can define the sections in an array list of Objects and then use
        // if (object instanceof SectionClass) to identify if it is a section or not

        ArrayList<CurrentWeekItems> exampleItemsList = new ArrayList<>();

        exampleItemsList.add(new CurrentWeekItems("this is android", true));
        exampleItemsList.add(new CurrentWeekItems("is cloud"));
        exampleItemsList.add(new CurrentWeekItems("is cloud"));
        exampleItemsList.add(new CurrentWeekItems("this is android", true));
        exampleItemsList.add(new CurrentWeekItems("is cloud"));
        mDays.add(new Days(exampleItemsList)); // day 1 initialized
        exampleItemsList = new ArrayList<>();

        exampleItemsList.add(new CurrentWeekItems("this is android", true));
        exampleItemsList.add(new CurrentWeekItems("is cloud"));
        exampleItemsList.add(new CurrentWeekItems("is cloud"));
        exampleItemsList.add(new CurrentWeekItems("this is android", true));
        exampleItemsList.add(new CurrentWeekItems("is cloud"));
        mDays.add(new Days(exampleItemsList)); // day 1 initialized
        exampleItemsList = new ArrayList<>();


        exampleItemsList.add(new CurrentWeekItems("this is android", true));
        exampleItemsList.add(new CurrentWeekItems("is cloud"));
        exampleItemsList.add(new CurrentWeekItems("is cloud"));
        exampleItemsList.add(new CurrentWeekItems("this is android", true));
        exampleItemsList.add(new CurrentWeekItems("is cloud"));
        mDays.add(new Days(exampleItemsList)); // day 1 initialized
        exampleItemsList = new ArrayList<>();

        exampleItemsList.add(new CurrentWeekItems("this is android", true));
        exampleItemsList.add(new CurrentWeekItems("is cloud"));
        exampleItemsList.add(new CurrentWeekItems("is cloud"));
        exampleItemsList.add(new CurrentWeekItems("this is android", true));
        exampleItemsList.add(new CurrentWeekItems("is cloud"));
        mDays.add(new Days(exampleItemsList)); // day 1 initialized
        exampleItemsList = new ArrayList<>();

        exampleItemsList.add(new CurrentWeekItems("this is android", true));
        exampleItemsList.add(new CurrentWeekItems("is cloud"));
        exampleItemsList.add(new CurrentWeekItems("is cloud"));
        exampleItemsList.add(new CurrentWeekItems("this is android", true));
        exampleItemsList.add(new CurrentWeekItems("is cloud"));
        mDays.add(new Days(exampleItemsList)); // day 1 initialized
        exampleItemsList = new ArrayList<>();

        exampleItemsList.add(new CurrentWeekItems("this is android", true));
        exampleItemsList.add(new CurrentWeekItems("is cloud"));
        exampleItemsList.add(new CurrentWeekItems("is cloud"));
        exampleItemsList.add(new CurrentWeekItems("this is android", true));
        exampleItemsList.add(new CurrentWeekItems("is cloud"));
        mDays.add(new Days(exampleItemsList)); // day 1 initialized
        exampleItemsList = new ArrayList<>();

        exampleItemsList.add(new CurrentWeekItems("this is android", true));
        exampleItemsList.add(new CurrentWeekItems("is cloud"));
        exampleItemsList.add(new CurrentWeekItems("is cloud"));
        exampleItemsList.add(new CurrentWeekItems("this is android", true));
        exampleItemsList.add(new CurrentWeekItems("is cloud"));
        mDays.add(new Days(exampleItemsList)); // day 1 initialized
        exampleItemsList = new ArrayList<>();





    }


}
