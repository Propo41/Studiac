package com.valhalla.studiac.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.valhalla.studiac.R;
import com.valhalla.studiac.utility.common.Common;

public class ForgotPasswordActivity extends AppCompatActivity {

    private EditText mEmailField;
    final Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgotpassword);
        Common.addStroke(mEmailField, 0);
        initViews();
    }

    private void initViews(){
        mEmailField =  findViewById(R.id.setup_email_id);
    }


    public void onSignInClick(View v)
    {
        startActivity(new Intent(ForgotPasswordActivity.this, LoginActivity.class));
    }

    public void onConfirmClick(View v)
    {
        if(isInputValid()){
            openDialog();
        }else{
            // TODO: show error popup with border when email not valid or no input provided
             Common.addStroke(mEmailField, 5);
             mEmailField.setError("");
        }
    }

    private void openDialog() {
        final Dialog dialogForgotPasswordEmailSent = new Dialog(context);
        dialogForgotPasswordEmailSent.setContentView(R.layout.dialog_forgotpassword_emailsent);
        Button dialogForgotPasswordEmailSentButton = dialogForgotPasswordEmailSent.findViewById(R.id.forgotpassword_button_id);
        dialogForgotPasswordEmailSentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v0) {
                dialogForgotPasswordEmailSent.dismiss();
                startActivity(new Intent(ForgotPasswordActivity.this, LoginActivity.class));
            }
        });
        dialogForgotPasswordEmailSent.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialogForgotPasswordEmailSent.show();
    }

    public boolean isInputValid() {
        boolean mReturnValue = true;
        if(mEmailField.getText().length() == 0) {
            mEmailField.setError("Field cannot be empty!");
            mReturnValue = false;
        }

        return  mReturnValue;

    }


}
