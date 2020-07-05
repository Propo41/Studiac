package com.valhalla.studiac.utility;


import android.content.Context;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.widget.EditText;

import androidx.core.content.res.ResourcesCompat;

import com.valhalla.studiac.R;

/**
 * singleton class
 */
public class ErrorStyle {

    private static ErrorStyle instance;
    private final String TAG = "ErrorStyle";
    private Drawable icon;


    public static ErrorStyle getInstance(Context context) {
        if (instance == null) {
            instance = new ErrorStyle(context);
        }
        return instance;
    }

    private ErrorStyle(Context context) {
         icon = context.getResources().getDrawable(R.drawable.ic_error);
    }


    /**
     * sets a stroke of width 4 and an error message with an error icon in the edit text view
     * @param errorMessage the message that will be displayed
     * @param view the editText view
     */
    public void setError(String errorMessage, EditText view) {
        GradientDrawable backgroundGradient = (GradientDrawable) view.getBackground();
        backgroundGradient.setStroke(5, ResourcesCompat.getColor(view.getResources(), R.color.colorInputRequired, null));

        if (icon != null) {
            icon.setBounds(0, 0,
                    icon.getIntrinsicWidth(),
                    icon.getIntrinsicHeight());
        }
        view.setError(errorMessage, icon);
    }

    /**
     * resets the stroke to 0. Call this in else conditions during input validation
     * @param view the edit text view
     */
    public void resetError(EditText view){
        GradientDrawable backgroundGradient = (GradientDrawable) view.getBackground();
        backgroundGradient.setStroke(0, ResourcesCompat.getColor(view.getResources(), R.color.colorInputRequired, null));
    }

}
