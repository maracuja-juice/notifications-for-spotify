package com.maracuja_juice.spotifynotifications.api.service;

import android.util.Log;

import com.maracuja_juice.spotifynotifications.api.model.AccessTokenResponse;
import com.maracuja_juice.spotifynotifications.api.model.RefreshTokenResponse;
import com.maracuja_juice.spotifynotifications.ui.activities.MainActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TokenService {
    private static final String LOG_TAG = MainActivity.class.getName();

    // TODO: think about implementing a failure method in the two listeners.

    public static void getAccessToken(String code, AuthorizationListener listener) {
        TokenClient client = TokenServiceGenerator.createService();
        Call<AccessTokenResponse> call = client.token(code);
        call.enqueue(new Callback<AccessTokenResponse>() {
            @Override
            public void onResponse(Call<AccessTokenResponse> call, Response<AccessTokenResponse> response) {
                Log.d(LOG_TAG, "network request was successful");

                String accessToken = response.body().getAccessToken();
                String refreshToken = response.body().getRefreshToken();
                int expiresInSeconds = response.body().getExpiresIn();

                listener.authorizationFinished(accessToken, refreshToken, expiresInSeconds);
            }

            @Override
            public void onFailure(Call<AccessTokenResponse> call, Throwable t) {
                Log.e(LOG_TAG, t.getMessage());
                Log.d(LOG_TAG, "network request for FIRST authorization failed");
            }
        });
    }

    public static void getRefreshToken(String refreshToken, RefreshTokenListener listener) {
        TokenClient client = TokenServiceGenerator.createService();
        Call<RefreshTokenResponse> call = client.refreshToken(refreshToken);
        call.enqueue(new Callback<RefreshTokenResponse>() {
            @Override
            public void onResponse(Call<RefreshTokenResponse> call, Response<RefreshTokenResponse> response) {
                Log.d(LOG_TAG, "network request was successful");

                String accessToken = response.body().getAccessToken();
                int expiresInSeconds = response.body().getExpiresIn();

                listener.refreshFinished(accessToken, expiresInSeconds);
            }

            @Override
            public void onFailure(Call<RefreshTokenResponse> call, Throwable t) {
                Log.e(LOG_TAG, t.getMessage());
                Log.d(LOG_TAG, "network request failed for REFRESH TOKEN");
            }
        });
    }
}
