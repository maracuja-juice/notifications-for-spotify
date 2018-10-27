package com.maracuja_juice.spotifynotifications;

import android.app.Application;

import com.maracuja_juice.spotifynotifications.model.MyObjectBox;

import net.danlew.android.joda.JodaTimeAndroid;

import io.objectbox.BoxStore;

/**
 * Created by Maurice on 04.03.18.
 */

public class SpotifyNotificationsApplication extends Application {
    private static BoxStore boxStore;

    @Override
    public void onCreate() {
        super.onCreate();
        JodaTimeAndroid.init(this);

        boxStore = MyObjectBox.builder().androidContext(SpotifyNotificationsApplication.this).build();
    }

    public static BoxStore getBoxStore() {
        return boxStore;
    }
}