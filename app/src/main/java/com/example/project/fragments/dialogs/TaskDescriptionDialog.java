package com.example.project.fragments.dialogs;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.example.project.R;
import com.example.project.utility.todo.Task;

import java.util.ArrayList;

public class TaskDescriptionDialog extends AppCompatDialogFragment {

    private TextView mTaskName;
    private TextView mTaskCategory;
    private TextView mTaskType;
    private TextView mTaskDue;
    private Button mContinueBtn;
    private Button mDeleteButton;
    private Task mTask;
    private int mIndexPosition;

    public TaskDescriptionDialog(Task task, int pos) {
        mIndexPosition = pos;
        mTask = task;
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
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_task_description, null);
        initializeViews(view);
        setContent();
        builder.setView(view);
        AlertDialog alertDialog = builder.create();
        alertDialog.getWindow().setLayout(205, 2000);
        return alertDialog;
    }

    private void setContent() {
        mTaskName.setText(mTask.getDescription());
        mTaskCategory.setText(mTask.getCategory());
        mTaskType.setText(mTask.getType());
        if (mTask.getSchedule() == null) {
            mTaskDue.setText("Current");
        } else {
            mTaskDue.setText(mTask.getSchedule());
        }
    }


    private void initializeViews(View view) {
        mTaskName = view.findViewById(R.id.dialog_task_description_taskName);
        mTaskCategory = view.findViewById(R.id.dialog_task_description_taskCategory);
        mTaskType = view.findViewById(R.id.dialog_task_description_taskType);
        mTaskDue = view.findViewById(R.id.dialog_task_description_taskDue);
        mContinueBtn = view.findViewById(R.id.dialog_task_description_continueBtn);

        mContinueBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        mDeleteButton = view.findViewById(R.id.dialog_task_description_deleteButton);
        mDeleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getTargetFragment().onActivityResult(
                        getTargetRequestCode(),
                        Activity.RESULT_OK,
                        new Intent().putExtra("pos", mIndexPosition)
                );

                dismiss();

            }
        });


    }

    @Override
    public void onResume() {
        super.onResume();
        getDialog().getWindow().setLayout(800, 1000);
    }


}
