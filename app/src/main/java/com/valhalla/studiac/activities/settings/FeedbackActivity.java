package com.valhalla.studiac.activities.settings;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.valhalla.studiac.R;
import com.valhalla.studiac.toolbars.NavigationToolbarWhite;
import com.valhalla.studiac.utility.Common;

public class FeedbackActivity extends NavigationToolbarWhite {

    private EditText mInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContent(R.layout.activity_feedback);

        mInput = findViewById(R.id.feedback_input_id);
        Button send = findViewById(R.id.feedback_send_button_id);

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isInputValid()) {
                    Intent emailIntent = new Intent(Intent.ACTION_SEND);
                    emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{"aliahnaf327@gmail.com"});
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
            mInput.setError("Field Cannot Be Empty");
            Common.addStroke(mInput, 5);
            return false;

        } else {
            mInput.setError(null);
            Common.addStroke(mInput, 0);
        }

        if (input.length() <= 10) {
            mInput.setError("Some More Words Would Help!");
            Common.addStroke(mInput, 5);
            return false;
        } else {
            mInput.setError(null);
            Common.addStroke(mInput, 0);
        }

        return true;

    }
}
