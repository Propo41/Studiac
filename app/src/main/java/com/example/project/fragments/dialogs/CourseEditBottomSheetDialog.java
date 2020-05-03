package com.example.project.fragments.dialogs;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.project.R;
import com.example.project.utility.common.Common;
import com.example.project.utility.common.Course;
import com.example.project.utility.common.Instructor;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CourseEditBottomSheetDialog extends BottomSheetDialogFragment implements View.OnClickListener {

    private EditText mCourseCredit;
    private EditText mCourseCode;
    private EditText mInstructorName;
    private EditText mInstructorEmail;
    private EditText mInstructorRoomNumber;
    private FloatingActionButton mCounsellingHourBtn;
    private TextView mCourseName;
    private Button mUpdateBtn;
    private Button mDeleteBtn;

    private Course mCourse;
    private OnUpdateClickListener mListener;

    private static final Pattern EMAIL_PATTERN =
            Pattern.compile(
                    "[a-zA-Z0-9+._%\\-+]{1,256}" +
                            "@" +
                            "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
                            "(" +
                            "\\." +
                            "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" +
                            ")+"
            );


    private Pattern ps = Pattern.compile("^[a-zA-Z ]+$");

    public CourseEditBottomSheetDialog(Course course) {
        mCourse = course;
    }


    public interface OnUpdateClickListener {
        void onUpdateClick();

        void onDeleteClick();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Common.setBottomSheetDialogState(getDialog(), "EXPANDED");
        View view = inflater.inflate(R.layout.dialog_viewcourses_edit_course, container, false);
        initializeViews(view);
        setContent();
        return view;
    }

    public void setOnUpdateClickListener(OnUpdateClickListener listener) {
        mListener = listener;
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
        return new BottomSheetDialog(getContext(), getTheme());
    }

    private void setContent() {
        mCourseName.setText(mCourse.getName());
        mCourseCode.setText(mCourse.getCode());
        mCourseCredit.setText(String.valueOf(mCourse.getCredit()));
        Instructor instructor = mCourse.getInstructor();

        String unavailable = getResources().getString(R.string.Unavailable);

        if (!instructor.getName().equals(unavailable)) {
            mInstructorName.setText(instructor.getName());
        }
        if (!instructor.getEmail().equals(unavailable)) {
            mInstructorEmail.setText(instructor.getEmail());
        }
        if (!instructor.getRoom().equals(unavailable)) {
            mInstructorRoomNumber.setText(instructor.getRoom());
        }
    }

    private void initializeViews(View view) {
        mCourseName = view.findViewById(R.id.dialog_viewCourses_edit_courseName_id);
        mCourseCredit = view.findViewById(R.id.dialog_viewCourses_edit_courseCredit_id);
        mCourseCode = view.findViewById(R.id.dialog_viewCourses_edit_courseCode_id);
        mInstructorName = view.findViewById(R.id.dialog_viewCourses_edit_instructorName_id);
        mInstructorEmail = view.findViewById(R.id.dialog_viewCourses_edit_instructorEmail_id);
        mCounsellingHourBtn = view.findViewById(R.id.dialog_viewCourses_edit_addCounsellingHour_button_id);
        mInstructorRoomNumber = view.findViewById(R.id.dialog_viewCourses_edit_instructorRoom_id);
        mUpdateBtn = view.findViewById(R.id.dialog_viewCourses_edit_updateButton_id);
        mDeleteBtn = view.findViewById(R.id.dialog_viewCourses_edit_delete_id);

        mCounsellingHourBtn.setOnClickListener(this);
        mUpdateBtn.setOnClickListener(this);
        mDeleteBtn.setOnClickListener(this);
    }

    @Override
    public int getTheme() {
        return R.style.BaseBottomSheetDialog;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == mCounsellingHourBtn.getId()) {
            // open dialog D
            SetupAddRoutineDialog dialog = new SetupAddRoutineDialog(mCourse.getInstructor().getCounsellingTime(), false);
            assert getFragmentManager() != null;
            dialog.show(getFragmentManager(), getResources().getString(R.string.AddCounsellingHoursFragment));
        }

        if (v.getId() == mDeleteBtn.getId()) {
            mListener.onDeleteClick();
            dismiss();
        }

        if (v.getId() == mUpdateBtn.getId()) {
            // boolean check = isInputValid();
            boolean check = true;
            if (check) {
                mCourse.setCredit(Double.parseDouble(mCourseCredit.getText().toString()));
                mCourse.setCode(mCourseCode.getText().toString());
                mCourse.setName(mCourseName.getText().toString());

                mCourse.getInstructor().setName(mInstructorName.getText().toString());
                mCourse.getInstructor().setEmail(mInstructorEmail.getText().toString());
                mCourse.getInstructor().setRoom(mInstructorRoomNumber.getText().toString());

                mListener.onUpdateClick();
                dismiss();
            }
        }
    }

    private boolean isInputValid() {
        // todo: if course name/ course code/ course credit is not entered then return false
        // for the rest, they are all optional. But the formats must be valid if entered
        int count = 0;
        if (mCourseCode.getText().length() == 0) {
            mCourseCode.setError("Field Cannot Be Empty");
            Common.addStroke(mCourseCode, 5);
        } else {
            Common.addStroke(mCourseCode, 0);
            count++;
        }

        if (mCourseCredit.getText().length() == 0) {
            mCourseCredit.setError("Field Cannot Be Empty");
            Common.addStroke(mCourseCode, 5);
        } else {
            Common.addStroke(mCourseCode, 0);
            count++;
        }

        if (mInstructorEmail.getText().length() == 0) {
            mInstructorEmail.setError("Field Cannot Be Empty");
            Common.addStroke(mCourseCode, 5);
        } else {
            String emailInput = mInstructorEmail.getText().toString().trim();
            if (!EMAIL_PATTERN.matcher(emailInput).matches()) {
                mInstructorEmail.setError("Invalid Email");
                Common.addStroke(mCourseCode, 5);
            } else {
                Common.addStroke(mCourseCode, 0);
                count++;
            }
        }

        if (mInstructorName.getText().length() == 0) {
            mInstructorName.setError("Field Cannot Be Empty");
            Common.addStroke(mCourseCode, 5);
        } else {
            Matcher ms = ps.matcher(mInstructorName.getText().toString());
            boolean bs = ms.matches();
            if (!bs) {
                mInstructorName.setError("Name cannot contain any number");
                Common.addStroke(mCourseCode, 5);
            } else {
                Common.addStroke(mCourseCode, 0);
                count++;
            }
        }

        if (mInstructorRoomNumber.getText().length() == 0) {
            mInstructorRoomNumber.setError("Field Cannot Be Empty");
            Common.addStroke(mCourseCode, 5);
        } else {
            Common.addStroke(mCourseCode, 0);
            count++;
        }


        boolean returnValue;
        if (count == 5) {
            returnValue = true;
        } else {
            returnValue = false;
        }

        return returnValue;
    }


}
