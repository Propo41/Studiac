package com.valhalla.studiac.activities.settings;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.valhalla.studiac.R;
import com.valhalla.studiac.toolbars.NavigationToolbarWhite;
import com.valhalla.studiac.utility.ErrorStyle;

public class FeedbackActivity extends NavigationToolbarWhite {

    private EditText mInput;
    private ErrorStyle errorStyle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContent(R.layout.activity_feedback);

        errorStyle = ErrorStyle.getInstance(getApplicationContext());
        mInput = findViewById(R.id.feedback_input_id);
        errorStyle.resetError(mInput);
        Button send = findViewById(R.id.feedback_send_button_id);

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isInputValid()) {
                    Intent emailIntent = new Intent(Intent.ACTION_SEND);
                    emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{getString(R.string.dev_email)});
                    emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Bug Report");
                    emailIntent.putExtra(Intent.EXTRA_TEXT, mInput.getText().toString());
                    // this will make such that when user returns to your app,
                    // your app is displayed, instead of the email app.
                    // need this to prompts email client only
                    emailIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    emailIntent.setType("message/rfc822");
                    startActivity(Intent.createChooser(emailIntent, "Choose an Email client :"));
                }
            }
        });
    }


    private boolean isInputValid() {
        String input = mInput.getText().toString();
        if (input.isEmpty()) {
            errorStyle.setError(getString(R.string.Error_EmptyField), mInput);
            return false;
        } else {
            errorStyle.resetError(mInput);
        }

        if (input.length() <= 10) {
            errorStyle.setError(getString(R.string.Error_MoreWord), mInput);
            return false;
        } else {
            errorStyle.resetError(mInput);
        }
        return true;

    }
}
