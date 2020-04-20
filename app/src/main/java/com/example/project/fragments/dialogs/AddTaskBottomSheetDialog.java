package com.example.project.fragments.dialogs;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
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
import com.example.project.R;
import com.example.project.utility.common.Common;
import com.example.project.utility.common.Course;
import com.example.project.utility.todo.Others;
import com.example.project.utility.todo.SelfStudy;
import com.example.project.utility.todo.Task;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;


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
    private Task mNewTask;
    public static final int REQUEST_CODE = 10; // Used to identify the result obtained from the time picker dialog


    // we use an interface here so that we can handle the events
    // in the parent class of this fragment
    public interface BottomSheetListener {
        void onAddPressed(Task task);

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
        // check for the results
        if (requestCode == REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            // get date from string
            mSelectedDate = data.getStringExtra("selectedDate");
            mNewTask.setSchedule(mSelectedDate);
            mBottomSheetListener.onAddPressed(mNewTask);
            Toast.makeText(getContext(), "Task added", Toast.LENGTH_SHORT).show();
            dismiss();
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
        View view = inflater.inflate(R.layout.dialog_todo_addtask, container, false);
        mTag = getTag();
        // if the add button is clicked anywhere but current tasks, then change the button text to Next.
        if (mTag.equals("upcoming")) {
            Button button = view.findViewById(R.id.todo_ADD_add_id);
            button.setText("Next");
        }

        handleEvents(view);
        // reset the stroke colour when creating the view
        Common.addStroke(view.findViewById(R.id.todo_ADD_inputTask_id), 0);
        return view;
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
     * if all the mandatory fields are pressed, then the interface method onAddPressed() is invoked
     * and the input data is passed along as parameters to the parent Activity
     */
    private void handleAddEvent(final View view) {

        Course course;
        String taskDescription;

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
                    Pair<String, Object> category;
                    String type = mType.first;
                    if (mCategory.first.equals("Others")) {
                        Others others = new Others();
                        category = new Pair<String, Object>("Others", others);
                    } else if (mCategory.first.equals("Self Study")) {
                        SelfStudy selfStudy = new SelfStudy();
                        category = new Pair<String, Object>("Self Study", selfStudy);
                    } else {
                        Course course = new Course("Algorithm", "ALGO 1234", 3.0);
                        category = new Pair<String, Object>("Course", course);
                    }


                    mNewTask = new Task(taskDescription, additionalNotes, category, type);
                    // If the bottom sheet is opened from the CurrentWeek tab
                    if (mTag.equals("currentWeek")) {
                        mBottomSheetListener.onAddPressed(mNewTask);
                        Toast.makeText(getContext(), "Task added", Toast.LENGTH_SHORT).show();
                        mNewTask.setSchedule(null);
                        dismiss();
                        // If the bottom sheet is opened from the Upcoming tab
                    } else if (mTag.equals("upcoming")) {
                        // open the date picker dialog and don't dismiss the bottom sheet unless a date
                        // is selected
                        showDatePickerDialog();
                    } else {
                        // If the bottom sheet is opened from the CurrentTasks tab
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
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        // create the datePickerFragment
        DatePickerFragment dialog = new DatePickerFragment();
        // set the targetFragment to receive the results, specifying the request code
        dialog.setTargetFragment(AddTaskBottomSheetDialog.this, REQUEST_CODE);
        // show the datePicker
        dialog.show(fragmentManager, "datePickerFromFragment");
    }




    /*
     * checks if the buttons are selected or not. If they aren't then a prompt will be shown
     * returns true if all the required buttons are selected. Else false.
     */
    private boolean isInputProvided(View view) {
        boolean a = false, b = false;
        if (mCategory.first != null) {
            view.findViewById(R.id.todo_ADD_input_required_id1).setVisibility(View.INVISIBLE);
            a = true;
        }
        if (mCategory.first == null) {
            view.findViewById(R.id.todo_ADD_input_required_id1).setVisibility(View.VISIBLE);
        }

        if (mType.first != null) {
            view.findViewById(R.id.todo_ADD_input_required_id3).setVisibility(View.INVISIBLE);
            b = true;
        }
        if (mType.first == null) {
            view.findViewById(R.id.todo_ADD_input_required_id3).setVisibility(View.VISIBLE);
        }

        return a && b;
    }


    /*
     * returns a pair object having name of category: Name of course/Others/Self Study
     * and the View object of the button.
     */
    public Pair<String, View> checkIfToggled(Pair<String, View> current, String name, View view) {

        int CHECKED = R.drawable.ripple_effect_2;
        if (current.first == null && current.second == null) {
            // then check the item and return it
            view.setBackgroundResource(CHECKED);
            return new Pair<>(name, view);
        } else {
            Pair<String, View> pair = new Pair<>(name, view);
            // if the pressed button is the same as before
            int UNCHECKED = R.drawable.ripple_effect_1;
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
                // todo: open dialog 30. Iff an item is selected, continue, else mark unchecked
                mType = checkIfToggled(mType, "More", v);
            }
        });
    }


    private void handleCategoryEvents(View view) {
        // category buttons
        ImageButton courseButton = view.findViewById(R.id.todo_ADD_course_id);
        courseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // todo: open dialog 31. Iff an item is selected, continue, else mark unchecked
                // the dialog will return a string which will be courseNameSelected
                String courseNameSelected = "course";
                mCategory = checkIfToggled(mCategory, courseNameSelected, v);
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

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return new BottomSheetDialog(getContext(), getTheme());
    }
}





