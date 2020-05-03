package com.example.project.fragments.dialogs;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Layout;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project.R;
import com.example.project.adapters.CommonAddScheduleRecycleAdapter;
import com.example.project.adapters.SelectCourseRecycleAdapter;
import com.example.project.utility.common.Common;
import com.example.project.utility.common.Course;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class SelectCourseDialog extends AppCompatDialogFragment {

    private ArrayList<Course> mCourses;
    private View mView;
    private SelectCourseRecycleAdapter mAdapter;
    private int mCheckedPosition = 0;

    public SelectCourseDialog(ArrayList<Course> courses) {
        mCourses = courses;
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
        View view = inflater.inflate(R.layout.dialog_common_select_course, null);
        mView = view;

        setupRecyclerView();
        handleClickEvents();

        builder.setView(view);
        AlertDialog alertDialog = builder.create();

        alertDialog.getWindow().setLayout(Common.getEquivalentPx(300), ConstraintLayout.LayoutParams.WRAP_CONTENT);
        alertDialog.getWindow().setGravity(Gravity.CENTER);

        return alertDialog;
    }

    private void handleClickEvents() {
        Button selectButton = mView.findViewById(R.id.dialog_course_picker_button_id);
        selectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getTargetFragment().onActivityResult(
                        getTargetRequestCode(),
                        Activity.RESULT_OK,
                        new Intent().putExtra("selectedCoursePosition", mCheckedPosition)
                );

                dismiss();
            }
        });
    }

    private void setupRecyclerView() {
        RecyclerView recyclerView = mView.findViewById(R.id.dialog_course_picker_recycler_view_id);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        mAdapter = new SelectCourseRecycleAdapter(mCourses);
        handleToggleEvents();
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(mAdapter);
    }

    private void handleToggleEvents() {
        mAdapter.setOnItemClickListener(new SelectCourseRecycleAdapter.OnItemClickListener() {
            @Override
            public void onItemChecked(int position) {
                mCheckedPosition = position;
            }
        });

    }


    @Override
    public void onResume() {
        super.onResume();
        getDialog().getWindow().setLayout(Common.getEquivalentPx(300), ConstraintLayout.LayoutParams.WRAP_CONTENT);
        getDialog().getWindow().setGravity(Gravity.CENTER);

    }


}
