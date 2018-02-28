package com.maracuja_juice.spotifynotifications.di;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import kaaes.spotify.webapi.android.SpotifyApi;

@Module
public class SpotifyApiModule {
    @Provides
    @Singleton
    SpotifyApi provideSpotifyApi() {
        return new SpotifyApi();
    }
}
