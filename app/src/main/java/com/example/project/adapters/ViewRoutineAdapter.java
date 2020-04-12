package com.example.project.adapters;

import android.content.ContentUris;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.ContextThemeWrapper;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.example.project.R;
import com.example.project.utility.CardItem;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;


public class ViewRoutineAdapter extends PagerAdapter implements CardAdapter {


    private ViewRoutineAdapter.OnItemClickListener mListener;
    private List<CardView> mViews;
    private List<CardItem> mData;
    private float mBaseElevation;
    private static boolean editingMode;


    public interface OnItemClickListener {
        // when user presses the edit button, this custom listener will be implemented
        void onCourseNameClick(CardItem item, View view, int pos);

        void onCourseStartClick(CardItem item, View view,  int pos);

        void onCourseEndClick(CardItem item, View view,  int pos);

        void onButtonClick(CardItem item, View view,  int pos);
    }


    public ViewRoutineAdapter() {
        editingMode = false;
        mData = new ArrayList<>();
        mViews = new ArrayList<>();
    }

    public static void setEditingMode(boolean editingMode) {
        ViewRoutineAdapter.editingMode = editingMode;
    }

    public void addCardItem(CardItem item) {
        mViews.add(null);
        mData.add(item);
    }

    public float getBaseElevation() {
        return mBaseElevation;
    }

    @Override
    public CardView getCardViewAt(int position) {

        return mViews.get(position);
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        View view = LayoutInflater.from(container.getContext())
                .inflate(R.layout.adapter_viewroutine, container, false);

        container.addView(view);
        bind(mData.get(position), view);
        CardView cardView = view.findViewById(R.id.cardView);

        if (mBaseElevation == 0) {
            mBaseElevation = cardView.getCardElevation();
        }

        cardView.setMaxCardElevation(mBaseElevation * MAX_ELEVATION_FACTOR);
        mViews.set(position, cardView);

        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
        mViews.set(position, null);
    }

    /*
    binds the text views with the texts that is passed through the CardItem object
     */


    // this will be called from our MainActivity
    // we need to handle the onclick item from the ExampleViewHolder class
    public void setOnItemClickListener(ViewRoutineAdapter.OnItemClickListener listener) {
        mListener = listener;
    }

    public static boolean isEditingMode() {
        return editingMode;
    }


    private void bindToEventListener(View view, final CardItem item, final String type, final int pos) {
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    switch (type) {
                        case "button":
                            mListener.onButtonClick(item, v, pos);
                            break;
                        case "courseName":
                            mListener.onCourseNameClick(item, v, pos);
                            break;
                        case "courseStartTime":
                            mListener.onCourseStartClick(item, v, pos);
                            break;
                        case "courseEndTime":
                            mListener.onCourseEndClick(item, v, pos);
                            break;

                    }
                }
            }
        });
    }



    // sets the margin of a particular view
    public TableRow.LayoutParams setMargin(int layoutWidth, int layoutHeight, int start, int left, int top,
                                           int right, int bottom) {


           //layoutHeight = TableRow.LayoutParams.WRAP_CONTENT;
        //
       // TableRow.LayoutParams.MATCH_PARENT;


        TableRow.LayoutParams layoutParams = new TableRow.LayoutParams(layoutWidth, layoutHeight);
        layoutParams.setMarginStart(start);
        layoutParams.setMargins(left, top, right, bottom);
        return layoutParams;

    }


    private void bind(final CardItem item, View view) {
        TextView titleTv = view.findViewById(R.id.titleTextView);
        titleTv.setText(item.getTitle());

        FloatingActionButton editBv = view.findViewById(R.id.viewRoutine_edit_button_id);
        bindToEventListener(editBv, item, "button", 0);

        // set other texts programmatically
        // by creating table rows. It's done down below
        TableLayout tableLayout = view.findViewById(R.id.viewRoutine_tableLayout_id);
        Context context = view.getContext();
        Typeface typeface = ResourcesCompat.getFont(context, R.font.montserrat_medium);
        int marginTop = 35;

        for (int i = 0; i < 7; i++) {
            TableRow table_row = new TableRow(context);

            TextView courseName = new TextView(context);
            courseName.setTypeface(typeface);
            courseName.setTextColor(Color.WHITE);
            courseName.setLayoutParams(setMargin(310,
                    TableRow.LayoutParams.WRAP_CONTENT,
                    30,0,marginTop,0,0));
            courseName.setPaddingRelative(20,0,10,5);
            courseName.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_START);
            courseName.setText("MATH 1013 ");
            table_row.addView(courseName);
            bindToEventListener(courseName, item, "courseName", i);


            TextView courseStartTime = new TextView(context);
            courseStartTime.setTextColor(Color.WHITE);
            courseStartTime.setTypeface(typeface);

            courseStartTime.setLayoutParams(setMargin(TableRow.LayoutParams.WRAP_CONTENT,
                    TableRow.LayoutParams.WRAP_CONTENT,
                    0,0,marginTop,0,0));
            courseStartTime.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_END);
            courseStartTime.setText(i+1 + ":00PM");
            table_row.addView(courseStartTime);
            bindToEventListener(courseStartTime, item, "courseStartTime", i);


            TextView courseEndTime = new TextView(context);
            courseEndTime.setLayoutParams(setMargin(TableRow.LayoutParams.WRAP_CONTENT,
                    TableRow.LayoutParams.WRAP_CONTENT,
                    0,0,marginTop,0,0));
            courseEndTime.setTypeface(typeface);
            courseEndTime.setTextColor(Color.WHITE);
            courseEndTime.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_END);
            courseEndTime.setText(i+1 + ":00PM");
            table_row.addView(courseEndTime);
            bindToEventListener(courseEndTime, item, "courseEndTime", i);

            tableLayout.addView(table_row);
        }


    }


}




