package com.maracuja_juice.spotifynotifications;

import android.app.Application;

import com.maracuja_juice.spotifynotifications.data.MyObjectBox;
import com.squareup.leakcanary.LeakCanary;

import net.danlew.android.joda.JodaTimeAndroid;

import io.objectbox.BoxStore;

/**
 * Created by Maurice on 04.03.18.
 */

public class SpotifyNotificationsApplication extends Application {
    private BoxStore boxStore;

    @Override
    public void onCreate() {
        super.onCreate();
        if (LeakCanary.isInAnalyzerProcess(this)) {
            // This process is dedicated to LeakCanary for heap analysis.
            // You should not init your app in this process.
            return;
        }
        LeakCanary.install(this);
        JodaTimeAndroid.init(this);

        boxStore = MyObjectBox.builder().androidContext(SpotifyNotificationsApplication.this).build();
    }

    public BoxStore getBoxStore() {
        return boxStore;
    }
}