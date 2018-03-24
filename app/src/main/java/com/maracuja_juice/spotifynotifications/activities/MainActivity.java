package com.maracuja_juice.spotifynotifications.activities;

import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.maracuja_juice.spotifynotifications.R;
import com.maracuja_juice.spotifynotifications.adapters.AlbumListAdapter;
import com.maracuja_juice.spotifynotifications.fragments.ProgressBarFragment;
import com.maracuja_juice.spotifynotifications.helper.ConfigReader;
import com.maracuja_juice.spotifynotifications.model.MyAlbum;
import com.maracuja_juice.spotifynotifications.services.OnTaskCompleted;
import com.maracuja_juice.spotifynotifications.services.SpotifyCrawlerTask;
import com.spotify.sdk.android.authentication.AuthenticationClient;
import com.spotify.sdk.android.authentication.AuthenticationRequest;
import com.spotify.sdk.android.authentication.AuthenticationResponse;

import java.util.List;

public class MainActivity extends AppCompatActivity implements OnTaskCompleted {

    private static final String LOG_TAG = MainActivity.class.getName();
    private static final int REQUEST_CODE = 1369;
    private boolean isLoggedIn = false; // TODO: save this value?!!! -> sharedpreferences?
    private String token;
    private int expiresIn;
    private String[] permissionScopes = new String[]{"user-follow-read"};

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    private ProgressBarFragment progressBarFragment;

    // TODO: add filter button

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FragmentManager fragmentManager = getSupportFragmentManager();
        progressBarFragment = (ProgressBarFragment) fragmentManager.findFragmentById(R.id.fragmentProgressBar);

        if (!isLoggedIn) {
            startLogin();
        } else {
            startSpotifyCrawlerTask();
        }
    }

    private void startSpotifyCrawlerTask() {
        progressBarFragment.showProgressBar();
        new SpotifyCrawlerTask(token, this).execute();
    }

    @Override
    public void onTaskCompleted(Object result) {
        progressBarFragment.hideProgressBar();

        List<MyAlbum> albums = (List<MyAlbum>) result;

        recyclerView = findViewById(R.id.albumListView);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        adapter = new AlbumListAdapter(this, albums);
        recyclerView.setAdapter(adapter);

    }

    // TODO Login Fragment?!!!
    private void startLogin() {
        String clientId = ConfigReader.getConfigValue(this, "clientId");
        String redirectUri = ConfigReader.getConfigValue(this, "redirectUri");

        AuthenticationRequest.Builder builder =
                new AuthenticationRequest.Builder(clientId, AuthenticationResponse.Type.TOKEN, redirectUri);

        builder.setScopes(permissionScopes);
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
                    Log.d(LOG_TAG, "Successful. " + response.getAccessToken());
                    isLoggedIn = true;
                    token = response.getAccessToken();
                    expiresIn = response.getExpiresIn();
                    Log.d(LOG_TAG, "Token expires in: " + expiresIn);
                    startSpotifyCrawlerTask();
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
