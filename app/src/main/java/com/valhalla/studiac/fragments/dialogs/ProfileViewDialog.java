package com.valhalla.studiac.fragments.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.res.Resources;
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

import com.valhalla.studiac.R;
import com.valhalla.studiac.models.Student;
import com.valhalla.studiac.utility.Common;


public class ProfileViewDialog extends AppCompatDialogFragment {


    private String mName;
    private String mCurrentSem;
    private String mDepartment;
    private String mUniversity;
    private View mView;
    private String mImageRes;

    public ProfileViewDialog(Student basicInfo ) {
        mName = basicInfo.getName();
        mCurrentSem = basicInfo.getCurrentSemester() + "";
        mDepartment = basicInfo.getDepartmentName();
        mUniversity = basicInfo.getUniversityName();
        mImageRes = basicInfo.getImageResName();
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_view_profile, null);
        builder.setView(view);

        mView = view;
        setContent();


        onContinueClick(view);

        AlertDialog alertDialog = builder.create();
        // makes background transparent
        Window window = alertDialog.getWindow();
        if (window != null) {
            window.setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            // removes title from dialog.
            alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

            WindowManager.LayoutParams windowManager = window.getAttributes();
            // positions the dialog in bottom|center
            windowManager.gravity = Gravity.BOTTOM;

            window.getAttributes().windowAnimations = R.style.DialogAnimationSliding;
            //window.getAttributes().windowAnimations = R.style.DialogAnimationScale;

        }


        return alertDialog;
    }

    @Override
    public int getTheme() {
        return R.style.Theme_AppCompat_Light_Dialog_Alert;
    }


    private void onContinueClick(View view) {
        Button close = view.findViewById(R.id.view_profile_button);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

    private void setContent() {
        TextView name = mView.findViewById(R.id.view_profile_name_id);
        name.setText(mName);

        TextView currentSem = mView.findViewById(R.id.view_profile_current_semester_id);
        currentSem.setText(mCurrentSem);

        TextView department = mView.findViewById(R.id.view_profile_department);
        department.setText(mDepartment);

        TextView university = mView.findViewById(R.id.view_profile_university_name);
        university.setText(mUniversity);

        ImageView userImage = mView.findViewById(R.id.view_profile_image_id);
        int imgId = getImageDrawableId(mImageRes, requireContext());
        userImage.setImageResource(imgId);

    }

    private int getImageDrawableId(String resName, Context context) {
        Resources resources = context.getResources();
        return resources.getIdentifier(resName, "drawable", Common.PACKAGE_NAME);
    }


}

