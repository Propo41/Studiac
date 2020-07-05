package com.valhalla.studiac.fragments.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.valhalla.studiac.R;
import com.valhalla.studiac.adapters.result_tracker.SemesterResultDisplayRecycleAdapter;
import com.valhalla.studiac.models.Result;

import java.util.Locale;

public class SemesterResultDisplayDialog extends AppCompatDialogFragment {

    private Result mResult;
    private TextView mGPAText;
    private TextView mNumberOfCourse;
    private double mGPA;
    private int mCoursesTaken;

    public SemesterResultDisplayDialog(Result result) {
        mResult = result;
        mGPA = result.getGpa();
        mCoursesTaken = result.getCoursesTaken();
    }


    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_semester_result_display, null);
        initializeViews(view);
        builder.setView(view);
        AlertDialog alertDialog = builder.create();
        Window window = alertDialog.getWindow();

        if (window != null) {
            window.setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            window.getAttributes().windowAnimations = R.style.DialogBottomAnimationSliding;
            WindowManager.LayoutParams windowManager = alertDialog.getWindow().getAttributes();
            // positions the dialog in bottom|center
            windowManager.gravity = Gravity.BOTTOM;
            windowManager.y = 20;

        }
        return alertDialog;
    }

    private void initializeViews(View view) {
        Button mContinueButton = view.findViewById(R.id.dialog_semester_result_display_continuebtn);
        mGPAText = view.findViewById(R.id.dialog_semester_result_gpa);
        mNumberOfCourse = view.findViewById(R.id.dialog_semester_result_courses_taken);
        mContinueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        setContent();
        setupRecyclerView(view);


    }

    private void setupRecyclerView(View view) {
        RecyclerView mRecycleView = view.findViewById(R.id.dialog_semester_result_display_recyclerview);
        if (mResult.getGradesObtained().size() != 0) {
            mRecycleView.setVisibility(View.VISIBLE);
            mRecycleView.setHasFixedSize(true);
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
            SemesterResultDisplayRecycleAdapter mAdapter = new SemesterResultDisplayRecycleAdapter(mResult);
            mRecycleView.setLayoutManager(mLayoutManager);
            mRecycleView.setAdapter(mAdapter);
        } else {
            mRecycleView.setVisibility(View.GONE);
        }

    }

    private void setContent() {
        mGPAText.setText(String.format(Locale.getDefault(), "%.3f", mGPA));
        String courses = mCoursesTaken + "";
        mNumberOfCourse.setText(courses);
    }

}
