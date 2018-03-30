package com.maracuja_juice.spotifynotifications.interfaces;

import java.util.List;

import kaaes.spotify.webapi.android.models.Album;

/**
 * Created by Marimba on 28.01.2018.
 */

public interface DownloadCompleted {
    void downloadComplete(List<Album> result);
}
