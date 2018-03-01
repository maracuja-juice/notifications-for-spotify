package com.maracuja_juice.spotifynotifications.services;

import android.os.AsyncTask;
import android.util.Log;

import com.maracuja_juice.spotifynotifications.di.DaggerSpotifyApiComponent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

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

public class SpotifyCrawlerTask extends AsyncTask<Void, Void, List<Album>> {

    private static final String LOG_TAG = SpotifyCrawlerTask.class.getName();

    private SpotifyService spotify;
    @Inject SpotifyApi api;
    private OnTaskCompleted listener;

    private ArtistCrawler artistCrawler;
    private AlbumCrawler albumCrawler;

    public SpotifyCrawlerTask(String token, OnTaskCompleted listener) {
        DaggerSpotifyApiComponent.create().inject(this);

        api.setAccessToken(token);
        spotify = api.getService();
        artistCrawler = new ArtistCrawler(spotify);
        albumCrawler = new AlbumCrawler(spotify);

        this.listener = listener;
    }

    public List<Artist> getFollowedArtists() {
        return artistCrawler.getFollowedArtists();
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
        return albumCrawler.getAlbumsOfArtist(artist);
    }

    @Override
    protected List<Album> doInBackground(Void... voids) {
        List<Artist> artists = getFollowedArtists();
        List<Album> albums = getAlbumsOfAllArtists(artists);
        return albums;
    }

    @Override
    protected void onPostExecute(List<Album> s) {
        super.onPostExecute(s);
        listener.onTaskCompleted(s);
    }
}
