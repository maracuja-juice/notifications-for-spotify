package com.maracuja_juice.spotifynotifications.services;

import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import kaaes.spotify.webapi.android.SpotifyError;
import kaaes.spotify.webapi.android.SpotifyService;
import kaaes.spotify.webapi.android.models.Artist;
import kaaes.spotify.webapi.android.models.ArtistsCursorPager;
import retrofit.RetrofitError;

public class ArtistCrawler {

    private static final String LOG_TAG = SpotifyCrawlerTask.class.getName();
    private SpotifyService spotify;

    public ArtistCrawler(SpotifyService service) {
        spotify = service;
    }

    private ArtistsCursorPager followedArtistsRequest(Map<String, Object> options) {
        ArtistsCursorPager pager = null;

        try {
            pager = spotify.getFollowedArtists(options);
        } catch (RetrofitError error) {
            SpotifyError spotifyError = SpotifyError.fromRetrofitError(error);
            Log.e(LOG_TAG, "BIG FAILURE! " + spotifyError.getErrorDetails());
        }

        return pager;
    }

    public List<Artist> getFollowedArtists() {
        int maximumArtists = 50;
        ArrayList<Artist> artists = new ArrayList<>();
        Map<String, Object> options = new HashMap<>();

        options.put(SpotifyService.LIMIT, maximumArtists);

        ArtistsCursorPager pager = followedArtistsRequest(options);
        artists.addAll(pager.artists.items);

        while(pager.artists.cursors.after != null) {
            options.put("after", pager.artists.cursors.after);
            pager = followedArtistsRequest(options);
            artists.addAll(pager.artists.items);
        }

        return artists;
    }
}
