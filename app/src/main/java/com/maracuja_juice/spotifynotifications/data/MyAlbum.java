package com.maracuja_juice.spotifynotifications.data;

import android.support.annotation.NonNull;

import com.maracuja_juice.spotifynotifications.data.converter.AlbumConverter;
import com.maracuja_juice.spotifynotifications.data.converter.LocalDateConverter;

import org.joda.time.LocalDate;

import java.util.List;

import io.objectbox.annotation.Convert;
import io.objectbox.annotation.Entity;
import io.objectbox.annotation.Id;
import io.objectbox.annotation.NameInDb;
import kaaes.spotify.webapi.android.models.Album;
import kaaes.spotify.webapi.android.models.ArtistSimple;

/**
 * Created by Maurice on 24.03.18.
 */

@Entity
public class MyAlbum implements Comparable<MyAlbum> {
    @Id
    private Long id;

    @NameInDb("ALBUM")
    @Convert(converter = AlbumConverter.class, dbType = String.class)
    private Album album;
    @NameInDb("RELEASEDATE")
    @Convert(converter = LocalDateConverter.class, dbType = String.class)
    private LocalDate releaseDate;

    public MyAlbum(Long id, Album album, LocalDate releaseDate) {
        this.id = id;
        this.album = album;
        this.releaseDate = releaseDate;
    }

    public MyAlbum(Album album, LocalDate releaseDate) {
        this.album = album;
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

    // getters and setters
    public Album getAlbum() {
        return album;
    }

    public void setAlbum(Album album) {
        this.album = album;
    }

    public LocalDate getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(LocalDate releaseDate) {
        this.releaseDate = releaseDate;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

}
