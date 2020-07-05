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
import androidx.constraintlayout.widget.Group;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.valhalla.studiac.R;
import com.valhalla.studiac.models.Course;


public class CourseDetailsDialog extends AppCompatDialogFragment {


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

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_viewcourses_description, null);
        builder.setView(view);

        mView = view;
        setContent();
        onCounsellingClick(view);
        onContinueClick(view);

        AlertDialog alertDialog = builder.create();
        // makes background transparent
        Window window = alertDialog.getWindow();
        if (window != null) {
            window.setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            // removes title from dialog.
            alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

            WindowManager.LayoutParams windowManager = window.getAttributes();
            // positions the dialog in bottom|center
            windowManager.gravity = Gravity.BOTTOM;

            windowManager.y = 20;
            // window.getAttributes().windowAnimations = R.style.DialogAnimationSliding;
            window.getAttributes().windowAnimations = R.style.DialogBottomAnimationSliding;

        }


        return alertDialog;
    }


    private void onCounsellingClick(View view) {
        FloatingActionButton mCounsellingHourButtonView = view.findViewById(R.id.dialog_viewCourse_counsellingHours_button_id);
        mCounsellingHourButtonView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ViewCounsellingTimeDialog dialog = new ViewCounsellingTimeDialog(mCourse.getInstructor().getCounsellingTime());
                dialog.show(requireFragmentManager(), "courseDetailsFragment");
            }
        });

    }

    private void onContinueClick(View view) {
        Button mContinueButtonView = view.findViewById(R.id.dialog_viewCourse_continue_button_id);
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

        Group instructorContact = mView.findViewById(R.id.dialog_view_courses_group_contact);
        Group instructorRoom = mView.findViewById(R.id.dialog_view_courses_group_room);
        Group instructorCounselling = mView.findViewById(R.id.dialog_view_courses_group_counselling);
        Group instructorName = mView.findViewById(R.id.dialog_view_courses_group_name);
        Group instructorSeparator = mView.findViewById(R.id.dialog_view_courses_group_separator);

        // iff instructor name is provided, show the rest if not null
        if (!mInstructorName.equals("null")) {
            TextView mInstructorNameView = mView.findViewById(R.id.dialog_viewCourse_instructorName);
            mInstructorNameView.setText(mInstructorName);

            if (mInstructorRoom.equals("null")) {
                instructorRoom.setVisibility(View.GONE);
            } else {
                TextView mInstructorRoomView = mView.findViewById(R.id.dialog_viewCourse_room);
                mInstructorRoomView.setText(mInstructorRoom);
            }

            if (mInstructorContact.equals("null")) {
                instructorContact.setVisibility(View.GONE);
            } else {
                TextView mInstructorContactView = mView.findViewById(R.id.dialog_viewCourse_contact);
                mInstructorContactView.setText(mInstructorContact);
            }


            if (mCourse.getInstructor().getCounsellingTime() == null || mCourse.getInstructor().getCounsellingTime().size()==0) {
                instructorCounselling.setVisibility(View.GONE);
            }

        } else {
            instructorContact.setVisibility(View.GONE);
            instructorName.setVisibility(View.GONE);
            instructorCounselling.setVisibility(View.GONE);
            instructorSeparator.setVisibility(View.GONE);
            instructorRoom.setVisibility(View.GONE);


        }

    }


}

