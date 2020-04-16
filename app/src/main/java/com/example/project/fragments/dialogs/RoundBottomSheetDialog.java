package com.example.project.fragments.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
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

import com.example.project.R;
import com.example.project.utility.Common;
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
public class RoundBottomSheetDialog extends BottomSheetDialogFragment {

    private BottomSheetListener mBottomSheetListener;

    private Pair<String, View> mCategory = new Pair<>(null, null);
    private Pair<String, View> mSchedule = new Pair<>(null, null);
    private Pair<String, View> mType = new Pair<>(null, null);

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_todo_addtask, container, false);
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
        handleScheduleEvents(view);
        handleTypeEvents(view);
        handleAddEvents(view);
    }

    /*
     * logic for when the add button is pressed.
     * if all the mandatory fields are pressed, then the interface method onAddPressed() is invoked
     * and the input data is passed along as parameters to the parent Activity
     */
    private void handleAddEvents(final View view) {
        // add button
        Button addButton = view.findViewById(R.id.todo_ADD_add_id);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // input fields
                EditText addTaskView = view.findViewById(R.id.todo_ADD_inputTask_id);
                EditText addAdditionalNotesView = view.findViewById(R.id.todo_ADD_adNotes_id);

                String taskViewText = addTaskView.getText().toString();
                String additionalNotesText = addAdditionalNotesView.getText().toString();
                if (taskViewText.equals("")) {
                    Common.addStroke(addTaskView, 5);
                    addTaskView.setError("Field cannot be empty!");
                } else if (isInputProvided(view)) {
                    Task newTask = new Task(taskViewText, additionalNotesText, mSchedule.first, mCategory.first, mType.first);
                    mBottomSheetListener.onAddPressed(newTask);
                    Toast.makeText(getContext(), "Task added", Toast.LENGTH_SHORT).show();
                    dismiss();
                }
            }
        });
    }

    /*
     * checks if the buttons are selected or not. If they aren't then a prompt will be shown
     * returns true if all the required buttons are selected. Else false.
     */
    private boolean isInputProvided(View view) {
        boolean a = false, b = false, c = false;
        if (mCategory.first != null) {
            view.findViewById(R.id.todo_ADD_input_required_id1).setVisibility(View.INVISIBLE);
            a = true;
        }
        if (mCategory.first == null) {
            view.findViewById(R.id.todo_ADD_input_required_id1).setVisibility(View.VISIBLE);
        }

        if (mSchedule.first != null) {
            view.findViewById(R.id.todo_ADD_input_required_id2).setVisibility(View.INVISIBLE);
            b = true;
        }
        if (mSchedule.first == null) {
            view.findViewById(R.id.todo_ADD_input_required_id2).setVisibility(View.VISIBLE);
        }

        if (mType.first != null) {
            view.findViewById(R.id.todo_ADD_input_required_id3).setVisibility(View.INVISIBLE);
            c = true;
        }
        if (mType.first == null) {
            view.findViewById(R.id.todo_ADD_input_required_id3).setVisibility(View.VISIBLE);
        }

        return a && b && c;
    }


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
                mType = checkIfToggled(mType, "quiz", v);
            }
        });

        ImageButton assignmentButton = view.findViewById(R.id.todo_ADD_assignment_id);
        assignmentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mType = checkIfToggled(mType, "assignment", v);
            }
        });

        ImageButton moreButton = view.findViewById(R.id.todo_ADD_more_id);
        moreButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // todo: open dialog 30. Iff an item is selected, continue, else mark unchecked

                mType = checkIfToggled(mType, "more", v);
            }
        });
    }

    private void handleScheduleEvents(View view) {

        // schedule buttons
        ImageButton currentTasksButton = view.findViewById(R.id.todo_ADD_currentTask_id);
        currentTasksButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSchedule = checkIfToggled(mSchedule, "Current Tasks", v);
            }
        });

        ImageButton currentWeekButton = view.findViewById(R.id.todo_ADD_currentWeek_id);
        currentWeekButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: open dialog 29. It will return a day string
                String day = "current_week";
                mSchedule = checkIfToggled(mSchedule, day, v);
            }
        });

        ImageButton futureButton = view.findViewById(R.id.todo_ADD_future_id);
        futureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // todo: open calender dialog. will return a string in format day/month/year
                String date = "future";
                mSchedule = checkIfToggled(mSchedule, date, v);
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
            mBottomSheetListener = (BottomSheetListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement BottomSheetListener");
        }
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





