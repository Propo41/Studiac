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

import com.valhalla.studiac.R;

public class SemesterResultDisplayDialog extends AppCompatDialogFragment {

    private String mGPA;
    private String mCourseTaken;


    public SemesterResultDisplayDialog(String mGPA, String mCourseTaken) {
        this.mGPA = mGPA;
        this.mCourseTaken = mCourseTaken;
    }


    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_show_single_semester_result, null);
        builder.setView(view);
        initializeViews(view);

        AlertDialog alertDialog = builder.create();
        // makes background transparent
        Window window = alertDialog.getWindow();
        if (window != null) {
            window.setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            WindowManager.LayoutParams windowManager = window.getAttributes();
            windowManager.gravity = Gravity.CENTER;
            window.getAttributes().windowAnimations = R.style.DialogAnimationSliding;
        }

        return alertDialog;
    }


    private void initializeViews(View view) {
        TextView mGPAEditText = view.findViewById(R.id.dialog_semester_result_display_gpa_text2);
        TextView mCoursesTakenEditText = view.findViewById(R.id.dialog_semester_result_display_course_text);
        mGPAEditText.setText(getString(R.string.PlaceHolder_GPA, mGPA));
        mCoursesTakenEditText.setText(getString(R.string.PlaceHolder_CoursesTaken, mCourseTaken));
        Button mContinueButton = view.findViewById(R.id.dialog_semester_result_display_continuebtn);
        mContinueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }


}
