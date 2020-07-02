package com.valhalla.studiac.fragments.dialogs;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.valhalla.studiac.R;
import com.valhalla.studiac.utility.Common;

import java.util.Objects;

public class BulletinDeletePromptDialog extends AppCompatDialogFragment {

    private OnButtonClickListener mListener;

    public interface OnButtonClickListener {
        void onConfirmClick();

        void onCancelClick();
    }


    @SuppressLint("RestrictedApi")
    @Override

    public void setupDialog(@NonNull Dialog dialog, int style) {
        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        super.setupDialog(dialog, style);

    }

    @NonNull
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_bulletin_board_delete_prompt, null);
        builder.setView(view);

        Button deleteConfirmButton = view.findViewById(R.id.dialog_delete_prompt_yes_button_id);
        Button deleteCancelButton = view.findViewById(R.id.dialog_delete_account_no_button_id);

        AlertDialog alertDialog = builder.create();
        Objects.requireNonNull(alertDialog.getWindow()).setLayout(Common.getEquivalentPx(300), ConstraintLayout.LayoutParams.WRAP_CONTENT);
        alertDialog.getWindow().setGravity(Gravity.CENTER);


        deleteConfirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onConfirmClick();
            }
        });
        deleteCancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onCancelClick();
            }
        });


        return alertDialog;

    }

    public void setOnButtonClickListener(OnButtonClickListener listener) {
        mListener = listener;
    }

    public void onResume() {
        super.onResume();
        getDialog().getWindow().setLayout(Common.getEquivalentPx(300), ConstraintLayout.LayoutParams.WRAP_CONTENT);
        getDialog().getWindow().setGravity(Gravity.CENTER);

    }
}
