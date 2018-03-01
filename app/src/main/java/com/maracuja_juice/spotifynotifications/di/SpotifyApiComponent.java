package com.maracuja_juice.spotifynotifications.di;

import com.maracuja_juice.spotifynotifications.services.SpotifyCrawlerTask;

import javax.inject.Singleton;
import dagger.Component;

@Singleton
@Component(modules = SpotifyApiModule.class)
public interface SpotifyApiComponent {
    void inject(SpotifyCrawlerTask spotifyCrawlerTask);
}
