package com.example.project.activities;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.project.R;

public class StartAppActivity extends AppCompatActivity {
    Button incorrectCretentialPopupButton,setupProfileButton,courseAddedPopupButton,forgotPasswordPopupbutton;
    final Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_startapp);

        incorrectCretentialPopupButton=(Button)findViewById(R.id.button1);
        setupProfileButton =(Button)findViewById(R.id.button2);
        courseAddedPopupButton=(Button)findViewById(R.id.button3);
        forgotPasswordPopupbutton=(Button)findViewById(R.id.button4);

       //mDialog=new Dialog(this);


        incorrectCretentialPopupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {

                        /*final Dialog dialogIncorrectcredential = new Dialog(context);
                        dialog.setContentView(R.layout.dialog_login_incorrect);

                        // set the custom dialog components - text, image and button
                        TextView text =  dialogIncorrectcredential.findViewById(R.id.incorrect_text_id);

                        ImageView image = dialogIncorrectcredential.findViewById(R.id.alert_icon_id);


                        Button dialogIncorrectcredentialButton = dialogIncorrectcredential.findViewById(R.id.alert_button_id);

                        dialogIncorrectcredentialButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialogIncorrectcredential.dismiss();
                            }
                        });

                        dialogIncorrectcredential.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                        dialogIncorrectcredential.show();

                        //dialog (dialog_login_incorrect)end
                        */
                        final Dialog dialogAddroutine = new Dialog(context);
                        dialogAddroutine.setContentView(R.layout.dialog_setupprofile2_addroutine);

                        // set the custom dialog components - text, image and button
                        TextView text =  dialogAddroutine.findViewById(R.id.addroutine_text_id);

                        ImageView image = dialogAddroutine.findViewById(R.id.add_icon_id);


                        Button dialogAddroutineButton = dialogAddroutine.findViewById(R.id.addroutine_button_id);

                        dialogAddroutineButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialogAddroutine.dismiss();
                            }
                        });

                        dialogAddroutine.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                        dialogAddroutine.show();
                        
            }
        });

        //common_ic_profile image change start

        setupProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              //  Intent changeimgIntent = new Intent(StartAppActivity.this,SetupprofileChangimg.class);
              //  startActivity(changeimgIntent);
            }
        });

        //common_ic_profile image change end

        courseAddedPopupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialogCourseadded = new Dialog(context);
                dialogCourseadded.setContentView(R.layout.dialog_setupprofile2_courseadded);


                // set the custom dialog components - text, image and button
                TextView courseaddtext = (TextView) dialogCourseadded.findViewById(R.id.course_added_text_id);

                ImageView clickicon = (ImageView) dialogCourseadded.findViewById(R.id.click_icon_id);


                Button dialogCourseaddedButton = (Button) dialogCourseadded.findViewById(R.id.course_continue_button_id);

                dialogCourseaddedButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialogCourseadded.dismiss();
                    }
                });

                dialogCourseadded.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialogCourseadded.show();
            }
        });

        forgotPasswordPopupbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final  Dialog dialogForgotpasswordAlert = new Dialog(context);
                dialogForgotpasswordAlert.setContentView(R.layout.dialog_forgotpassword_emailsent);

                TextView forgetpasswortext = dialogForgotpasswordAlert.findViewById(R.id.forgotpassword_text_id);

                ImageView lockicon = dialogForgotpasswordAlert.findViewById(R.id.lock_icon_id);
                Button dialogForgotpasswordAlertButton = dialogForgotpasswordAlert.findViewById(R.id.forgotpassword_button_id);
                // laypouyt koi
                dialogForgotpasswordAlertButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialogForgotpasswordAlert.dismiss();
                    }
                });
                dialogForgotpasswordAlert.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialogForgotpasswordAlert.show();
            }
        });



    }
}
