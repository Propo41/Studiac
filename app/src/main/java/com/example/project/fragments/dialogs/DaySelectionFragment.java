package com.example.project.fragments.dialogs;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.example.project.R;
import com.example.project.utility.common.Common;

public class DaySelectionFragment extends AppCompatDialogFragment {
    private AlertDialog mAlertDialog;
    private RadioButton mRadioButton;

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
        final View view = inflater.inflate(R.layout.dialog_common_select_day, null);
        builder.setView(view);
        mAlertDialog = builder.create();
        final RadioGroup radioGroup = view.findViewById(R.id.dialog_day_radio_group_id);

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                onRadioButtonClick(group, checkedId);
            }
        });

        Button button = view.findViewById(R.id.dialog_day_picker_button_id);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onButtonClick(view, radioGroup);
            }
        });

        return mAlertDialog;
    }

    private void onRadioButtonClick(RadioGroup group, int checkedId) {
        mRadioButton = group.findViewById(checkedId);
       // Toast.makeText(getContext(), mRadioButton.getText().toString(), Toast.LENGTH_SHORT).show();
    }

    private void onButtonClick(View view, RadioGroup radioGroup){
       int radioId = radioGroup.getCheckedRadioButtonId();
        mRadioButton = view.findViewById(radioId);
        //Toast.makeText(getContext(), mRadioButton.getText().toString(), Toast.LENGTH_SHORT).show();
        getTargetFragment().onActivityResult(
                getTargetRequestCode(),
                Activity.RESULT_OK,
                new Intent().putExtra("selectedDay", mRadioButton.getText().toString())
        );
        mAlertDialog.dismiss();
    }


    @Override
    public void onResume() {
        super.onResume();
        getDialog().getWindow().setLayout(800, 1200);
    }


}
