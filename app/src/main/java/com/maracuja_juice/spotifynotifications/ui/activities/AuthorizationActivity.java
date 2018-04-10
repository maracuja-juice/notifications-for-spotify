package com.maracuja_juice.spotifynotifications.ui.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.maracuja_juice.spotifynotifications.R;
import com.spotify.sdk.android.authentication.AuthenticationResponse;

// TODO: investigate if I need to split LoginFragment and AuthorizationActivity. Just done like this for the moment.
// TODO: Rename to authentication? What is what again? :)

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
        // fix for: denied starting an intent without a user gesture:
        // https://stackoverflow.com/questions/33048945
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                Log.d(LOG_TAG, "the url: " + url);

                if (url.contains(getString(R.string.redirect_uri))) {
                    Log.d(LOG_TAG, "CONTAINS REDIRECT URI");
                    // TODO: I could make this parsing myself as I only need the code anyway!
                    // TODO: handle errors with switch and response.getType()
                    // TODO: make this into it's own method. bit messy
                    AuthenticationResponse response = AuthenticationResponse.fromUri(Uri.parse(url));
                    String code = response.getCode();
                    startTokenActivity(code);
                    return true;
                }

                return false;
            }
        });
        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl(url);
    }

    // TODO: do I need tokenActivity??
    private void startTokenActivity(String code) {
        Log.d(LOG_TAG, "this is the code: " + code);
        Intent intent = new Intent(this, TokenActivity.class);
        intent.putExtra(getString(R.string.intent_authorize_code), code);
        startActivity(intent);
        finish();
    }

    private String getRequestFromIntent() {
        return getIntent().getStringExtra(getString(R.string.intent_authorization));
    }


}
