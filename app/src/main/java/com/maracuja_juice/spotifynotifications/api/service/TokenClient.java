package com.maracuja_juice.spotifynotifications.api.service;

import com.maracuja_juice.spotifynotifications.api.model.AccessTokenResponse;
import com.maracuja_juice.spotifynotifications.api.model.RefreshTokenResponse;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface TokenClient {

    @FormUrlEncoded
    @POST("token")
    Call<AccessTokenResponse> token(
            @Field("code") String code
    );

    @FormUrlEncoded
    @POST("refresh_token")
    Call<RefreshTokenResponse> refreshToken(
            @Field("refresh_token") String refreshToken
    );
}
