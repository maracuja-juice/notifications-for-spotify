package com.maracuja_juice.spotifynotifications;

import android.app.Application;

import net.danlew.android.joda.JodaTimeAndroid;

/**
 * Created by Maurice on 04.03.18.
 */

public class SpotifyNotificationsApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        JodaTimeAndroid.init(this);
    }
}