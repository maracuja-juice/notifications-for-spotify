package com.maracuja_juice.spotifynotifications.services;

import android.os.AsyncTask;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import kaaes.spotify.webapi.android.SpotifyError;
import kaaes.spotify.webapi.android.SpotifyService;
import kaaes.spotify.webapi.android.models.Album;
import kaaes.spotify.webapi.android.models.Artist;
import kaaes.spotify.webapi.android.models.ArtistsCursorPager;
import kaaes.spotify.webapi.android.models.Pager;
import retrofit.RetrofitError;

/**
 * Created by Maurice on 01.03.18.
 */

public class AlbumCrawler {
    private static final String LOG_TAG = SpotifyCrawlerTask.class.getName();
    private SpotifyService spotify;

    public AlbumCrawler(SpotifyService service) {
        this.spotify = service;
    }

    private Pager<Album> albumsOfArtistRequest(String artistId, Map<String, Object> options) {
        Pager<Album> pager = null;

        try {
            pager = spotify.getArtistAlbums(artistId, options);
        } catch (RetrofitError error) {
            SpotifyError spotifyError = SpotifyError.fromRetrofitError(error);
            Log.e(LOG_TAG, "BIG FAILURE! " + spotifyError.getErrorDetails());
        }

        return pager;
    }

    public List<Album> getAlbumsOfArtist(Artist artist) {
        int maximumArtists = 50;
        ArrayList<Album> albums = new ArrayList<>();

        Map<String, Object> options = new HashMap<>();
        options.put(SpotifyService.LIMIT, maximumArtists);
        options.put(SpotifyService.ALBUM_TYPE, "album,single");

        // TODO: Add MARKET?
        Pager<Album> albumPager = albumsOfArtistRequest(artist.id, options);
        albums.addAll(albumPager.items);

        while(albumPager.next != null) {
            options.put(SpotifyService.OFFSET, albums.size());
            albumPager = albumsOfArtistRequest(artist.id, options);
            albums.addAll(albumPager.items);
        }

        return albums;
    }
}
