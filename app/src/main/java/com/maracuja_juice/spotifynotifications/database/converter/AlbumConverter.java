package com.maracuja_juice.spotifynotifications.database.converter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

import io.objectbox.converter.PropertyConverter;
import kaaes.spotify.webapi.android.models.Album;


/**
 * Created by Maurice on 24.03.18.
 */

public class AlbumConverter implements PropertyConverter<Album, String> {
    private ObjectMapper mapper = new ObjectMapper(); // create once, reuse

    @Override
    public Album convertToEntityProperty(String databaseValue) {
        if (databaseValue == null) {
            return null;
        }

        try {
            return mapper.readValue(databaseValue, Album.class);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public String convertToDatabaseValue(Album entityProperty) {
        try {
            return mapper.writeValueAsString(entityProperty);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return null;
    }
}