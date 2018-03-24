package com.maracuja_juice.spotifynotifications.model;

import android.support.annotation.NonNull;

import org.joda.time.LocalDate;

import java.util.Comparator;

import kaaes.spotify.webapi.android.models.Album;

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
}
