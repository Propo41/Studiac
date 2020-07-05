package com.valhalla.studiac.fragments.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.valhalla.studiac.R;
import com.valhalla.studiac.adapters.dashboard.ViewScheduleRecycleAdapter;
import com.valhalla.studiac.models.Schedule;

import java.util.ArrayList;

public class ViewCounsellingTimeDialog extends AppCompatDialogFragment {
    private ArrayList<Schedule> mSchedule;
    private Context mContext;
    private View mView;

    ViewCounsellingTimeDialog(ArrayList<Schedule> schedule) {
        mSchedule = schedule;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_view_schedule, null);

        mContext = getContext();
        mView = view;
        if (mSchedule != null && mSchedule.size() != 0) {
            setupRecyclerView();
        }
        handleClickEvents();

        builder.setView(view);
        AlertDialog alertDialog = builder.create();
        Window window = alertDialog.getWindow();
        if (window != null) {
            window.setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            WindowManager.LayoutParams windowManager = window.getAttributes();
            windowManager.gravity = Gravity.BOTTOM;
            window.getAttributes().windowAnimations = R.style.DialogBottomAnimationSliding;
        }

        return alertDialog;
    }

    private void handleClickEvents() {
        Button button = mView.findViewById(R.id.dialog_view_schedule_continue_button_id);
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
        ViewScheduleRecycleAdapter recycleAdapter = new ViewScheduleRecycleAdapter(mSchedule);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(recycleAdapter);
    }



}
