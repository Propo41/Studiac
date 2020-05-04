package com.example.project.fragments.dialogs;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project.R;
import com.example.project.adapters.dashboard.ViewScheduleRecycleAdapter;
import com.example.project.utility.common.Schedule;

import java.util.ArrayList;

public class ViewCounsellingTimeDialog extends AppCompatDialogFragment {
    private ArrayList<Schedule> mSchedule;
    private Context mContext;
    private View mView;

    ViewCounsellingTimeDialog(ArrayList<Schedule> schedule) {
        mSchedule = schedule;
    }

    @SuppressLint("RestrictedApi")
    @Override
    public void setupDialog(@NonNull Dialog dialog, int style) {
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        super.setupDialog(dialog, style);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_view_schedule, null);
        mContext = getContext();
        mView = view;

        setupRecyclerView();
        handleClickEvents();

        builder.setView(view);
        return builder.create();
    }

    private void handleClickEvents() {
        Button button = mView.findViewById(R.id.setup_dialog_course_added_add_id);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

    private void setupRecyclerView() {
        RecyclerView recyclerView = mView.findViewById(R.id.view_routine__pager_item_recycler_view_id);
        recyclerView.setHasFixedSize(true); // this will lock the scrolling. We cant scroll
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(mContext);
        ImageView imageView = mView.findViewById(R.id.view_routine_holiday_image_id);
        TextView textView = mView.findViewById(R.id.view_routine_holiday_text_id);
        TextView headerView = mView.findViewById(R.id.view_routine_item_title_id);
        headerView.setText("Counselling Hours");

        // if a particular day doesn't have any schedule, put an image
        if (mSchedule.size() == 0) {
            imageView.setVisibility(View.VISIBLE);
            textView.setVisibility(View.VISIBLE);
            textView.setText("Ops! Nothing here yet!");
        } else {
            imageView.setVisibility(View.INVISIBLE);
            textView.setVisibility(View.INVISIBLE);
            ViewScheduleRecycleAdapter recycleAdapter = new ViewScheduleRecycleAdapter(mSchedule);
            recyclerView.setLayoutManager(layoutManager);
            recyclerView.setAdapter(recycleAdapter);
        }
    }


}
