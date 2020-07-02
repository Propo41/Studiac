package com.valhalla.studiac.fragments.dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import com.valhalla.studiac.utility.Common;

import java.util.Calendar;

/**
 * returns selected time in 12hr format as (hh:mma) as an intent
 */
public class TimePickerDialog extends DialogFragment implements android.app.TimePickerDialog.OnTimeSetListener {
    private static final String TAG = "TimePickerFragment";

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        // Set the current time as the default date
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        return new android.app.TimePickerDialog(getActivity(), TimePickerDialog.this, hour, minute,
                false);
    }


    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        // converting 24hr to 12hr format
        String time = Common.convertTo12hFormat(hourOfDay + ":" + minute);
        getTargetFragment().onActivityResult(
                getTargetRequestCode(),
                Activity.RESULT_OK,
                new Intent().putExtra("selectedTime", time)
        );
    }
}