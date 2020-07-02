package com.valhalla.studiac.fragments.dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.valhalla.studiac.R;
import com.valhalla.studiac.adapters.SpinnerAdapter;
import com.valhalla.studiac.holders.ImageItem;
import com.valhalla.studiac.utility.Common;
import com.valhalla.studiac.models.Course;
import com.valhalla.studiac.models.Schedule;

import java.util.ArrayList;
import java.util.Objects;


public class AddCourseBottomSheetDialog extends BottomSheetDialogFragment {

    private BottomSheetListener mBottomSheetListener;
    private ArrayList<Schedule> mCourseSchedule;
    private ArrayList<String> mDays;
    private ArrayList<ImageItem> mCourseTypeItems;
    private String mCourseTypeSelected;
    private final int RESULT_ADD_COURSE_DIALOG = 1;
    private boolean[] mVisited = new boolean[7];

    public AddCourseBottomSheetDialog() {
        mCourseSchedule = new ArrayList<>();
        initList();


    }

    private void initList() {
        mCourseTypeItems = new ArrayList<>();
        mCourseTypeItems.add(new ImageItem("Lecture", R.drawable.course_ic_type_lecture));
        mCourseTypeItems.add(new ImageItem("Practical", R.drawable.course_ic_type_practical));

        mCourseTypeSelected = "Lecture"; // default selected item


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
        Spinner spinner = view.findViewById(R.id.view_courses_course_type_id);
        populateSpinner(spinner);

        handleEvents(view);
        return view;
    }


    private void populateSpinner(Spinner spinner) {
        SpinnerAdapter adapter = new SpinnerAdapter(requireContext(), mCourseTypeItems, AddCourseBottomSheetDialog.class.getSimpleName());
        spinner.setAdapter(adapter);
        handleSpinnerEvents(spinner);

    }

    private void handleSpinnerEvents(Spinner spinner) {
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ImageItem clickedItem = (ImageItem) parent.getItemAtPosition(position);
                mCourseTypeSelected = clickedItem.getName();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

    }


    /**
     * logic for when the add button is pressed.
     * if all the mandatory fields are filled, then the interface method onAddPressed() is invoked
     * and the input data is passed along as parameters to the parent Activity
     *
     * @events: add course routine button
     * @events: update changes button
     */
    private void handleEvents(final View view) {
        onAddRoutineClick(view);
        onAddButtonClick(view);

        // for debug
        Button debug = view.findViewById(R.id.debug_import_button);
        debug.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDays = new ArrayList<>();
                mDays.add("Sunday");
                mCourseSchedule = new ArrayList<>();
                mCourseSchedule.add(new Schedule("Sunday", "03:00PM", "5:00PM"));

                Course course = new Course(
                        "Computer Architecture",
                        "CSE 2300",
                        3.0,
                        mDays,
                        "Lecture"
                );
                // add the course type to the schedule
                for (Schedule schedule : mCourseSchedule) {
                    schedule.setType(mCourseTypeSelected);
                    schedule.setDescription(course.getName());

                }
                mBottomSheetListener.onAddPressed(course, mCourseSchedule);
                dismiss();
            }
        });
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

                    // if all inputs are correct, add a new course instance
                    // and pass it to the parent using interface
                    Course course = new Course(
                            courseNameTv.getText().toString(),
                            courseCodeTv.getText().toString(),
                            Double.parseDouble(courseCreditTv.getText().toString()),
                            mDays,
                            mCourseTypeSelected
                    );
                    // add the course type to the schedule
                    for (Schedule schedule : mCourseSchedule) {
                        schedule.setType(mCourseTypeSelected);
                        schedule.setDescription(course.getName());

                    }
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
                SetupAddRoutineDialog dialog = new SetupAddRoutineDialog(mCourseSchedule, mVisited);
                dialog.setTargetFragment(AddCourseBottomSheetDialog.this, RESULT_ADD_COURSE_DIALOG);
                dialog.show(getParentFragmentManager(), "addCourseDialog");
            }
        });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == RESULT_ADD_COURSE_DIALOG && resultCode == Activity.RESULT_OK) {
            assert data != null;
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
        return new BottomSheetDialog(requireContext(), getTheme());
    }
}





