package com.valhalla.studiac.fragments.dialogs;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
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
import com.valhalla.studiac.activities.EditWeightActivity;
import com.valhalla.studiac.models.Course;
import com.valhalla.studiac.models.Result;
import com.valhalla.studiac.models.Weight;

import java.util.ArrayList;

public class EditWeightUpdatePromptDialog extends AppCompatDialogFragment {

    private Button mYesButton;
    private Button mNoButton;
    private ArrayList<Weight> mWeightList;
    private ArrayList<Result> mResultList;
    private ArrayList<Course> mCourseList;

    public EditWeightUpdatePromptDialog(ArrayList<Weight> mWeightList,
                                        ArrayList<Result> mResultList, ArrayList<Course> mCourseList) {
        this.mWeightList = mWeightList;
        this.mResultList = mResultList;
        this.mCourseList = mCourseList;
    }


    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_edit_weight_confirm_prompt, null);
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
        mYesButton = view.findViewById(R.id.dialog_edit_weight_warning_prompt_yes_button);
        mNoButton = view.findViewById(R.id.dialog_edit_weight_warning_prompt_no_button);
        attachListeners();
    }

    private void attachListeners() {
        mYesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), EditWeightActivity.class);
                startActivity(intent);
            }

        });

        mNoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CalculateGPABottomSheet bottomSheet = new CalculateGPABottomSheet(
                        mResultList.get(mResultList.size() - 1),
                        mCourseList, mWeightList, mResultList);
                bottomSheet.show(requireActivity().getSupportFragmentManager(), "exampleBottomSheet");
                dismiss();
            }
        });
    }


}
