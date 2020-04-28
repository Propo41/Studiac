package com.example.project.fragments.dialogs;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project.R;
import com.example.project.adapters.CommonAddScheduleRecycleAdapter;
import com.example.project.adapters.todo.CurrentTasksRecycleAdapter;
import com.example.project.utility.common.Common;
import com.example.project.utility.common.Schedule;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Objects;

public class SetupAddRoutineDialogFragment extends AppCompatDialogFragment {

    private static final int REQUEST_DAY = 10; // Used to identify the result obtained from the day picker dialog
    private static final int REQUEST_START_TIME = 11; // Used to identify the result obtained from the time picker dialog
    private static final int REQUEST_END_TIME = 12; // Used to identify the result obtained from the time picker dialog

    private static ArrayList<Schedule> mSchedules;
    private String mEndTime;
    private String mStartTime;
    private String mDaySelected;
    private View mView;
    private TableLayout mTableLayout;
    private CommonAddScheduleRecycleAdapter mAdapter;

    private static boolean[] visited = new boolean[6];
    // stores routine as:
    // 0 -> <CourseName, StartTime_EndTime>
    // 1 -> <CourseName, StartTime_EndTime> ...
    // where 0 is Sunday, 1 is Monday etc...

    public SetupAddRoutineDialogFragment(ArrayList<Schedule> schedules, boolean newEntry) {
        mSchedules = schedules;
        if (newEntry) {
            for (int i = 0; i < 6; i++)
                visited[i] = false;
        }
    }

    @SuppressLint("RestrictedApi")
    @Override
    public void setupDialog(@NonNull Dialog dialog, int style) {
        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        super.setupDialog(dialog, style);
    }

    @NonNull
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(Objects.requireNonNull(getActivity()));
        LayoutInflater inflater = getActivity().getLayoutInflater();
        mView = inflater.inflate(R.layout.dialog_setupprofile2_add_routine, null);
        builder.setView(mView);
        FloatingActionButton addButton = mView.findViewById(R.id.setup_add_routine_button_id);
        Button doneButton = mView.findViewById(R.id.setup_done_routine_button_id);

        // ******setup recycler view******
        setupRecyclerView();
        handleEventsOnItemRemove();
        //*******************************

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDayPicker(REQUEST_DAY);
            }
        });
        // when all the routine inputs are done, dismiss the dialog and pass the mSchedule to parent
        doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // send the mSchedule object to parent activity
                // but since we are using a reference, it already updated the value
                // so we don't have to send it back again
                dismiss();
            }
        });

        return builder.create();

    }

    private void handleEventsOnItemRemove() {
        mAdapter.setOnItemClickListener(new CommonAddScheduleRecycleAdapter.OnItemClickListener() {
            @Override
            public void onRemoveClick(int position) {
                mSchedules.remove(position);
                visited[position]=false;
                mAdapter.notifyItemRemoved(position);

            }
        });
    }

    private void setupRecyclerView() {
        // setup recycler view and pass the schedule list.
        RecyclerView recyclerView = mView.findViewById(R.id.common_add_recycle_view_id);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        mAdapter = new CommonAddScheduleRecycleAdapter(mSchedules);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(mAdapter);
    }

    /*
     * opens a dialog for day picker.
     * the output is provided in the method onActivityResult()
     */
    private void openDayPicker(int requestCode) {
        FragmentManager fragmentManager = Objects.requireNonNull(getActivity()).getSupportFragmentManager();
        DaySelectionFragment daySelectionFragment = new DaySelectionFragment();
        daySelectionFragment.setTargetFragment(SetupAddRoutineDialogFragment.this, requestCode);
        daySelectionFragment.show(fragmentManager, "fromDialog");
    }

    /*
     * opens a dialog for time picker.
     * the output is provided in the method onActivityResult()
     */
    private void openTimePicker(int requestCode) {
        FragmentManager fragmentManager = Objects.requireNonNull(getActivity()).getSupportFragmentManager();
        TimePickerFragment dialog = new TimePickerFragment();
        // set the targetFragment to receive the results, specifying the request code
        dialog.setTargetFragment(SetupAddRoutineDialogFragment.this, requestCode);
        dialog.show(fragmentManager, "fromDialog");
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == REQUEST_DAY && resultCode == Activity.RESULT_OK) {
            mDaySelected = data.getStringExtra("selectedDay");
            Integer dayIndex = Common.DAYS.get(mDaySelected);
            if (dayIndex != null) {
                if (visited[dayIndex]) {
                    Toast.makeText(getContext(), "Day already selected!", Toast.LENGTH_SHORT).show();
                } else {
                    openTimePicker(REQUEST_START_TIME);
                    Toast.makeText(getContext(), "Enter Start Time", Toast.LENGTH_SHORT).show();
                    visited[dayIndex] = true;

                }
            } else {
                Common.showExceptionPrompt(getContext(), "Bug: Day index null");
            }

        }
        // if user enters time correctly, open the second window
        else if (requestCode == REQUEST_START_TIME && resultCode == Activity.RESULT_OK) {
            mStartTime = data.getStringExtra("selectedTime");
            openTimePicker(REQUEST_END_TIME);
            Toast.makeText(getContext(), "Enter End Time", Toast.LENGTH_SHORT).show();
        }
        // when the end time is selected, the view will be generated
        // the proceeding calls will be made from the method onActivityResult()
        else if (requestCode == REQUEST_END_TIME && resultCode == Activity.RESULT_OK) {
            mEndTime = data.getStringExtra("selectedTime");
            mSchedules.add(new Schedule(mDaySelected, mStartTime, mEndTime));
            mAdapter.notifyItemInserted(mSchedules.size() - 1);

        } else {
            mStartTime = null;
            mEndTime = null;
        }
    }


}
