package com.maracuja_juice.spotifynotifications.di;

import com.maracuja_juice.spotifynotifications.services.ArtistCrawlerTask;

import javax.inject.Singleton;

import dagger.Component;


@Singleton
@Component(modules = SpotifyApiModule.class)
public interface SpotifyApiComponent {
    void inject(ArtistCrawlerTask artistCrawlerTask);
}
