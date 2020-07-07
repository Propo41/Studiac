package com.valhalla.studiac.fragments.dialogs;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.os.CountDownTimer;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.valhalla.studiac.R;

public class CustomProgressDialog {
    private Activity activity;
    private AlertDialog dialog;
    private boolean dialogState;
    private static final int TIMEOUT = 20000;


    public CustomProgressDialog(Activity activity) {
        this.activity = activity;
        dialogState = false;
    }

    @SuppressLint("InflateParams")
    public void startLoadingAnimation() {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        LayoutInflater inflater = activity.getLayoutInflater();
        builder.setView(inflater.inflate(R.layout.custom_progressbar_dialog, null));
        builder.setCancelable(false);
        dialog = builder.create();
        dialog.show();
        dialogState = true;
        startTimer();
    }

    private void startTimer() {

        // if after 20sec the loading is not completed, stop it and show toast
        new CountDownTimer(TIMEOUT, 1000) {
            public void onTick(long millisUntilFinished) {
            }

            public void onFinish() {
                if (dialogState) {
                    stopLoadingAnimation();
                    Toast toast = Toast.makeText(activity, "Connection timeout.", Toast.LENGTH_SHORT);
                    View view = toast.getView();
                    if (view != null) {
                        TextView v = view.findViewById(android.R.id.message);
                        if (v != null) {
                            v.setGravity(Gravity.CENTER);
                        }
                    }
                    toast.show();
                }

            }
        }.start();
    }

    public void stopLoadingAnimation() {
        dialog.dismiss();
        dialogState = false;
    }
}
