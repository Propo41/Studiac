package com.valhalla.studiac.fragments.dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.valhalla.studiac.R;
import com.valhalla.studiac.adapters.CommonAddScheduleRecycleAdapter;
import com.valhalla.studiac.adapters.SpinnerAdapterText;
import com.valhalla.studiac.holders.TextItem;
import com.valhalla.studiac.models.Schedule;
import com.valhalla.studiac.utility.Common;

import java.util.ArrayList;
import java.util.Objects;

/*
 * may cause exception when selecting dates, ie in fast clicks. todo: handle the exceptions
 */
public class SetupAddRoutineDialog extends AppCompatDialogFragment {

    private static final int REQUEST_DAY = 10; // Used to identify the result obtained from the day picker dialog
    private static final int REQUEST_START_TIME = 11; // Used to identify the result obtained from the time picker dialog
    private static final int REQUEST_END_TIME = 12; // Used to identify the result obtained from the time picker dialog

    private static ArrayList<Schedule> mSchedules;
    private RecyclerView mRecyclerView;
    private String mEndTime;
    private String mStartTime;
    private String mDaySelected;
    private View mView;
    private CommonAddScheduleRecycleAdapter mAdapter;
    private Context mContext;
    private static int DAYS = 7;
    private ArrayList<String> mDays;
    private ArrayList<TextItem> mDaySpinnerItems;
    private boolean[] mVisited;
    private TextView mStartTimeTextView;
    private TextView mEndTimeTextView;


    public SetupAddRoutineDialog(ArrayList<Schedule> schedules, boolean[] visited) {
        mSchedules = schedules;
        mVisited = visited;
        mDaySpinnerItems = new ArrayList<>();
        mDaySpinnerItems.add(new TextItem("Sunday"));
        mDaySpinnerItems.add(new TextItem("Monday"));
        mDaySpinnerItems.add(new TextItem("Tuesday"));
        mDaySpinnerItems.add(new TextItem("Wednesday"));
        mDaySpinnerItems.add(new TextItem("Thursday"));
        mDaySpinnerItems.add(new TextItem("Friday"));
        mDaySpinnerItems.add(new TextItem("Saturday"));
    }


    @NonNull
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        mContext = getContext();

        AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(mContext, R.style.CustomDialogTheme));
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        mView = inflater.inflate(R.layout.dialog_setupprofile2_add_routine, null);
        builder.setView(mView);

        mRecyclerView = mView.findViewById(R.id.common_add_recycle_view_id);
        Spinner spinner = mView.findViewById(R.id.addroutine_day_spinner_id2);
        populateSpinner(spinner);

        if (Objects.equals(getTag(), "addCourseDialog")) {
            mDays = new ArrayList<>();
        }

        // ******setup recycler view******
        if (mSchedules.size() == 0) {
            mRecyclerView.setVisibility(View.GONE);
        } else {
            mRecyclerView.setVisibility(View.VISIBLE);
        }
        setupRecyclerView();
        handleEventsOnItemRemove();

        // handle button click events
        handleEvents();
        AlertDialog dialog = builder.create();
        //Objects.requireNonNull(dialog.getWindow()).setGravity(Gravity.CENTER);
        // makes background transparent
        Window window = dialog.getWindow();
        if (window != null) {
            window.setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            // removes title from dialog.
            // dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            window.setGravity(Gravity.BOTTOM);
            WindowManager.LayoutParams windowManager = window.getAttributes();
            // positions the dialog in bottom|center
            windowManager.gravity = Gravity.CENTER;
            // margin bottom of dialog relative to parent
            window.getAttributes().windowAnimations = R.style.DialogAnimationSliding;

        }

        return dialog;

    }

    private void populateSpinner(Spinner spinner) {
        SpinnerAdapterText adapter = new SpinnerAdapterText(requireContext(), mDaySpinnerItems,
                ResourcesCompat.getColor(getResources(), R.color.colorWhite, null));
        spinner.setAdapter(adapter);
        handleSpinnerEvents(spinner);
    }

    private void handleSpinnerEvents(Spinner spinner) {
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                TextItem clickedItem = (TextItem) parent.getItemAtPosition(position);
                mDaySelected = clickedItem.getText();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }


    /**
     * handles events
     *
     * @events: addButton, the FAB button to insert the routine entries
     * @events: doneButton, the button at the bottom. Clicking it will dismiss the dialog
     */
    private void handleEvents() {
        FloatingActionButton addButton = mView.findViewById(R.id.setup_add_routine_button_id2);
        Button doneButton = mView.findViewById(R.id.setup_done_routine_button_id);
        mStartTimeTextView = mView.findViewById(R.id.addroutine_start_time_input_id2);
        mEndTimeTextView = mView.findViewById(R.id.addroutine_end_time_input_id2);

        // when the start time text view is clicked, open time picker
        mStartTimeTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openTimePicker(REQUEST_START_TIME);
                Toast.makeText(getContext(), "Enter start time", Toast.LENGTH_SHORT).show();

            }
        });


        // when the end time text view is clicked, open time picker
        mEndTimeTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openTimePicker(REQUEST_END_TIME);
                Toast.makeText(getContext(), "Enter end time", Toast.LENGTH_SHORT).show();

            }
        });


        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Integer dayIndex = Common.GET_INDEX_FROM_DAY.get(mDaySelected);
                if (dayIndex != null) {
                    // check if the day has been previously entered
                    if (!mVisited[dayIndex]) {
                        // check if start and end time is provided
                        if (mStartTime != null && mEndTime != null) {
                            // inserts the new entry to the list if the time entries are correct
                            mSchedules.add(new Schedule(mDaySelected, mStartTime, mEndTime));
                            if (mSchedules.size() > 0) {
                                mRecyclerView.setVisibility(View.VISIBLE);
                            }
                            mAdapter.notifyItemInserted(mSchedules.size() - 1);
                            mVisited[dayIndex] = true;
                            String tag = getTag();
                            if (tag != null && tag.equals("addCourseDialog")) {
                                mDays.add(mDaySelected);
                            }
                        } else {
                            Toast.makeText(mContext, "You must select a time", Toast.LENGTH_SHORT).show();
                        }

                    } else {
                        Toast.makeText(mContext, "Day already selected!", Toast.LENGTH_SHORT).show();
                    }
                }


            }
        });

        // when all the routine inputs are done, dismiss the dialog
        doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // if the dialog is opened from Add Course dialog, then return mDays as an intent
                if (Objects.equals(getTag(), "addCourseDialog")) {
                    // pass the days list to previous dialog using intent
                    Fragment fragment = getTargetFragment();
                    if (fragment != null) {
                        fragment.onActivityResult(
                                getTargetRequestCode(),
                                Activity.RESULT_OK,
                                new Intent().putStringArrayListExtra("days", mDays)
                        );
                    }

                }

                dismiss();
            }
        });

    }

    private void handleEventsOnItemRemove() {
        mAdapter.setOnItemClickListener(new CommonAddScheduleRecycleAdapter.OnItemClickListener() {
            @Override
            public void onRemoveClick(int position) {
                Integer dayIndex = Common.GET_INDEX_FROM_DAY.get(mSchedules.get(position).getName());
                if (dayIndex != null) {
                    mSchedules.remove(position);
                    mVisited[dayIndex] = false;
                    mAdapter.notifyItemRemoved(position);
                    if (mSchedules.size() == 0) {
                        mRecyclerView.setVisibility(View.GONE);
                    }
                } else {
                    Log.d("DEBUG", "day index invalid. Check code");

                }


            }
        });
    }

    private void setupRecyclerView() {
        // setup recycler view and pass the schedule list.
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        mAdapter = new CommonAddScheduleRecycleAdapter(mSchedules);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(mAdapter);
    }


    /*
     * opens a dialog for time picker.
     * the output is provided in the method onActivityResult()
     */
    private void openTimePicker(int requestCode) {
        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
        TimePickerDialog dialog = new TimePickerDialog();
        // set the targetFragment to receive the results, specifying the request code
        dialog.setTargetFragment(SetupAddRoutineDialog.this, requestCode);
        dialog.show(fragmentManager, "fromDialog");
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // result obtained after user picks the start time from the dialog
        if (requestCode == REQUEST_START_TIME && resultCode == Activity.RESULT_OK) {
            mStartTime = data.getStringExtra("selectedTime");
            mStartTimeTextView.setText(mStartTime);
        }
        // result obtained after user picks the end time from the dialog
        else if (requestCode == REQUEST_END_TIME && resultCode == Activity.RESULT_OK) {
            mEndTime = data.getStringExtra("selectedTime");
            mEndTimeTextView.setText(mEndTime);

        } else {
            mStartTime = null;
            mEndTime = null;
        }
    }


}
