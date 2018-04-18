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

// TODO: investigate if I need to split AuthorizationFragment and AuthorizationActivity. Just done like this for the moment.
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
                    AuthenticationResponse response = AuthenticationResponse.fromUri(Uri.parse(url));

                    String code = response.getCode();
                    String extraName = getString(R.string.EXTRA_ACCESS_CODE);
                    Intent intent = new Intent().putExtra(extraName, code);
                    setResult(RESULT_OK, intent);
                    finish();
                    return true;
                }

                return false;
            }
        });
        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl(url);
    }

    private String getRequestFromIntent() {
        return getIntent().getStringExtra(getString(R.string.intent_authorization));
    }


}
