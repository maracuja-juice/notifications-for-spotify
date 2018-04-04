package com.maracuja_juice.spotifynotifications.ui.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.util.Log;

import com.maracuja_juice.spotifynotifications.ui.activities.MainActivity;
import com.maracuja_juice.spotifynotifications.helper.ConfigReader;
import com.maracuja_juice.spotifynotifications.interfaces.LoginListener;
import com.maracuja_juice.spotifynotifications.model.LoginResult;
import com.spotify.sdk.android.authentication.AuthenticationClient;
import com.spotify.sdk.android.authentication.AuthenticationRequest;
import com.spotify.sdk.android.authentication.AuthenticationResponse;

/**
 * Created by Maurice on 24.03.18.
 */

public class LoginFragment extends Fragment {

    private static final String LOG_TAG = MainActivity.class.getName();
    private static final int REQUEST_CODE = 1369;
    private String[] permissionScopes = new String[]{"user-follow-read"};
    private LoginListener loginFinishedListener;
    private Context context;

    public LoginFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
        loginFinishedListener = (LoginListener) context;
    }

    public void startLogin(Activity parentActivity) {
        String clientId = ConfigReader.getConfigValue(context, "clientId");
        String redirectUri = ConfigReader.getConfigValue(context, "redirectUri");

        AuthenticationRequest.Builder builder =
                new AuthenticationRequest.Builder(clientId, AuthenticationResponse.Type.TOKEN, redirectUri);

        builder.setScopes(permissionScopes);
        AuthenticationRequest request = builder.build();

        AuthenticationClient.openLoginActivity(parentActivity, REQUEST_CODE, request);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);

        // Check if result comes from the correct activity
        if (requestCode == REQUEST_CODE) {
            AuthenticationResponse response = AuthenticationClient.getResponse(resultCode, intent);

            switch (response.getType()) {
                // Response was successful and contains auth token
                case TOKEN:
                    Log.d(LOG_TAG, "Successful. " + response.getAccessToken());

                    String token = response.getAccessToken();
                    int expiresIn = response.getExpiresIn();
                    Log.d(LOG_TAG, "Token expires in: " + expiresIn);

                    LoginResult result = new LoginResult(token, expiresIn);
                    loginFinishedListener.loginFinished(result);
                    break;

                // Auth flow returned an error
                case ERROR:
                    Log.e(LOG_TAG, "Auth Flow returned error: " + response.getError());
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
}
