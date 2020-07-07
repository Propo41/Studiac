package com.valhalla.studiac.activities.settings;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.valhalla.studiac.R;
import com.valhalla.studiac.toolbars.NavigationToolbarWhite;

public class AboutActivity extends NavigationToolbarWhite {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContent(R.layout.activity_about);

        Button button = findViewById(R.id.about_contact_us_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent emailIntent = new Intent(Intent.ACTION_SEND);
                emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{getString(R.string.dev_email)});
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Contact us");
                // this will make such that when user returns to your app,
                // your app is displayed, instead of the email app.
                // need this to prompts email client only
                emailIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                emailIntent.setType("message/rfc822");
                startActivity(Intent.createChooser(emailIntent, "Choose an Email client :"));
            }
        });
    }
}