package com.valhalla.studiac.fragments.dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.DatePicker;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import com.valhalla.studiac.R;
import com.valhalla.studiac.utility.common.Common;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class DatePickerDialog extends DialogFragment implements android.app.DatePickerDialog.OnDateSetListener {
    private Calendar c = Calendar.getInstance();
    private String mTag;
    private final int SEVEN_DAYS_MS = 604800000;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        mTag = getTag();
        // Set the current date as the default date
        Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        // Return a new instance of DatePickerDialog
        android.app.DatePickerDialog dialog = new android.app.DatePickerDialog(
                getActivity(),
                R.style.DialogTheme,
                DatePickerDialog.this,
                year, month, day);
        if (mTag.equals(Common.TODO_TASKS)) {
            dialog.getDatePicker().setMinDate(Calendar.getInstance().getTimeInMillis() + SEVEN_DAYS_MS);
          //  dialog.getDatePicker().setMaxDate(Calendar.getInstance().getTimeInMillis() + 31556952000);

        }
        return dialog;
    }


    // called when a date has been selected
    public void onDateSet(DatePicker view, int year, int month, int day) {
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, month);
        c.set(Calendar.DAY_OF_MONTH, day);
        String selectedDate = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH).format(c.getTime());

        // send date back to the target fragment based on the Request Code that the target fragment provided
        getTargetFragment().onActivityResult(
                getTargetRequestCode(),
                Activity.RESULT_OK,
                new Intent().putExtra("selectedDate", selectedDate)
        );
    }
}