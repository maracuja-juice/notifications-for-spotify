package com.maracuja_juice.spotifynotifications.helper;

import com.annimon.stream.Stream;
import com.maracuja_juice.spotifynotifications.data.MyAlbum;

import org.joda.time.LocalDate;

import java.util.ArrayList;
import java.util.List;

import kaaes.spotify.webapi.android.models.Album;

/**
 * Created by Maurice on 30.03.18.
 */

public class AlbumToMyAlbumConverter {
    public static List<MyAlbum> convertAlbumsToMyAlbums(List<Album> albums) {
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
}
