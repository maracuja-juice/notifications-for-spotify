package com.maracuja_juice.spotifynotifications.api.service;

/**
 * Created by Maurice on 24.03.18.
 */

public interface AuthorizationListener {
    void authorizationFinished(String accessToken, String refreshToken, int secondsToExpiration);
}
