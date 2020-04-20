package com.example.project.utility.common;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.GradientDrawable;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import androidx.appcompat.app.AlertDialog;
import androidx.core.content.res.ResourcesCompat;

import com.example.project.R;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;

/*
 * A utility class that can be used across the entire project
 */
public class Common {


    private static Student sStudent;


    public static Student getStudent() {
        return sStudent;
    }

    public static void setStudent(Student student) {
        sStudent = student;
    }

    /*
     * parameters: view - the view on which the stroke is to be added
     *             width - the amount of width of the stroke. Typically: 5
     * typical usage: EditText editText = findViewById(....);
     *                Common.addStroke(editText, 5);
     */
    public static void addStroke(View view, int width) {

        GradientDrawable backgroundGradient = (GradientDrawable) view.getBackground();
        backgroundGradient.setStroke(width, ResourcesCompat.getColor(view.getResources(), R.color.colorInputRequired, null));
    }


    public static void showExceptionPrompt(Context context, String exceptionText){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(exceptionText);
        builder.setCancelable(true);

        builder.setPositiveButton(
                "okay",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }


    public static void setBottomSheetDialogState(Dialog dialog, final String expanded) {

        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                BottomSheetDialog d = (BottomSheetDialog) dialog;
                View bottomSheetInternal = d.findViewById(com.google.android.material.R.id.design_bottom_sheet);
                if(expanded.equals("EXPANDED")){
                    BottomSheetBehavior.from(bottomSheetInternal).setState(BottomSheetBehavior.STATE_EXPANDED);
                }
            }
        });

    }
}
