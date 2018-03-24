package com.maracuja_juice.spotifynotifications.model;

import android.support.annotation.NonNull;

import org.joda.time.LocalDate;

import java.util.Comparator;
import java.util.List;

import kaaes.spotify.webapi.android.models.Album;
import kaaes.spotify.webapi.android.models.ArtistSimple;

/**
 * Created by Maurice on 24.03.18.
 */

public class MyAlbum implements Comparable<MyAlbum> {
    private Album album;
    private LocalDate releaseDate;

    public MyAlbum(Album album, LocalDate releaseDate) {
        this.album = album;
        this.releaseDate = releaseDate;
    }

    public kaaes.spotify.webapi.android.models.Album getAlbum() {
        return album;
    }

    public void setAlbum(kaaes.spotify.webapi.android.models.Album album) {
        this.album = album;
    }

    public LocalDate getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(LocalDate releaseDate) {
        this.releaseDate = releaseDate;
    }

    @Override
    public int compareTo(@NonNull MyAlbum myAlbum) {
        return myAlbum.getReleaseDate().compareTo(releaseDate);
    }

    public String getImageUrl() {
        return album.images.get(0).url;
    }

    public String getArtistText() {
        List<ArtistSimple> artists = album.artists;

        StringBuilder artistText = new StringBuilder();
        for (int i = 0; i < artists.size(); i++) {
            artistText.append(artists.get(i).name);
            boolean thereAreMoreArtists = artists.size() > i+1;
            if(thereAreMoreArtists)
                artistText.append(", ");
        }

        int maximumLengthDisplay = 80;
        if(artistText.length() > maximumLengthDisplay) {
            artistText.delete(maximumLengthDisplay, artistText.length()-1);
            artistText.append("...");
        }

        return artistText.toString();
    }
}
