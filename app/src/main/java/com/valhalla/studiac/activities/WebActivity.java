package com.valhalla.studiac.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;

import com.valhalla.studiac.R;
import com.valhalla.studiac.toolbars.NavigationToolbarWhite;

public class WebActivity extends NavigationToolbarWhite {

    private WebView mWebView;

    @SuppressLint("NewApi")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String url = null;
        Intent intent = getIntent();
        if (intent != null) {
            Bundle bundle = intent.getExtras();
            if (bundle != null) {
                url = bundle.getString("url");
            }
        }

        ActionBar actionBar = getSupportActionBar();

        if (actionBar != null && url != null) {
            switch (url) {
                case "terms":
                    url = "https://studiac-terms.web.app/";
                    actionBar.setTitle("Terms & Conditions");
                    break;
                case "policy":
                    url = "https://studiac-policy.web.app/";
                    actionBar.setTitle("Privacy Policy");
                    break;
                case "support":
                    url = "https://studiac-2020.web.app/";
                    actionBar.setTitle("Help & Support");
                    break;
            }
        }
        super.setContent(R.layout.activity_web);
        super.startLoadingAnimation();

        mWebView = findViewById(R.id.webView);
        mWebView.getSettings().setLoadWithOverviewMode(true);
        mWebView.getSettings().setUseWideViewPort(true);
        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                WebActivity.super.startLoadingAnimation();
                view.loadUrl(url);
                return true;
            }

            @Override
            public void onPageFinished(WebView view, final String url) {
                WebActivity.super.stopLoadingAnimation();
            }
        });


        if (url == null) {
            Toast.makeText(this, "Problem Loading Page...", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            mWebView.loadUrl(url);
        }

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            if (keyCode == KeyEvent.KEYCODE_BACK) {
                if (mWebView.canGoBack()) {
                    mWebView.goBack();
                } else {
                    finish();
                }
                return true;
            }

        }
        return super.onKeyDown(keyCode, event);
    }


}
