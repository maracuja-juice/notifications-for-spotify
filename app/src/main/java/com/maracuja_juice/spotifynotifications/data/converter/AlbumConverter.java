package com.maracuja_juice.spotifynotifications.data.converter;

import com.google.gson.Gson;

import io.objectbox.converter.PropertyConverter;
import kaaes.spotify.webapi.android.models.Album;


/**
 * Created by Maurice on 24.03.18.
 */

public class AlbumConverter implements PropertyConverter<Album, String> {
    @Override
    public Album convertToEntityProperty(String databaseValue) {
        if (databaseValue == null) {
            return null;
        }
        Gson gson = new Gson();
        return gson.fromJson(databaseValue, Album.class);
    }

    @Override
    public String convertToDatabaseValue(Album entityProperty) {
        Gson gson = new Gson();
        return gson.toJson(entityProperty);
    }
}
