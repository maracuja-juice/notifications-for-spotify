package com.maracuja_juice.spotifynotifications.com.maracuja_juice.spotifynotifications.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.maracuja_juice.spotifynotifications.helper.ConfigReader;
import com.spotify.sdk.android.authentication.AuthenticationClient;
import com.spotify.sdk.android.authentication.AuthenticationRequest;
import com.spotify.sdk.android.authentication.AuthenticationResponse;

public class LoginActivity extends AppCompatActivity {

    private static final String LOG_TAG = LoginActivity.class.getName();

    // Request code will be used to verify if result comes from the login activity. Can be set to any integer.
    private static final int REQUEST_CODE = 1369;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String clientId = ConfigReader.getConfigValue(this, "clientId");
        String redirectUri = ConfigReader.getConfigValue(this, "redirectUri");

        AuthenticationRequest.Builder builder =
                new AuthenticationRequest.Builder(clientId, AuthenticationResponse.Type.TOKEN, redirectUri);

        builder.setScopes(new String[]{"user-follow-read"});
        AuthenticationRequest request = builder.build();

        AuthenticationClient.openLoginActivity(this, REQUEST_CODE, request);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);

        // Check if result comes from the correct activity
        if (requestCode == REQUEST_CODE) {
            AuthenticationResponse response = AuthenticationClient.getResponse(resultCode, intent);

            switch (response.getType()) {
                // Response was successful and contains auth token
                case TOKEN:
                    // Handle successful response
                    Log.d(LOG_TAG, "Successful. " + response.getAccessToken());
                    returnToMainActivity(response);
                    break;

                // Auth flow returned an error
                case ERROR:
                    Log.e(LOG_TAG, "ERROR! " + response.getError());
                    // Handle error response
                    // TODO POPUP or something.
                    break;

                // Most likely auth flow was cancelled
                default:
                    // Handle other cases
                    Log.d(LOG_TAG, "No Idea what happened lol. " + response.getType() + " " + response.getError());
                    //TODO POPUP or something.
            }
        }
    }

    private void returnToMainActivity(AuthenticationResponse response) {
        Intent mainActivityIntent = new Intent();
        mainActivityIntent.putExtra("token", response.getAccessToken());
        setResult(RESULT_OK, mainActivityIntent);
        finish();
    }
}
