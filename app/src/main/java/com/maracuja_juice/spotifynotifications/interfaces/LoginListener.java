package com.maracuja_juice.spotifynotifications.interfaces;

import com.maracuja_juice.spotifynotifications.model.LoginResult;

/**
 * Created by Maurice on 24.03.18.
 */

public interface LoginListener {
    void loginFinished(LoginResult result);
}
