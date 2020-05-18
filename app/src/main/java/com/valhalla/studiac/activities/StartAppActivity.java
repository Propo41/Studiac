package com.valhalla.studiac.activities;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.appcompat.app.AppCompatActivity;

import com.valhalla.studiac.R;

import java.util.ArrayList;

public class StartAppActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_startapp);

        String str = "A";

        ArrayList<String> all = new ArrayList<>();
        all.add(str);

        ArrayList<String> category1 = new ArrayList<>();
        category1.addAll(all);

        print(all, "all");
        print(category1, "category1");

        category1.remove(0);

        print(all, "all");
        print(category1, "category1");

    }

    private void print(ArrayList<String> list, String tag) {
        for (String str : list) {
            Log.i(tag, str);
        }
        System.out.println("**************************");


    }

}
