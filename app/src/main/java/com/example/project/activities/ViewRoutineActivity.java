package com.example.project.activities;

import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.project.R;
import com.example.project.adapters.dashboard.CustomViewPager;
import com.example.project.adapters.dashboard.ViewRoutinePagerAdapter;
import com.example.project.toolbars.NavigationToolbarWhite;
import com.example.project.utility.ShadowTransformer;
import com.example.project.utility.dashboard.CardItem;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Calendar;

public class ViewRoutineActivity extends NavigationToolbarWhite {

    Context mContext; // for debug
    private ViewRoutinePagerAdapter mCardAdapter;
    private CustomViewPager viewPager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContent(R.layout.activity_viewroutine);
        createCardView();
        mContext = this;

    }

    private void createCardView() {



        viewPager = findViewById(R.id.viewPager);

        mCardAdapter = new ViewRoutinePagerAdapter();
        mCardAdapter.addCardItem(new CardItem("Sunday", "Course names"));
        mCardAdapter.addCardItem(new CardItem("Monday", "Course names"));
        mCardAdapter.addCardItem(new CardItem("Tuesday", "Course names"));
        mCardAdapter.addCardItem(new CardItem("Thursday", "Course names"));
        mCardAdapter.addCardItem(new CardItem("Friday", "Course names"));
        mCardAdapter.addCardItem(new CardItem("Saturday", "Course names"));

        handleUserEvents();

        ShadowTransformer cardShadowTransformer = new ShadowTransformer(viewPager, mCardAdapter);
        cardShadowTransformer.enableScaling(true);
        viewPager.setAdapter(mCardAdapter); // only show it on fragments
        viewPager.setPageTransformer(false, cardShadowTransformer);
        viewPager.setOffscreenPageLimit(3);


    }

    private void handleUserEvents() {

        final TextView prompt = findViewById(R.id.viewRoutine_prompt_id);
        final TextView promptTop = findViewById(R.id.viewRoutine_prompt_id2);
        promptTop.setVisibility(View.INVISIBLE);

        mCardAdapter.setOnItemClickListener(new ViewRoutinePagerAdapter.OnItemClickListener() {

            @Override
            public void onButtonClick(final CardItem item, View view, int pos) {


                //Toast.makeText(mContext, "editing mode: " + ViewRoutineAdapter.isEditingMode(), Toast.LENGTH_SHORT).show();

                FloatingActionButton editBv = view.findViewById(R.id.viewRoutine_edit_button_id);
                // if editing mode is on, then change the image icon to edit
                // and the user cant interact with the items.
                // else, do the opposite
                if(ViewRoutinePagerAdapter.isEditingMode()){
                    editBv.setImageResource(R.drawable.common_ic_edit);
                    ViewRoutinePagerAdapter.setEditingMode(false);
                    viewPager.setPagingEnabled(true);
                    prompt.setText(R.string.viewRoutine_prompt_1);
                    promptTop.setVisibility(View.INVISIBLE);

                }else{
                    editBv.setImageResource(R.drawable.common_ic_check);
                    ViewRoutinePagerAdapter.setEditingMode(true);
                    viewPager.setPagingEnabled(false);
                    prompt.setText(R.string.viewRoutine_prompt_2);
                    promptTop.setVisibility(View.VISIBLE);

                }


            }


            @Override
            public void onCourseNameClick(CardItem item, View view, int pos) {


                if(ViewRoutinePagerAdapter.isEditingMode()){
                    // @TODO: open dialog: Select_Course (31)
                    Toast.makeText(mContext, "clicked: " , Toast.LENGTH_SHORT).show();


                }


            }

            @Override
            public void onCourseStartClick(CardItem item, View view, int pos) {

                Toast.makeText(mContext, "start" , Toast.LENGTH_SHORT).show();

                if(ViewRoutinePagerAdapter.isEditingMode()){

                    Calendar calendar = Calendar.getInstance();
                    final int hour = calendar.get(Calendar.HOUR_OF_DAY);
                    final int minute = calendar.get(Calendar.MINUTE);

                    TimePickerDialog timePickerDialog = new TimePickerDialog(mContext, new TimePickerDialog.OnTimeSetListener() {
                        @Override
                        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                            // @TODO: apply logic for when it's PM or AM
                        }
                    }, hour, minute, android.text.format.DateFormat.is24HourFormat(mContext));
                    timePickerDialog.show();

                }


            }

            @Override
            public void onCourseEndClick(CardItem item, View view, int pos) {
                if(ViewRoutinePagerAdapter.isEditingMode()){

                    Toast.makeText(mContext, "pos: " + pos , Toast.LENGTH_SHORT).show();

                    Calendar calendar = Calendar.getInstance();
                    final int hour = calendar.get(Calendar.HOUR_OF_DAY);
                    final int minute = calendar.get(Calendar.MINUTE);

                    TimePickerDialog timePickerDialog = new TimePickerDialog(mContext, new TimePickerDialog.OnTimeSetListener() {
                        @Override
                        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                            // @TODO: apply logic for when it's PM or AM
                        }
                    }, hour, minute, android.text.format.DateFormat.is24HourFormat(mContext));
                    timePickerDialog.show();

                }
            }



        });

    }


}



