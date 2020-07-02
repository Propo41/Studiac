package com.valhalla.studiac.fragments.dialogs;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
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

import com.google.android.material.appbar.AppBarLayout;
import com.valhalla.studiac.R;
import com.valhalla.studiac.utility.Common;
import com.valhalla.studiac.models.Course;
import com.valhalla.studiac.models.Instructor;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.valhalla.studiac.models.Schedule;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;
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
    private boolean[] mVisited = new boolean[7];


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
        void onUpdateClick(boolean changed, String oldCode);

        void onDeleteClick();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Common.setBottomSheetDialogState(getDialog(), "HALF_EXPANDED");
        View view = inflater.inflate(R.layout.dialog_viewcourses_edit_course, container, false);
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
                // boolean check = isInputValid();
                boolean check = true; // for debug
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

    }

    @Override
    public int getTheme() {
        return R.style.BaseBottomSheetDialog;
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
            // boolean check = isInputValid();
            boolean check = true; // for debug
            if (check) {
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
            Bundle bundle = data.getExtras();
            assert bundle != null;
            ArrayList<Schedule> schedule = bundle.getParcelableArrayList("schedule");
            mCourse.getInstructor().setCounsellingTime(schedule);


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
