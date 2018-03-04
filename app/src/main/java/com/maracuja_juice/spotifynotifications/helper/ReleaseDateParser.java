package com.maracuja_juice.spotifynotifications.helper;


import org.joda.time.LocalDate;

import java.util.Arrays;

/**
 * Created by Maurice on 04.03.18.
 */

public class ReleaseDateParser {
    /**
     *
     * @param date
     * The date the album was first released, for example "1981-12-15".
     * Depending on the precision, it might be shown as "1981" or "1981-12".
     * @param datePrecision
     * The precision with which release_date value is known: "year", "month", or "day".
     */
    public static LocalDate parseReleaseDate(String date, String datePrecision) {
        int year;
        int month;
        int day;
        if(datePrecision.equals("year")) {
            year = Integer.parseInt(date);
            month = 1;
            day = 1;
        } else if(datePrecision.equals("month")) {
            int[] intArray = getIntArray(date);
            year = intArray[0];
            month = intArray[1];
            day = 1;
        } else if(datePrecision.equals("day")) {
            int[] intArray = getIntArray(date);
            year = intArray[0];
            month = intArray[1];
            day = intArray[2];
        } else {
            throw new IllegalArgumentException("expected 'month', 'day' or 'year' as date precision");
        }

        return new LocalDate(year, month, day);
    }

    private static int[] stringToIntArray(String[] stringArray) {
        int[] intArray = new int[stringArray.length];
        for (int i = 0; i < stringArray.length; i++) {
            intArray[i] = Integer.parseInt(stringArray[i]);
        }
        return intArray;
    }

    private static int[] getIntArray(String dateWithDashes) {
        String[] splittedDate = dateWithDashes.split("-");
        return stringToIntArray(splittedDate);
    }
}
