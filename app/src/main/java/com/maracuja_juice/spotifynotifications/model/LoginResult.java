package com.maracuja_juice.spotifynotifications.model;

/**
 * Created by Maurice on 24.03.18.
 */

public class LoginResult {
    private String token;
    private int tokenExpirationIn;

    public LoginResult(String token, int tokenExpirationIn) {
        this.token = token;
        this.tokenExpirationIn = tokenExpirationIn;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public int getTokenExpirationIn() {
        return tokenExpirationIn;
    }

    public void setTokenExpirationIn(int tokenExpirationIn) {
        this.tokenExpirationIn = tokenExpirationIn;
    }
}
