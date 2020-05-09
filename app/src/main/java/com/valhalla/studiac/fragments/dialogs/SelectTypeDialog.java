package com.valhalla.studiac.fragments.dialogs;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.valhalla.studiac.R;
import com.valhalla.studiac.utility.common.Common;


public class SelectTypeDialog extends AppCompatDialogFragment implements View.OnClickListener, RadioGroup.OnCheckedChangeListener {

    private RadioGroup mRadioGroup;
    private AlertDialog mAlertDialog;
    private Button button;
    private RadioButton mRadioButton;
    private View mView;

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
        View view = inflater.inflate(R.layout.dialog_todo_select_type, null);
        builder.setView(view);

        mRadioButton = view.findViewById(R.id.radioButtonOne);
        mRadioButton.setChecked(true);

        mAlertDialog = builder.create();
        mView = view;

        mRadioGroup = view.findViewById(R.id.radioGroup);
        button = view.findViewById(R.id.selectButton);

        mRadioGroup.setOnCheckedChangeListener(this);
        button.setOnClickListener(this);
        return mAlertDialog;
    }

    @Override
    public void onResume() {
        super.onResume();
        getDialog().getWindow().setLayout(Common.getEquivalentPx(300), ConstraintLayout.LayoutParams.WRAP_CONTENT);
        getDialog().getWindow().setGravity(Gravity.CENTER);

    }


    @Override
    public void onClick(View v) {
        if (v.getId() == button.getId()) {
            try {
                getTargetFragment().onActivityResult(
                        getTargetRequestCode(),
                        Activity.RESULT_OK,
                        new Intent().putExtra("selectedType", mRadioButton.getText().toString())
                );
                dismiss();

            } catch (Exception ex) {
                Toast.makeText(getContext(), "Must Select An item", Toast.LENGTH_SHORT).show();
            }

        }
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {
            case R.id.radioButtonOne:
                mRadioButton = mView.findViewById(R.id.radioButtonOne);
                break;
            case R.id.radioButtonTwo:
                //Final
                mRadioButton = mView.findViewById(R.id.radioButtonTwo);
                break;
            case R.id.radioButtonThree:
                // Lab Quiz
                mRadioButton = mView.findViewById(R.id.radioButtonThree);
                break;
            case R.id.radioButtonFour:
                //Lab Assignment
                mRadioButton = mView.findViewById(R.id.radioButtonFour);
                break;
            case R.id.radioButtonfive:
                // Presentation
                mRadioButton = mView.findViewById(R.id.radioButtonfive);
                break;
            case R.id.radioButtonSix:
                // Case Work
                mRadioButton = mView.findViewById(R.id.radioButtonSix);
                break;
            case R.id.radioButtonSeven:
                //Project
                mRadioButton = mView.findViewById(R.id.radioButtonSeven);
                break;
        }

    }

}
