package com.maracuja_juice.spotifynotifications.services;

import android.os.AsyncTask;
import android.util.Log;

import com.maracuja_juice.spotifynotifications.di.DaggerSpotifyApiComponent;
import com.maracuja_juice.spotifynotifications.di.SpotifyApiComponent;
import com.maracuja_juice.spotifynotifications.di.SpotifyApiModule;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import kaaes.spotify.webapi.android.SpotifyApi;
import kaaes.spotify.webapi.android.SpotifyError;
import kaaes.spotify.webapi.android.SpotifyService;
import kaaes.spotify.webapi.android.models.Artist;
import kaaes.spotify.webapi.android.models.ArtistsCursorPager;
import retrofit.RetrofitError;

public class ArtistCrawlerTask extends AsyncTask<Void, Void, List<Artist>> {

    private static final String LOG_TAG = SpotifyCrawlerTask.class.getName();
    private SpotifyService spotify;
    @Inject SpotifyApi api;
    private OnTaskCompleted listener;

    public ArtistCrawlerTask(String token, OnTaskCompleted listener) {
        DaggerSpotifyApiComponent.create().inject(this);

        api.setAccessToken(token);
        spotify = api.getService();
        this.listener = listener;
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

    private List<Artist> getFollowedArtists() {
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

    @Override
    protected List<Artist> doInBackground(Void... voids) {
        return getFollowedArtists();
    }

    @Override
    protected void onPostExecute(List<Artist> s) {
        super.onPostExecute(s);
        listener.onTaskCompleted(s);
    }
}
