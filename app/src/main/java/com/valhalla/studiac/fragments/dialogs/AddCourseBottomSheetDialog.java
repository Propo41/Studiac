package com.valhalla.studiac.fragments.dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.valhalla.studiac.R;
import com.valhalla.studiac.utility.Common;
import com.valhalla.studiac.models.Course;
import com.valhalla.studiac.models.Schedule;

import java.util.ArrayList;


public class AddCourseBottomSheetDialog extends BottomSheetDialogFragment {

    private BottomSheetListener mBottomSheetListener;
    private ArrayList<Schedule> mCourseSchedule;
    private ArrayList<String> mDays;

    private final int RESULT_ADD_COURSE_DIALOG = 1;

    public AddCourseBottomSheetDialog() {
        mCourseSchedule = new ArrayList<>();

    }

    // we use an interface here so that we can handle the events
    // in the parent class of this fragment
    public interface BottomSheetListener {
        void onAddPressed(Course course, ArrayList<Schedule> courseSchedule);

    }

    @Override
    public void onAttach(@NonNull Context context) {
        // condition to check if parent activity implemented this interface or not
        super.onAttach(context);
        try {
            mBottomSheetListener = (BottomSheetListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement BottomSheetListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mBottomSheetListener = null;

    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Common.setBottomSheetDialogState(getDialog(), "EXPANDED");
        View view = inflater.inflate(R.layout.dialog_view_courses_add_course, container, false);
        handleEvents(view);
        return view;
    }


    /*
     * logic for when the add button is pressed.
     * if all the mandatory fields are filled, then the interface method onAddPressed() is invoked
     * and the input data is passed along as parameters to the parent Activity
     */
    private void handleEvents(final View view) {
        onAddRoutineClick(view);
        onAddButtonClick(view);
    }

    private void onAddButtonClick(final View view) {
        // add button
        Button addButton = view.findViewById(R.id.view_courses_add_button_id);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // input fields
                EditText courseNameTv = view.findViewById(R.id.view_courses_add_name_id);
                EditText courseCreditTv = view.findViewById(R.id.view_courses_add_credit_id);
                EditText courseCodeTv = view.findViewById(R.id.view_courses_add_code_id);
                if (isInputValid()) {

                    Course course = new Course(
                            courseNameTv.getText().toString(),
                            courseCodeTv.getText().toString(),
                            Double.parseDouble(courseCreditTv.getText().toString()),
                            mDays
                    );
                    mBottomSheetListener.onAddPressed(course, mCourseSchedule);

                    dismiss();
                }

            }
        });

    }

    private void onAddRoutineClick(View view) {
        FloatingActionButton addRoutineButton = view.findViewById(R.id.view_courses_add_routine_button_id);
        addRoutineButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SetupAddRoutineDialog dialog = new SetupAddRoutineDialog(mCourseSchedule, true);
                dialog.setTargetFragment(AddCourseBottomSheetDialog.this, Common.ADD_COURSE_DIALOG);
                dialog.show(getFragmentManager(), "addCourseDialog");
            }
        });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == RESULT_ADD_COURSE_DIALOG && resultCode == Activity.RESULT_OK) {
            mDays = data.getStringArrayListExtra("days");
        }
    }

    /*
     * checks if the input formats are valid and if at least one routine entry is added
     */
    private boolean isInputValid() {
        if (mCourseSchedule.size() == 0) {
            Toast.makeText(getContext(), "No routine added!", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    @Override
    public int getTheme() {
        return R.style.BaseBottomSheetDialog;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return new BottomSheetDialog(getContext(), getTheme());
    }
}





