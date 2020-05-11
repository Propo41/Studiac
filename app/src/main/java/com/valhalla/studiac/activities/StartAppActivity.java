package com.valhalla.studiac.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.valhalla.studiac.R;
import com.valhalla.studiac.fragments.dialogs.SelectCourseDialog;
import com.valhalla.studiac.utility.common.Course;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class StartAppActivity extends AppCompatActivity {

    private final int GREATER = 0;
    private final int LESSER = 1;
    private final int EQUAL = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_startapp);

        int result = compareTimes("12:00AM", "11:30PM");
        if (result == GREATER) {
            Toast.makeText(this, "date1 comes after date2", Toast.LENGTH_SHORT).show();
        } else if (result == LESSER) {
            Toast.makeText(this, "date1 comes before date2", Toast.LENGTH_SHORT).show();
        } else if (result == EQUAL) {
            Toast.makeText(this, "date1 equals  date2", Toast.LENGTH_SHORT).show();
        }
    }


    /*
     *
     */
    public int compareTimes(String time1, String time2) {
        DateFormat dateFormat = new SimpleDateFormat("hh:mma", Locale.US);
        try {
            Date date1 = dateFormat.parse(time1);
            Date date2 = dateFormat.parse(time2);

            if (date1.compareTo(date2) > 0) {
                return GREATER;
            } else if (date1.compareTo(date2) < 0) {
                return LESSER;
            } else {
                return EQUAL;
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }

        return -1;


    }

}
