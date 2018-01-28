package com.maracuja_juice.spotifynotifications.services;

import android.os.AsyncTask;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import kaaes.spotify.webapi.android.SpotifyApi;
import kaaes.spotify.webapi.android.SpotifyCallback;
import kaaes.spotify.webapi.android.SpotifyError;
import kaaes.spotify.webapi.android.SpotifyService;
import kaaes.spotify.webapi.android.models.Album;
import kaaes.spotify.webapi.android.models.Artist;
import kaaes.spotify.webapi.android.models.ArtistsCursorPager;
import kaaes.spotify.webapi.android.models.Pager;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class SpotifyCrawlerTask extends AsyncTask {

    private static final String LOG_TAG = SpotifyCrawlerTask.class.getName();

    private SpotifyService spotify;

    public SpotifyCrawlerTask(String token, int expiresIn) {
        SpotifyApi api = new SpotifyApi();
        api.setAccessToken(token);

        spotify = api.getService();
    }

    public List<Artist> getFollowedArtists() {
        final int maximumArtists = 50;
        final ArrayList<Artist> artists = new ArrayList<>();
        final Map<String, Object> options = new HashMap<>();

        SpotifyCallback callback = new SpotifyCallback() {
            @Override
            public void failure(SpotifyError spotifyError) {
                // TODO: Better handle this.
                Log.e(LOG_TAG, "BIG FAILURE! " + spotifyError.getErrorDetails());
            }

            @Override
            public void success(Object o, Response response) {
                ArtistsCursorPager pager = (ArtistsCursorPager) o;
                artists.addAll(pager.artists.items);

                if(pager.artists.cursors.after != null) {
                    options.put("after", pager.artists.cursors.after);
                    follwedArtistsRequest(options, this);
                }
            }
        };

        options.put(SpotifyService.LIMIT, maximumArtists);
        follwedArtistsRequest(options, callback);
        return artists;
    }


    public List<Album> getAlbumsOfAllArtists(List<Artist> artists) {
        ArrayList<Album> albums = new ArrayList<>();
        for (int i = 0; i < artists.size(); i++) {
            List<Album> artistsAlbums = getAlbumsOfSingleArtist(artists.get(i));
            albums.addAll(artistsAlbums);
        }
        return albums;
    }

    public List<Album> getAlbumsOfSingleArtist(Artist artist) {
        return new ArrayList<>();
    }

    private void albumOfArtistRequest(Map<String, Object> options, final SpotifyCallback callback) {

    }

    private void follwedArtistsRequest(Map<String, Object> options, final SpotifyCallback callback) {
        spotify.getFollowedArtists(options, new SpotifyCallback<ArtistsCursorPager>() {
            @Override
            public void success(ArtistsCursorPager artistsCursorPager, Response response) {
                callback.success(artistsCursorPager, response);
            }

            @Override
            public void failure(SpotifyError error) {
                callback.failure(error);
            }
        });
    }

    @Override
    protected Object doInBackground(Object[] objects) {

        return null;
    }

}
