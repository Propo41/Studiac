package com.valhalla.studiac.fragments.dialogs;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.valhalla.studiac.R;
import com.valhalla.studiac.utility.common.Course;

public class CourseDetailsDialog extends AppCompatDialogFragment {


    private Button mContinueButtonView;
    private FloatingActionButton mCounsellingHourButtonView;
    private Double mCourseCredit;
    private String mCourseCode;
    private String mCourseName;
    private String mInstructorName;
    private String mInstructorRoom;
    private String mInstructorContact;
    private Course mCourse;
    private View mView;

    public CourseDetailsDialog(Course course) {
        mCourse = course;
        mCourseCode = course.getCode();
        mCourseName = course.getName();
        mCourseCredit = course.getCredit();
        mInstructorContact = course.getInstructor().getEmail();
        mInstructorName = course.getInstructor().getName();
        mInstructorRoom = course.getInstructor().getRoom();
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
        View view = inflater.inflate(R.layout.dialog_viewcourses_description, null);
        mView = view;
        setContent();
        onCounsellingClick(view);
        onContinueClick(view);

        builder.setView(view);
        AlertDialog alertDialog = builder.create();
        alertDialog.getWindow().setLayout(205, 600);
        return alertDialog;
    }


    private void onCounsellingClick(View view) {
        mCounsellingHourButtonView = view.findViewById(R.id.dialog_viewCourse_counsellingHours_button_id);
        mCounsellingHourButtonView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ViewCounsellingTimeDialog dialog = new ViewCounsellingTimeDialog(mCourse.getInstructor().getCounsellingTime());
                assert getFragmentManager() != null;
                dialog.show(getFragmentManager(), "courseDetailsFragment");
            }
        });

    }

    private void onContinueClick(View view) {
        mContinueButtonView = view.findViewById(R.id.dialog_viewCourse_continue_button_id);
        mContinueButtonView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

    private void setContent() {
        TextView courseName = mView.findViewById(R.id.dialog_viewCourse_courseName);
        courseName.setText(mCourseName);

        TextView mCourseCreditView = mView.findViewById(R.id.dialog_viewCourse_courseCredit);
        mCourseCreditView.setText(String.valueOf(mCourseCredit));

        TextView mCourseCodeView = mView.findViewById(R.id.dialog_viewCourse_courseCode);
        mCourseCodeView.setText(mCourseCode);

        TextView mInstructorNameView = mView.findViewById(R.id.dialog_viewCourse_instructorName);
        mInstructorNameView.setText(mInstructorName);

        TextView mInstructorContactView = mView.findViewById(R.id.dialog_viewCourse_contact);
        mInstructorContactView.setText(mInstructorContact);

        TextView mInstructorRoomView = mView.findViewById(R.id.dialog_viewCourse_room);
        mInstructorRoomView.setText(mInstructorRoom);
    }

    @Override
    public void onResume() {
        super.onResume();
        getDialog().getWindow().setLayout(800, 1000);
    }


}
