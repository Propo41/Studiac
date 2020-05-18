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
        //Calendar calendar = Calendar.getInstance();
     /*   String time;
        if (calendar.get(Calendar.AM_PM) == Calendar.AM) {
            time = "AM";
        } else {
            time = "PM";
        }
        // converting to 12 hr format
        String hours = (hourOfDay > 12) ? hourOfDay - 12 + "" : hourOfDay + "";*/
        // Log.i("picker:::: ", hourOfDay + " : " + minute);

        String time = Common.convertTo24hFormat(hourOfDay + ":" + minute);
        getTargetFragment().onActivityResult(
                getTargetRequestCode(),
                Activity.RESULT_OK,
                new Intent().putExtra("selectedTime", time)
        );

    }
}