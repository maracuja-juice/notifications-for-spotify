package com.maracuja_juice.spotifynotifications.database.converter;

import org.joda.time.LocalDate;

import io.objectbox.converter.PropertyConverter;

/**
 * Created by Maurice on 24.03.18.
 */

public class LocalDateConverter implements PropertyConverter<LocalDate, String> {
    @Override
    public LocalDate convertToEntityProperty(String databaseValue) {
        if (databaseValue == null) {
            return null;
        }

        return LocalDate.parse(databaseValue);
    }

    @Override
    public String convertToDatabaseValue(LocalDate entityProperty) {
        return entityProperty.toString();
    }
}
