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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

    private Pattern ps = Pattern.compile("^[a-zA-Z ]+$");

    public CourseEditBottomSheetDialog(Course course) {
        mCourse = course;
    }


    public interface OnUpdateClickListener {
        void onUpdateClick(boolean changed, String oldCode);

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
        handleEvents();
        setContent();
        return view;
    }

    private void handleEvents() {
        mCounsellingHourBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // open dialog D
                if (mCourse.getInstructor().getCounsellingTime() == null) {
                    // if schedule list is null, then initiate it
                    ArrayList<Schedule> counsellingTime = new ArrayList<>();
                    mCourse.getInstructor().setCounsellingTime(counsellingTime);
                }

                SetupAddRoutineDialog dialog = new SetupAddRoutineDialog(mCourse.getInstructor().getCounsellingTime(), mVisited);
                dialog.setTargetFragment(CourseEditBottomSheetDialog.this, Common.COUNSELLING_TIME);
                dialog.show(requireFragmentManager(), "AddCounsellingHoursFragment");
            }
        });


        mUpdateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean check = isInputValid();
                //boolean check = true; // for debug
                if (check) {
                    boolean changed = false;
                    String oldCode = mCourse.getCode();

                    mCourse.setCredit(Double.parseDouble(mCourseCredit.getText().toString()));
                    mCourse.setName(mCourseName.getText().toString());
                    if (!oldCode.equals(mCourseCode.getText().toString())) {
                        changed = true;
                        mCourse.setCode(mCourseCode.getText().toString());
                    }

                    Instructor instructor = mCourse.getInstructor();
                    if (!mInstructorName.getText().toString().equals("")) {
                        instructor.setName(mInstructorName.getText().toString());
                    }
                    if (!mInstructorEmail.getText().toString().equals("")) {
                        instructor.setEmail(mInstructorEmail.getText().toString());
                    }
                    if (!mInstructorRoomNumber.getText().toString().equals("")) {
                        instructor.setRoom(mInstructorRoomNumber.getText().toString());
                    }

                    // send the old code to the listener
                    mListener.onUpdateClick(changed, oldCode);

                    dismiss();
                }

            }
        });

        mDeleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onDeleteClick();
                dismiss();
            }
        });

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
                mCourse.setCredit(Double.parseDouble(mCourseCredit.getText().toString()));
                mCourse.setName(mCourseName.getText().toString());

                mCourse.getInstructor().setName(mInstructorName.getText().toString());
                mCourse.getInstructor().setEmail(mInstructorEmail.getText().toString());
                mCourse.getInstructor().setRoom(mInstructorRoomNumber.getText().toString());

                // if the course code is changed, then update it
                if (!mCourseCode.getText().toString().equals(mCourse.getCode())) {
                    mListener.onUpdateClick(true, mCourse.getCode());
                    mCourse.setCode(mCourseCode.getText().toString());
                } else {
                    mListener.onUpdateClick(false, null);
                }

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
