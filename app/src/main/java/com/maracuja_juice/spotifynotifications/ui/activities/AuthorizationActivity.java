package com.maracuja_juice.spotifynotifications.ui.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebView;

import com.maracuja_juice.spotifynotifications.R;

// TODO: investigate if I need to split LoginFragment and AuthorizationActivity. Just done like this for the moment.

public class AuthorizationActivity extends AppCompatActivity {

    private static final String LOG_TAG = MainActivity.class.getName();

    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authorization);

        String url = getRequestFromIntent();
        Log.d(LOG_TAG, url);

        webView = findViewById(R.id.authorizationWebView);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl(url);
        // TODO: how to handle success, close etc?!!
    }

    private String getRequestFromIntent() {
        return getIntent().getStringExtra(getString(R.string.intent_authorization));
    }


}
