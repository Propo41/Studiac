package com.valhalla.studiac.fragments.dialogs;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.cardview.widget.CardView;
import androidx.core.content.res.ResourcesCompat;

import com.valhalla.studiac.R;
import com.valhalla.studiac.models.Task;
import com.valhalla.studiac.utility.Common;

import java.util.Objects;

public class TaskDescriptionDialog extends AppCompatDialogFragment {

    private TextView mTaskDescription;
    private TextView mTaskCategory;
    private TextView mTaskTag;
    private TextView mTaskDue;
    private TextView mTaskAdditionalNotes;
    private TextView mTaskCreatedDate;


    private TextView mHeaderTaskDue;
    private TextView mHeaderTaskAdditional;
    private TextView mHeaderTaskTag;
    private TextView mHeaderTaskCategory;
    private CardView mTaskTagColour;

    private Task mTask;
    private int mIndexPosition;


    public TaskDescriptionDialog(Task task, int pos) {
        mIndexPosition = pos;
        mTask = task;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_task_description, null);
        initializeViews(view);
        setContent();
        builder.setView(view);

        AlertDialog alertDialog = builder.create();
        Window window = alertDialog.getWindow();
        if (window != null) {
            window.setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            window.getAttributes().windowAnimations = R.style.DialogBottomAnimationSliding;
            WindowManager.LayoutParams windowManager = alertDialog.getWindow().getAttributes();
            // positions the dialog in bottom|center
            windowManager.gravity = Gravity.BOTTOM;
            windowManager.y = 20;

        }

        return alertDialog;
    }

    private void setContent() {
        mTaskDescription.setText(mTask.getDescription());
        mTaskCreatedDate.setText(mTask.getCreatedDate());

        // tag
        if (mTask.getTaskType() == null) {
            mTaskTag.setVisibility(View.GONE);
            mHeaderTaskTag.setVisibility(View.GONE);
            mTaskTagColour.setVisibility(View.GONE);
        } else {
            mHeaderTaskTag.setVisibility(View.VISIBLE);
            mTaskTag.setVisibility(View.VISIBLE);
            mTaskTag.setText(mTask.getTaskType());
            mTaskTagColour.setVisibility(View.VISIBLE);
            Integer colour = Common.TASK_TAG_COLOURS.get(mTask.getTaskType());
            if (colour != null) {
                mTaskTagColour.setCardBackgroundColor(ResourcesCompat.getColor(getResources(), colour, null));
            } else {
                // colour to show if anything goes wrong
                mTaskTagColour.setCardBackgroundColor(ResourcesCompat.getColor(getResources(), R.color.colorAshHint, null));

            }
            // mTaskTagColour.setColorFilter();
        }

        // category
        if (mTask.getCategory() == null) {
            mTaskCategory.setVisibility(View.GONE);
            mHeaderTaskCategory.setVisibility(View.GONE);
        } else {
            mHeaderTaskCategory.setVisibility(View.VISIBLE);
            mTaskCategory.setVisibility(View.VISIBLE);
            mTaskCategory.setText(mTask.getCategory());
        }

        // additional notes
        if (mTask.getAdditionalNotes() == null) {
            mTaskAdditionalNotes.setVisibility(View.GONE);
            mHeaderTaskAdditional.setVisibility(View.GONE);
        } else {
            mHeaderTaskAdditional.setVisibility(View.VISIBLE);
            mTaskAdditionalNotes.setVisibility(View.VISIBLE);
            mTaskAdditionalNotes.setText(mTask.getAdditionalNotes());
        }

        // task schedule
        if (mTask.getSchedule() == null) {
            mTaskDue.setVisibility(View.GONE);
            mHeaderTaskDue.setVisibility(View.GONE);

        } else {
            mHeaderTaskDue.setVisibility(View.VISIBLE);
            mTaskDue.setVisibility(View.VISIBLE);
            mTaskDue.setText(mTask.getSchedule());
        }
    }

    private void initializeViews(View view) {
        // text views
        mTaskDescription = view.findViewById(R.id.dialog_task_description_description);
        mTaskCategory = view.findViewById(R.id.dialog_task_description_taskCategory);
        mTaskTag = view.findViewById(R.id.dialog_task_description_task_tag);
        mTaskDue = view.findViewById(R.id.dialog_task_description_taskDue);
        mTaskCreatedDate = view.findViewById(R.id.dialog_task_description_created_on);
        mTaskAdditionalNotes = view.findViewById(R.id.dialog_task_description_additional_notes);
        mTaskTagColour = view.findViewById(R.id.dialog_task_description_tag_colour);
        // header views
        mHeaderTaskDue = view.findViewById(R.id.dialog_task_description_header_due);
        mHeaderTaskCategory = view.findViewById(R.id.dialog_task_description_header_category);
        mHeaderTaskAdditional = view.findViewById(R.id.dialog_task_description_header_additional);
        mHeaderTaskTag = view.findViewById(R.id.dialog_task_description_header_tag);

        Button mContinueBtn = view.findViewById(R.id.dialog_task_description_continueBtn);
        Button mDeleteButton = view.findViewById(R.id.dialog_task_description_deleteButton);

        mContinueBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
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

}
