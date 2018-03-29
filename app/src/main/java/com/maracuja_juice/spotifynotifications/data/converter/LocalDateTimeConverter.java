package com.maracuja_juice.spotifynotifications.data.converter;

import org.joda.time.LocalDateTime;

import io.objectbox.converter.PropertyConverter;

/**
 * Created by Maurice on 24.03.18.
 */

public class LocalDateTimeConverter implements PropertyConverter<LocalDateTime, String> {
    @Override
    public LocalDateTime convertToEntityProperty(String databaseValue) {
        if (databaseValue == null) {
            return null;
        }

        return LocalDateTime.parse(databaseValue);
    }

    @Override
    public String convertToDatabaseValue(LocalDateTime entityProperty) {
        return entityProperty.toString();
    }
}
