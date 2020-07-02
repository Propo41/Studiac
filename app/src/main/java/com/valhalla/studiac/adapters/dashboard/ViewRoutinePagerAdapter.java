package com.valhalla.studiac.adapters.dashboard;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.PagerAdapter;

import com.google.android.material.snackbar.Snackbar;
import com.valhalla.studiac.R;
import com.valhalla.studiac.models.Routine;
import com.valhalla.studiac.models.Schedule;


import java.util.ArrayList;

public class ViewRoutinePagerAdapter extends PagerAdapter {

    private Context mContext;
    private ArrayList<Routine> mClassRoutine;
    private String[] DAYS = {"Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"};

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


    private void setupRecyclerView(final View view, int position) {
        RecyclerView recyclerView = view.findViewById(R.id.view_routine__pager_item_recycler_view_id);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(mContext);

        final ArrayList<Schedule> schedules = mClassRoutine.get(position).getItems();
        ImageView imageView = view.findViewById(R.id.view_routine_holiday_image_id);
        final TextView textView = view.findViewById(R.id.view_routine_holiday_text_id);
        final Typeface typeface = ResourcesCompat.getFont(mContext, R.font.montserrat_medium);


        // if a particular day doesn't have any schedule, put an image
        if (schedules == null) {
            imageView.setVisibility(View.VISIBLE);
            textView.setVisibility(View.VISIBLE);
        } else {
            imageView.setVisibility(View.INVISIBLE);
            textView.setVisibility(View.INVISIBLE);
            ViewRoutineRecycleAdapter adapter = new ViewRoutineRecycleAdapter(schedules);
            recyclerView.setLayoutManager(mLayoutManager);
            recyclerView.setAdapter(adapter);
            adapter.setOnItemClickListener(new ViewRoutineRecycleAdapter.OnItemClickListener() {
                @Override
                public void onCourseClick(int position) {
                    ConstraintLayout root = view.findViewById(R.id.view_routine_root);
                    Snackbar snack = Snackbar.make(root, schedules.get(position).getDescription(), Snackbar.LENGTH_LONG);
                    View view = snack.getView();
                    TextView tv = view.findViewById(com.google.android.material.R.id.snackbar_text);
                    tv.setTextColor(Color.WHITE);
                    tv.setCompoundDrawablesWithIntrinsicBounds(R.drawable.setup_ic_course, 0, 0, 0);
                    tv.setCompoundDrawablePadding(mContext.getResources().getDimensionPixelOffset(R.dimen.EditTextDrawablePadding));
                    tv.setTypeface(typeface);
                    snack.show();
                }
            });
        }

    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }


}
