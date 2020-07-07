package com.valhalla.studiac.fragments.dialogs;

import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.valhalla.studiac.R;

public class CourseAddedPromptDialog extends AppCompatDialogFragment {

    private OnButtonClickListener mListener;

    public interface OnButtonClickListener {
        void onAddMoreClick();

        void onContinueClick();
    }


    @NonNull
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_setupprofile2_prompt_added, null);
        builder.setView(view);

        Button addMoreButton = view.findViewById(R.id.setup_dialog_course_added_add_id);
        Button continueButton = view.findViewById(R.id.setup_dialog_course_added_continue_id);

        addMoreButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                mListener.onAddMoreClick();
            }
        });
        continueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                mListener.onContinueClick();
            }
        });

        AlertDialog alertDialog = builder.create();
        Window window = alertDialog.getWindow();
        if (window != null) {
            window.setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            window.getAttributes().windowAnimations = R.style.DialogAnimationSliding;
            WindowManager.LayoutParams windowManager = alertDialog.getWindow().getAttributes();
            windowManager.gravity = Gravity.CENTER;
        }
        return alertDialog;

    }

    public void setOnButtonClickListener(OnButtonClickListener listener) {
        mListener = listener;
    }


}
