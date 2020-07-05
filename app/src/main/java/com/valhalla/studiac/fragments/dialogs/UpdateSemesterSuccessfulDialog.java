package com.valhalla.studiac.fragments.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.valhalla.studiac.R;

public class UpdateSemesterSuccessfulDialog extends AppCompatDialogFragment {

    private Button mCloseButton;
    private Double gpa;


    public UpdateSemesterSuccessfulDialog(Double gpa) {
        this.gpa = gpa;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();

        View view = inflater.inflate(R.layout.dialog_update_semester_successful, null);
        builder.setView(view);
        initializeViews(view);

        AlertDialog alertDialog = builder.create();
        Window window = alertDialog.getWindow();
        if (window != null) {
            window.setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            // removes title from dialog.
            alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            WindowManager.LayoutParams windowManager = window.getAttributes();
            windowManager.gravity = Gravity.CENTER;
            window.getAttributes().windowAnimations = R.style.DialogBottomAnimationSliding;
        }

        return alertDialog;
    }


    private void initializeViews(View view) {
        mCloseButton = view.findViewById(R.id.dialog_update_semester_successful_close_btn);
        TextView mSuccessText = view.findViewById(R.id.dialog_update_semester_successful_text);
        mSuccessText.setText(getString(R.string.Placeholder_congratsSemesterPassed, gpa + ""));
        attachListeners();
    }


    private void attachListeners() {

        mCloseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

    }

}
