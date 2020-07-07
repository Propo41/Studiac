package com.valhalla.studiac.fragments.dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.valhalla.studiac.R;
import com.valhalla.studiac.models.Course;
import com.valhalla.studiac.models.Instructor;
import com.valhalla.studiac.models.Schedule;
import com.valhalla.studiac.utility.Common;
import com.valhalla.studiac.utility.ErrorStyle;

import java.util.ArrayList;

public class CourseEditBottomSheetDialog extends BottomSheetDialogFragment implements View.OnClickListener {

    private EditText mCourseCredit;
    private EditText mCourseCode;
    private EditText mInstructorName;
    private EditText mInstructorEmail;
    private EditText mInstructorRoomNumber;
    private EditText mCourseName;
    private FloatingActionButton mCounsellingHourBtn;
    private Button mUpdateBtn;
    private Button mDeleteBtn;
    private boolean[] mVisited = new boolean[7];
    private ErrorStyle errorStyle;


    private Course mCourse;
    private OnUpdateClickListener mListener;


    public CourseEditBottomSheetDialog(Course course) {
        mCourse = course;

    }


    public interface OnUpdateClickListener {
        void onUpdateClick(boolean changed, String oldCode, boolean nameChanged, String oldCourseName);

        void onDeleteClick();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_viewcourses_edit_course, container, false);


        Dialog dialog = getDialog();
        if (dialog != null) {
            Common.setBottomSheetDialogState(dialog, "HALF_EXPANDED");
        }
        errorStyle = ErrorStyle.getInstance(getContext());
        initializeViews(view);
        setContent();
        return view;
    }


    public void setOnUpdateClickListener(OnUpdateClickListener listener) {
        mListener = listener;
    }


    private void setContent() {
        mCourseName.setText(mCourse.getName());
        mCourseCode.setText(mCourse.getCode());
        mCourseCredit.setText(String.valueOf(mCourse.getCredit()));
        Instructor instructor = mCourse.getInstructor();

        String unavailable = Common.UNAVAILABLE;

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

        errorStyle.resetError(mCourseName);
        errorStyle.resetError(mCourseCredit);
        errorStyle.resetError(mCourseCode);
        errorStyle.resetError(mInstructorEmail);
        errorStyle.resetError(mInstructorName);
        errorStyle.resetError(mInstructorRoomNumber);


    }

    @Override
    public int getTheme() {
        return R.style.BaseBottomSheetDialog;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return new BottomSheetDialog(requireContext(), getTheme());
    }


    /**
     * handles events:
     * - add counselling hour
     * - delete course
     *
     * @param v the button view which the user clicked
     */

    @Override
    public void onClick(View v) {
        if (v.getId() == mCounsellingHourBtn.getId()) {
            // open dialog D
            if (mCourse.getInstructor().getCounsellingTime() == null) {
                ArrayList<Schedule> counsellingTime = new ArrayList<>();
                mCourse.getInstructor().setCounsellingTime(counsellingTime);
            }

            for (Schedule schedule : mCourse.getInstructor().getCounsellingTime()) {
                Integer index = Common.GET_INDEX_FROM_DAY.get(schedule.getName());
                if (index != null) {
                    mVisited[index] = true;
                }
            }

            SetupAddRoutineDialog dialog = new SetupAddRoutineDialog(mCourse.getInstructor().getCounsellingTime(), mVisited);
            dialog.setTargetFragment(CourseEditBottomSheetDialog.this, Common.COUNSELLING_TIME);
            dialog.show(requireFragmentManager(), "AddCounsellingHoursFragment");
        }


        if (v.getId() == mDeleteBtn.getId()) {
            mListener.onDeleteClick();
            dismiss();
        }


        if (v.getId() == mUpdateBtn.getId()) {
            if (isInputValid()) {
                String oldCode = null;
                boolean codeChanged = false;
                String oldName = null;
                boolean nameChanged = false;

                mCourse.setCredit(Double.parseDouble(mCourseCredit.getText().toString()));
                // if course name is changed, then update it
                if (!mCourse.getName().equals(mCourseName.getText().toString())) {
                    nameChanged = true;
                    oldName = mCourse.getName();
                    mCourse.setName(mCourseName.getText().toString());
                }
                // if the course code is changed, then update it
                if (!mCourseCode.getText().toString().equals(mCourse.getCode())) {
                    oldCode = mCourse.getCode();
                    codeChanged = true;
                    mCourse.setCode(mCourseCode.getText().toString());
                }

                Instructor instructor = mCourse.getInstructor();
                if (!mInstructorName.getText().toString().equals("")) {
                    instructor.setName(mInstructorName.getText().toString());
                }
                if (!mInstructorEmail.getText().toString().equals("")) {
                    instructor.setName(mInstructorEmail.getText().toString());
                }
                if (!mInstructorRoomNumber.getText().toString().equals("")) {
                    instructor.setName(mInstructorRoomNumber.getText().toString());
                }
                
                mListener.onUpdateClick(codeChanged, oldCode, nameChanged, oldName);


                dismiss();
            }

        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == Common.COUNSELLING_TIME && resultCode == Activity.RESULT_OK) {
            // fetch the transferred list of schedule from the setupAddRoutineDialog fragment
            if (data != null) {
                Bundle bundle = data.getExtras();
                if (bundle != null) {
                    ArrayList<Schedule> schedule = bundle.getParcelableArrayList("schedule");
                    mCourse.getInstructor().setCounsellingTime(schedule);
                }

            }


        }
    }

    private boolean isInputValid() {
        // for the rest, they are all optional. But the formats must be valid if entered
        if (mCourseName.getText().length() == 0) {
            errorStyle.setError(getString(R.string.Error_EmptyField), mCourseName);
            return false;
        } else {
            errorStyle.resetError(mCourseName);
        }


        if (mCourseCredit.getText().length() == 0) {
            errorStyle.setError(getString(R.string.Error_EmptyField), mCourseCredit);
            return false;
        } else {
            errorStyle.resetError(mCourseCredit);
        }


        if (mCourseCode.getText().length() == 0) {
            errorStyle.setError(getString(R.string.Error_EmptyField), mCourseCode);
            return false;
        } else {
            errorStyle.resetError(mCourseCode);
        }


        return true;
    }


}
