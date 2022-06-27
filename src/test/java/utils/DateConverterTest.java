package utils;


import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DateConverterTest {

    @org.junit.jupiter.api.Test
    void testConvertStringToLocalDateTimeMethod() {
        String date = "2022-04-20";
        String time = "06:09:42";
        LocalDateTime dateTime = LocalDateTime.of(2022, 4, 20, 6, 9, 42);
        LocalDateTime dateTimeExclTime = LocalDateTime.of(2022, 4, 20, 0, 0, 0);

        LocalDateTime convertedDateTime = DateConverter.convertStringToLocalDateTime(date, time);
        LocalDateTime convertedDateTimeExclTime = DateConverter.convertStringToLocalDateTime(date);

        assertEquals(dateTime, convertedDateTime);
        assertEquals(dateTimeExclTime, convertedDateTimeExclTime);

    }

    @org.junit.jupiter.api.Test
    void testConvertUnixTimestampToLocalDateTimeMethod() {
        long unixTimestamp = 1654661382000L; // Wed Jun 08 2022 04:09:42 GMT+0000 -> Wed Jun 08 2022 06:09:42 GMT+0200 (Central European Summer Time)
        LocalDateTime dateTime = LocalDateTime.of(2022, 6, 8, 6, 9, 42);
        LocalDateTime convertedDateTime = DateConverter.convertUnixTimestampToLocalDateTime(unixTimestamp);

        assertEquals(dateTime, convertedDateTime);
    }

    @org.junit.jupiter.api.Test
    void testConvertLocalDateTimeToUnixTimestampMethod() {
        LocalDateTime dateTime = LocalDateTime.of(2022, 6, 8, 6, 9, 42);
        long unixTimestamp = 1654661382000L; // Wed Jun 08 2022 04:09:42 GMT+0000 -> Wed Jun 08 2022 06:09:42 GMT+0200 (Central European Summer Time)
        long convertedTimestamp = DateConverter.convertLocalDateTimeToUnixTimestamp(dateTime);

        assertEquals(unixTimestamp, convertedTimestamp);
    }


}