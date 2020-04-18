package com.example.project.fragments.dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
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
import androidx.fragment.app.Fragment;

import com.example.project.R;
import com.example.project.utility.common.Common;
import com.example.project.utility.common.Course;
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
public class AddTaskMainBSDialog extends BottomSheetDialogFragment {

    private BottomSheetListener mBottomSheetListener;

    private Pair<String, View> mCategory = new Pair<>(null, null);
    private Pair<String, View> mType = new Pair<>(null, null);
    private String mTag;

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

    @Override
    public void onDetach() {
        super.onDetach();
        mBottomSheetListener = null;
    }

    public AddTaskMainBSDialog(){
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_todo_addtask, container, false);
        mTag = getTag();
       // Toast.makeText(getActivity(), "mtag:" + mTag, Toast.LENGTH_SHORT).show();
        // if the add button is clicked anywhere but current tasks, then change the button text
        // to Next.
        if(!mTag.equals("currentTasks")){
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

                    Toast.makeText(getContext(), "all input valid", Toast.LENGTH_SHORT).show();

                    // for debug. Use actual values later on
                    Course course = new Course("Algorithm", "ALGO 1234", 3.0 );

                    Task newTask = new Task(taskDescription, additionalNotes,
                            new Pair<String, Object>("Course", course), "Quiz");
                    mBottomSheetListener.onAddPressed(newTask);
                    if(mTag.equals("currentWeek")){
                        // todo: open dialog 29
                        // add logic for when no item is selected, then cannot continue
                    }else if(mTag.equals("upcoming")){
                        // todo: open calender dialog
                        // add logic for when no item is selected

                    }else{
                        Toast.makeText(getContext(), "Task added", Toast.LENGTH_SHORT).show();
                        dismiss();
                    }


                }
            }
        });


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





