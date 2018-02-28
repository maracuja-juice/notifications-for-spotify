package com.maracuja_juice.spotifynotifications;

import android.app.Application;

import com.maracuja_juice.spotifynotifications.di.DaggerSpotifyApiComponent;
import com.maracuja_juice.spotifynotifications.di.SpotifyApiComponent;
import com.maracuja_juice.spotifynotifications.services.ArtistCrawlerTask;

/**
 * Created by Maurice on 28.02.18.
 */

public class SpotifyNotificationApplication extends Application {

    private SpotifyApiComponent mSpotifyApiComponent;

    @Override
    public void onCreate() {
        super.onCreate();

        mSpotifyApiComponent = DaggerSpotifyApiComponent.create();
    }

    public void inject(ArtistCrawlerTask artistCrawlerTask) {
        mSpotifyApiComponent.inject(artistCrawlerTask);
    }
}

