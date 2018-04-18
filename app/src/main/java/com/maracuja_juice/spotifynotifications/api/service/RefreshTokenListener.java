package com.maracuja_juice.spotifynotifications.api.service;

public interface RefreshTokenListener {
    void refreshFinished(String accessToken, int secondsToExpiration);
}
