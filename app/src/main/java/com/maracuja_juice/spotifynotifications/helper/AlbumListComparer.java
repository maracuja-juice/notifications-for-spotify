package com.maracuja_juice.spotifynotifications.helper;

import com.annimon.stream.Collectors;
import com.annimon.stream.Stream;

import java.util.List;

import kaaes.spotify.webapi.android.models.Album;

/**
 * Created by Maurice on 30.03.18.
 */

public class AlbumListComparer {
    public static List<Album> getAddedAlbums(List<Album> downloadedAlbums, List<Album> savedAlbums) {
        List<Album> newAlbums = Stream.of(downloadedAlbums).filter(downloadedAlbum -> {
            boolean albumIsAlreadySaved = Stream.of(savedAlbums)
                    .anyMatch(album -> album.id.equals(downloadedAlbum.id));
            return !albumIsAlreadySaved;
        }).collect(Collectors.toList());
        return newAlbums;
    }
}
