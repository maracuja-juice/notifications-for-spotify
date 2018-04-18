package com.maracuja_juice.spotifynotifications.ui.fragments;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.util.Log;

import com.maracuja_juice.spotifynotifications.R;
import com.maracuja_juice.spotifynotifications.api.service.AuthorizationListener;
import com.maracuja_juice.spotifynotifications.helper.ConfigReader;
import com.maracuja_juice.spotifynotifications.ui.activities.AuthorizationActivity;
import com.maracuja_juice.spotifynotifications.ui.activities.MainActivity;
import com.spotify.sdk.android.authentication.AuthenticationRequest;
import com.spotify.sdk.android.authentication.AuthenticationResponse;

import static android.app.Activity.RESULT_OK;

/**
 * Created by Maurice on 24.03.18.
 */

public class AuthorizationFragment extends Fragment {

    private static final String LOG_TAG = MainActivity.class.getName();
    private static final int REQUEST_CODE = 1369;
    private String[] permissionScopes = new String[]{"user-follow-read"};
    private AuthorizationListener authorizationListener;
    private Context context;

    public AuthorizationFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
        authorizationListener = (AuthorizationListener) context;
    }

    public void startLogin() {
        String authorizationUri = getRequestUri();

        Intent intent = new Intent(getActivity(), AuthorizationActivity.class);
        intent.putExtra(getString(R.string.intent_authorization), authorizationUri);
        startActivity(intent);
    }

    private String getRequestUri() {
        String clientId = ConfigReader.getConfigValue(context, "clientId");
        String redirectUri = getString(R.string.redirect_uri);

        AuthenticationRequest.Builder builder =
                new AuthenticationRequest.Builder(clientId, AuthenticationResponse.Type.CODE, redirectUri);
        builder.setScopes(permissionScopes);
        AuthenticationRequest request = builder.build();
        return request.toUri().toString(); // TODO: make my own implementation of building the authenticationRequest
    }

    // TODO: What do I do with this? -> remove?
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);

        if (resultCode == RESULT_OK) {
            Log.d(LOG_TAG, "Successful. ");

            String accessToken = intent.getStringExtra(getString(R.string.EXTRA_ACCESS_CODE));
            // TODO then call the service class
        }
        // TODO error case...

    }
}
