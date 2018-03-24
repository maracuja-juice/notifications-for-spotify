package com.maracuja_juice.spotifynotifications.services;

import android.os.AsyncTask;
import android.os.Build;

import com.annimon.stream.Stream;
import com.maracuja_juice.spotifynotifications.di.DaggerSpotifyApiComponent;
import com.maracuja_juice.spotifynotifications.helper.ReleaseDateParser;
import com.maracuja_juice.spotifynotifications.model.MyAlbum;

import org.joda.time.LocalDate;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import kaaes.spotify.webapi.android.SpotifyApi;
import kaaes.spotify.webapi.android.SpotifyService;
import kaaes.spotify.webapi.android.models.Album;
import kaaes.spotify.webapi.android.models.Artist;

public class SpotifyCrawlerTask extends AsyncTask<Void, Void, List<MyAlbum>> {

    private static final String LOG_TAG = SpotifyCrawlerTask.class.getName();
    @Inject
    SpotifyApi api;
    private SpotifyService spotify;
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

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            artists.parallelStream().forEach(artist -> {
                albums.addAll(getAlbumsOfSingleArtist(artist));
            });
        } else {
            // backwards compatible streams api via library
            Stream.of(artists).forEach((artist) -> {
                albums.addAll(getAlbumsOfSingleArtist(artist));
            });
        }
        return albums;
    }

    public List<Album> getAlbumsOfSingleArtist(Artist artist) {
        return albumCrawler.getAlbumsOfArtist(artist);
    }

    private List<MyAlbum> convertAlbumsToMyAlbums(List<Album> albums) {
        ArrayList<MyAlbum> myAlbums = new ArrayList<>();
        Stream.of(albums).forEach(album -> {
            LocalDate releaseDate = ReleaseDateParser.parseReleaseDate(
                    album.release_date,
                    album.release_date_precision);
            MyAlbum myAlbum = new MyAlbum(album, releaseDate);
            myAlbums.add(myAlbum);
        });
        return myAlbums;
    }

    @Override
    protected List<MyAlbum> doInBackground(Void... voids) {
        List<Artist> artists = getFollowedArtists();
        List<Album> albums = getAlbumsOfAllArtists(artists);
        List<MyAlbum> myAlbums = convertAlbumsToMyAlbums(albums);
        return myAlbums;
    }

    @Override
    protected void onPostExecute(List<MyAlbum> s) {
        super.onPostExecute(s);
        listener.onTaskCompleted(s);
    }
}
