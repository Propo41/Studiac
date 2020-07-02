package com.valhalla.studiac.fragments.dialogs;

import android.annotation.SuppressLint;
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
import androidx.constraintlayout.widget.ConstraintLayout;

import com.valhalla.studiac.R;
import com.valhalla.studiac.utility.Common;

import java.util.Objects;

public class DeleteAccountPromptDialog extends AppCompatDialogFragment {

    private OnButtonClickListener mListener;

    public interface OnButtonClickListener {
        void onYesClick();

        void onNoClick();
    }


    @NonNull
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_prompt_delete_account, null);
        builder.setView(view);

        Button deleteConfirmButton = view.findViewById(R.id.dialog_delete_account_yes_button_id);
        Button deleteCancelButton = view.findViewById(R.id.dialog_delete_account_no_button_id);

        deleteConfirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onYesClick();
            }
        });
        deleteCancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onNoClick();
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
