package com.valhalla.studiac.fragments.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.valhalla.studiac.R;



public class UpdateSemesterCalculateGPAPromptDialog extends AppCompatDialogFragment {

    private Button mLaterButton;
    private Button mCalculateButton;
    private UpdateGpaPromptListener updateGpaPromptListener;

    public interface UpdateGpaPromptListener {
        void openBottomSheetGPACalculation();
    }


    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_prompt_calculate_gpa_update_semester, null);
        builder.setView(view);
        initializeViews(view);

        AlertDialog alertDialog = builder.create();
        Window window = alertDialog.getWindow();
        if (window != null) {
            window.setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            // removes title from dialog.
            alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            WindowManager.LayoutParams windowManager = window.getAttributes();
            // positions the dialog in bottom|center
            windowManager.gravity = Gravity.CENTER;
            // window.getAttributes().windowAnimations = R.style.DialogAnimationSliding;
            window.getAttributes().windowAnimations = R.style.DialogBottomAnimationSliding;
        }

        return alertDialog;
    }


    private void initializeViews(View view) {
        mLaterButton = view.findViewById(R.id.dialog_update_semester_calculate_gpa_prompt_later_btn);
        mCalculateButton = view.findViewById(R.id.dialog_update_semester_calculate_gpa_prompt_calculate_btn);
        attachListeners();
    }

    private void attachListeners() {

        mLaterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        mCalculateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateGpaPromptListener.openBottomSheetGPACalculation();
                dismiss();

            }
        });

    }


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            updateGpaPromptListener = (UpdateGpaPromptListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement Dialog Listener");
        }
    }


}
