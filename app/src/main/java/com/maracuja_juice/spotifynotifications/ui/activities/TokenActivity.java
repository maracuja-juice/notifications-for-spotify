package com.maracuja_juice.spotifynotifications.ui.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.maracuja_juice.spotifynotifications.R;
import com.maracuja_juice.spotifynotifications.api.model.AccessTokenResponse;
import com.maracuja_juice.spotifynotifications.api.service.TokenClient;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

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
        String code = bundle.getString(getString(R.string.intent_authorize_code));

        // TODO I think all of this doesn't belong here and actually I think this activity isn't needed at all!

        String API_BASE_URL = "https://refresh-token-spotify.herokuapp.com/api/";

        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

        Retrofit.Builder builder =
                new Retrofit.Builder()
                        .baseUrl(API_BASE_URL)
                        .addConverterFactory(
                                JacksonConverterFactory.create()
                        );

        Retrofit retrofit =
                builder
                        .client(
                                httpClient.build()
                        )
                        .build();

        TokenClient client = retrofit.create(TokenClient.class);

        Call<AccessTokenResponse> call = client.token(code);
        call.enqueue(new Callback<AccessTokenResponse>() {
            @Override
            public void onResponse(Call<AccessTokenResponse> call, Response<AccessTokenResponse> response) {
                Log.d(LOG_TAG, "network request was successful");
                Log.d(LOG_TAG, response.body().getAccessToken());
                Log.d(LOG_TAG, response.body().getScope());
            }

            @Override
            public void onFailure(Call<AccessTokenResponse> call, Throwable t) {
                Log.d(LOG_TAG, "network request failed");
            }
        });

        // TODO: now I need to call my service here.
        // TODO: Make a retrofit class that has the two methods just like my service.
        // TODO: this should only call the token endpoint: that should only be called once (unless someone reverts the permission )
    }


}
