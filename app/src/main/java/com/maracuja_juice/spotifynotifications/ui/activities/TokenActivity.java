package com.maracuja_juice.spotifynotifications.ui.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.maracuja_juice.spotifynotifications.R;

public class TokenActivity extends AppCompatActivity {

    private static final String LOG_TAG = MainActivity.class.getName();

    // TODO: decide if this should be an activity!

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_token);

        Log.d(LOG_TAG, "now starting token activity");
        Bundle bundle = getIntent().getExtras();
        Log.d(LOG_TAG, bundle.getString(getString(R.string.intent_authorize_code)));

        // TODO: now I need to call my service here.
        // TODO: Make a retrofit class that has the two methods just like my service.
        // TODO: this should only call the token endpoint: that should only be called once (unless someone reverts the permission )
    }


}
