package com.example.project.utility;

import android.graphics.drawable.GradientDrawable;
import android.view.View;

import androidx.core.content.res.ResourcesCompat;

import com.example.project.R;

/*
 * A utility class that can be used across the entire project
 */
public class Common {

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
}
