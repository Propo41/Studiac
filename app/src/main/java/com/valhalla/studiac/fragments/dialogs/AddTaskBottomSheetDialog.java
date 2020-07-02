package com.valhalla.studiac.fragments.dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.util.Pair;
import androidx.fragment.app.FragmentManager;

import com.valhalla.studiac.R;
import com.valhalla.studiac.models.Course;
import com.valhalla.studiac.models.Task;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.valhalla.studiac.utility.Common;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Objects;


/*
 * A custom BottomSheetDialog class that uses the style BaseBottomSheetDialog
 * to show a round dialog box that pops up from the bottom
 *
 * The dialog is opened whenever the FAB Add button is pressed.
 *
 * parent Fragment: CurrentTasksFragment, CurrentWeekFragment, UpcomingFragment
 * parent Activity: TodoTasksActivity
 */
public class AddTaskBottomSheetDialog extends BottomSheetDialogFragment {

    private BottomSheetListener mBottomSheetListener;
    private Pair<String, View> mCategory = new Pair<>(null, null);
    private Pair<String, View> mType = new Pair<>(null, null);
    private String mTag;
    private String mSelectedDate;
    private View mCourseButtonView;
    private View mMoreButtonView;
    private Task mNewTask;
    public static final int REQUEST_CODE_SELECT_DAY = 10; // Used to identify the result obtained from the DAY picker dialog
    public static final int REQUEST_CODE_SELECT_COURSE = 11; // Used to identify the result obtained from the time picker dialog
    public static final int REQUEST_CODE_SELECT_TYPE = 12;// Used to identify the result obtained from the SELECT type  dialog
    private ArrayList<Course> mCourses;


    public AddTaskBottomSheetDialog(ArrayList<Course> courses) {
        mCourses = courses;
    }


    // we use an interface here so that we can handle the events
    // in the parent class of this fragment
    public interface BottomSheetListener {
        void onAddPressed(Task task);

    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Common.setBottomSheetDialogState(Objects.requireNonNull(getDialog()), "EXPANDED");
        View view = inflater.inflate(R.layout.dialog_todo_addtask, container, false);
        mTag = getTag();
        // if the add button is clicked anywhere but current tasks, then change the button text to Next.
        assert mTag != null;
        if (mTag.equals("upcoming")) {
            Button button = view.findViewById(R.id.todo_ADD_add_id);
            button.setText("Next");
        }

        handleEvents(view);
        // reset the stroke colour when creating the view
        Common.addStroke(view.findViewById(R.id.todo_ADD_inputTask_id), 0);
        return view;
    }


    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return new BottomSheetDialog(requireContext(), getTheme());
    }

    @Override
    public void onAttach(@NonNull Context context) {
        // condition to check if parent activity implemented this interface or not
        super.onAttach(context);
        try {
            mBottomSheetListener = (BottomSheetListener) getTargetFragment();
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement BottomSheetListener");
        }
    }

    /*
     * https://stackoverflow.com/questions/20114485/use-onactivityresult-android
     * https://brandonlehr.com/android/learn-to-code/2018/08/19/callling-android-datepicker-fragment-from-a-fragment-and-getting-the-date
     * it is used to obtain the result of the date selected from the datePickerDialogFragment
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        Toast.makeText(getContext(), requestCode + " " + resultCode, Toast.LENGTH_SHORT).show();

        // check for the results
        if (requestCode == REQUEST_CODE_SELECT_DAY && resultCode == Activity.RESULT_OK) {
            // get date from string
            mSelectedDate = data.getStringExtra("selectedDate");
            mNewTask.setSchedule(mSelectedDate);
            mBottomSheetListener.onAddPressed(mNewTask);
            Toast.makeText(getContext(), "Task added", Toast.LENGTH_SHORT).show();
            dismiss();
        } else if (requestCode == REQUEST_CODE_SELECT_COURSE && resultCode == Activity.RESULT_OK) {
            int pos = data.getExtras().getInt("selectedCoursePosition");
            mCategory = checkIfToggled(mCategory, "Course", mCourseButtonView);
            mCategory = new Pair<>(mCourses.get(pos).getCode(), mCourseButtonView);
        } else if (requestCode == REQUEST_CODE_SELECT_TYPE && resultCode == Activity.RESULT_OK) {
            String type = data.getExtras().getString("selectedType");
            mType = new Pair<>(type, mMoreButtonView);

        }

    }

    @Override
    public void onDetach() {
        super.onDetach();
        mBottomSheetListener = null;

    }


    /*
     * handles all user events
     */
    private void handleEvents(final View view) {
        handleCategoryEvents(view);
        handleTypeEvents(view);
        handleAddEvent(view);
    }

    /*
     * logic for when the add button is pressed.
     * if all the mandatory fields are filled, then the interface method onAddPressed() is invoked
     * and the input data is passed along as parameters to the parent Activity
     */
    private void handleAddEvent(final View view) {

        // add button
        Button addButton = view.findViewById(R.id.todo_ADD_add_id);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // input fields
                EditText taskDescriptionTv = view.findViewById(R.id.todo_ADD_inputTask_id);
                EditText additionalNotesTv = view.findViewById(R.id.todo_ADD_adNotes_id);
                String taskDescription = taskDescriptionTv.getText().toString();
                String additionalNotes = additionalNotesTv.getText().toString();
                // if field is empty, show prompt
                if (taskDescription.equals("")) {
                    Common.addStroke(taskDescriptionTv, 5);
                    taskDescriptionTv.setError("Field cannot be empty!");
                } else if (isInputProvided(view)) {
                    String category = mCategory.first; // holds either courseCode, Self Study or Others
                    String type = mType.first;
                    String dateCreated = Common.convertToDateFormat(Calendar.getInstance().getTime());

                    if (additionalNotes.equals("")) {
                        additionalNotes = null;
                    }
                    // by default, schedule will be set to null. If the dialog is opened from upcoming,
                    // only then the schedule will be replaced in the method onActivityResult()
                    mNewTask = new Task(taskDescription, additionalNotes, category, null, type, dateCreated);

                    // the following code is used to trigger what happens when the Button is pressed
                    // while in different fragments:
                    // If the bottom sheet is opened from the Upcoming tab, then on pressing the button,
                    // open the date picker dialog
                    if (mTag.equals("upcoming")) {
                        // open the date picker dialog and don't dismiss the bottom sheet unless a date
                        // is selected
                        showDatePickerDialog();
                    }
                    // If the bottom sheet is opened from the CurrentTasks or CurrentWeek tab, then
                    // simply continue
                    else {
                        mBottomSheetListener.onAddPressed(mNewTask);
                        mNewTask.setSchedule(null);
                        Toast.makeText(getContext(), "Task added", Toast.LENGTH_SHORT).show();
                        dismiss();
                    }
                }
            }
        });


    }


    private void showDatePickerDialog() {
        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
        // create the datePickerFragment
        DatePickerDialog dialog = new DatePickerDialog();
        // set the targetFragment to receive the results, specifying the request code
        dialog.setTargetFragment(AddTaskBottomSheetDialog.this, REQUEST_CODE_SELECT_DAY);
        // show the datePicker
        dialog.show(fragmentManager, Common.TODO);
    }


    /*
     * checks if the buttons are selected or not. If they aren't then a prompt will be shown
     * returns true if all the required buttons are selected. Else false.
     */
    private boolean isInputProvided(View view) {
        // boolean a = false;
        // boolean b = false;
        if (mCategory.first != null) {
            view.findViewById(R.id.todo_ADD_input_required_id1).setVisibility(View.INVISIBLE);
            //  a = true;
        } else {
            view.findViewById(R.id.todo_ADD_input_required_id1).setVisibility(View.VISIBLE);
            return false;
        }
        return true;

       /* if (mType.first != null) {
            view.findViewById(R.id.todo_ADD_input_required_id3).setVisibility(View.INVISIBLE);
            b = true;
        }
        if (mType.first == null) {
            view.findViewById(R.id.todo_ADD_input_required_id3).setVisibility(View.VISIBLE);
        }*/

        // return a;
    }


    /**
     * returns a pair object having name of category: Name of course/Others/Self Study
     * and the View object of the button.
     * it is used to check if a button is toggled or not
     * @returns: pair<null,null> if unchecked
     * @returns: pair<string,view> if checked
     */
    public Pair<String, View> checkIfToggled(Pair<String, View> current, String name, View view) {

        int CHECKED = R.drawable.ripple_effect_2; // ripple effect for checked state
        int UNCHECKED = R.drawable.ripple_effect_1; // ripple effect for unchecked state
        if (current.first == null && current.second == null) {
            // then check the item and return it
            view.setBackgroundResource(CHECKED);
            return new Pair<>(name, view);
        } else {
            if (current.second != null) {
                Pair<String, View> pair = new Pair<>(name, view);
                // if the pressed button is the same as before
                if (pair.equals(current)) {
                    // remove toggle from it
                    current.second.setBackgroundResource(UNCHECKED);
                    return new Pair<>(null, null);
                } else {
                    // set the new button as toggled and return it
                    current.second.setBackgroundResource(UNCHECKED);
                    view.setBackgroundResource(CHECKED);
                    return pair;
                }
            } else {
                Log.d("TaskAddBottomSheet", "current.second is null");
                return null;
            }

        }

    }

    private void handleTypeEvents(View view) {
        // type buttons
        ImageButton quizButton = view.findViewById(R.id.todo_ADD_quiz_id);
        quizButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mType = checkIfToggled(mType, "Quiz", v);
            }
        });

        ImageButton assignmentButton = view.findViewById(R.id.todo_ADD_assignment_id);
        assignmentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mType = checkIfToggled(mType, "Assignment", v);
            }
        });

        ImageButton moreButton = view.findViewById(R.id.todo_ADD_more_id);
        moreButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!Objects.equals(mType.first, "More")){
                    SelectTagBottomSheetDialog dialog = new SelectTagBottomSheetDialog();
                    dialog.setTargetFragment(AddTaskBottomSheetDialog.this, REQUEST_CODE_SELECT_TYPE);
                    dialog.show(requireFragmentManager(), "AddTaskBottomSheetDialog");
                }
                mType = checkIfToggled(mType, "More", v);

                mMoreButtonView = v;

            }
        });
    }

    private void handleCategoryEvents(final View view) {
        // category buttons
        ImageButton courseButton = view.findViewById(R.id.todo_ADD_course_id);
        courseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCourseButtonView = v;
                if (mCourses.size() == 0) {
                    Toast.makeText(getContext(), "No Courses Added!", Toast.LENGTH_SHORT).show();
                } else {
                    // open bottom sheet dialog to add courses
                    if(!Objects.equals(mCategory.first, "Course")){
                        SelectCourseBottomSheetDialog dialog = new SelectCourseBottomSheetDialog(mCourses);
                        dialog.setTargetFragment(AddTaskBottomSheetDialog.this, REQUEST_CODE_SELECT_COURSE);
                        dialog.show(getParentFragmentManager(), "AddTaskBottomSheetDialog");
                    }
                    mCategory = checkIfToggled(mCategory, "Course", v); // toggle it

                }


            }
        });

        ImageButton selfStudyButton = view.findViewById(R.id.todo_ADD_selfStudy_id);
        selfStudyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCategory = checkIfToggled(mCategory, "Self Study", v);
            }
        });

        ImageButton othersButton = view.findViewById(R.id.todo_ADD_others_id);
        othersButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCategory = checkIfToggled(mCategory, "Others", v);
            }
        });

    }


    @Override
    public int getTheme() {
        return R.style.BaseBottomSheetDialog;
    }

}





