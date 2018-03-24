package com.maracuja_juice.spotifynotifications;

import android.app.Application;

import com.squareup.leakcanary.LeakCanary;

import net.danlew.android.joda.JodaTimeAndroid;

/**
 * Created by Maurice on 04.03.18.
 */

public class SpotifyNotificationsApplication extends Application {
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
    }
}