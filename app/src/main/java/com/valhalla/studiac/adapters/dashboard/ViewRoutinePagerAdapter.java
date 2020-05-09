package com.valhalla.studiac.adapters.dashboard;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.PagerAdapter;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.valhalla.studiac.R;
import com.valhalla.studiac.database.Firebase;
import com.valhalla.studiac.utility.common.Routine;
import com.valhalla.studiac.utility.common.Schedule;
import com.valhalla.studiac.utility.common.Student;

import java.util.ArrayList;

public class ViewRoutinePagerAdapter extends PagerAdapter {

    private Context mContext;
    private ArrayList<Routine> mClassRoutine;
    private String[] DAYS = {"Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"};
    private RecyclerView mRecyclerView;
    private ViewRoutineRecycleAdapter mRecycleAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    public ViewRoutinePagerAdapter(ArrayList<Routine> classRoutine, Context context) {
        mContext = context;
        mClassRoutine = classRoutine;
    }


    @Override
    public int getCount() {
        return DAYS.length;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view.equals(object);
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, final int position) {
        View view = LayoutInflater.from(container.getContext()).inflate(R.layout.item_pager_view_routine, container, false);
        container.addView(view, 0);

        // set text for the title
        TextView titleTv = view.findViewById(R.id.view_routine_item_title_id);
        titleTv.setText(DAYS[position]);
        setupRecyclerView(view, position);


        return view;
    }

    private void setupRecyclerView(View view, int position) {
        mRecyclerView = view.findViewById(R.id.view_routine__pager_item_recycler_view_id);
        mLayoutManager = new LinearLayoutManager(mContext);

        ArrayList<Schedule> schedules = mClassRoutine.get(position).getItems();
        ImageView imageView = view.findViewById(R.id.view_routine_holiday_image_id);
        TextView textView = view.findViewById(R.id.view_routine_holiday_text_id);


        // if a particular day doesn't have any schedule, put an image
        if (schedules == null) {
            imageView.setVisibility(View.VISIBLE);
            textView.setVisibility(View.VISIBLE);
        } else {
            imageView.setVisibility(View.INVISIBLE);
            textView.setVisibility(View.INVISIBLE);
            mRecycleAdapter = new ViewRoutineRecycleAdapter(schedules);
            mRecyclerView.setLayoutManager(mLayoutManager);
            mRecyclerView.setAdapter(mRecycleAdapter);
        }

    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }


}
