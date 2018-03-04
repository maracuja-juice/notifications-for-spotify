package com.maracuja_juice.spotifynotifications;

import com.maracuja_juice.spotifynotifications.helper.ReleaseDateParser;

import org.joda.time.LocalDate;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Created by Maurice on 04.03.18.
 */

public class ReleaseDateParserTest {
    @Test
    public void parsedReleaseDate_dayPrecision_isCorrect() {
        String dateInput = "2017-04-12";
        String datePrecisionInput = "day";
        LocalDate expectedResult = new LocalDate(2017, 4, 12);
        LocalDate actualResult = ReleaseDateParser.parseReleaseDate(dateInput, datePrecisionInput);

        assertEquals(expectedResult, actualResult);
    }

    @Test
    public void parsedReleaseDate_monthPrecision_isCorrect() {
        String dateInput = "2017-03";
        String datePrecisionInput = "month";
        LocalDate expectedResult = new LocalDate(2017, 3, 1);
        LocalDate actualResult = ReleaseDateParser.parseReleaseDate(dateInput, datePrecisionInput);

        assertEquals(expectedResult, actualResult);
    }

    @Test
    public void parsedReleaseDate_yearPrecision_isCorrect() {
        String dateInput = "2016";
        String datePrecisionInput = "year";
        LocalDate expectedResult = new LocalDate(2016, 1, 1);
        LocalDate actualResult = ReleaseDateParser.parseReleaseDate(dateInput, datePrecisionInput);

        assertEquals(expectedResult, actualResult);
    }
}
