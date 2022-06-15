package utils;

import java.time.*;

public class DateConverter {

    public static final ZoneOffset timeZone = ZoneId.systemDefault().getRules().getOffset(Instant.now());

    public static LocalDate convertStringToLocalDate(String date) {
        String[] array = date.split("-");
        LocalDate result = LocalDate.of(Integer.parseInt(array[0]), Integer.parseInt(array[1]),
                Integer.parseInt(array[2]));
        return result;
    }

    public static LocalTime convertStringToLocalTime(String time) {
        String[] array = time.split(":");
        LocalTime result = LocalTime.of(Integer.parseInt(array[0]), Integer.parseInt(array[1]));
        return result;
    }

    public static LocalDateTime convertStringToLocalDateTime(String date) {
        String[] dateArray = date.split("-");

        return LocalDateTime.of(Integer.parseInt(dateArray[0]), Integer.parseInt(dateArray[1]),
                Integer.parseInt(dateArray[2]), 0, 0, 0);
    }

    public static LocalDateTime convertStringToLocalDateTime(String date, String time) {
        String[] dateArray = date.split("-");
        String[] timeArray = time.split(":");

        return LocalDateTime.of(Integer.parseInt(dateArray[0]), Integer.parseInt(dateArray[1]),
                Integer.parseInt(dateArray[2]), Integer.parseInt(timeArray[0]), Integer.parseInt(timeArray[1]),
                0);
    }

    //    Convert string to unixTimestamp
    public static long convertStringToUnixTimestamp(String date) {
        return DateConverter.convertLocalDateTimeToUnixTimestamp(DateConverter.convertStringToLocalDateTime(date));
    }    //    Convert string to unixTimestamp

    public static long convertStringToUnixTimestamp(String date, String time) {
        return DateConverter.convertLocalDateTimeToUnixTimestamp(DateConverter.convertStringToLocalDateTime(date, time));
    }

    //    Convert localDate to unixTimestamp
    public static long convertLocalDateToUnixTimestamp(LocalDate date) {
        LocalDateTime converted = LocalDateTime.from(date.atStartOfDay(DateConverter.timeZone));
        return DateConverter.convertLocalDateTimeToUnixTimestamp(converted);
    }

    public static LocalDate convertUnixTimestampToLocalDate(long timestamp) {
        return convertUnixTimestampToLocalDateTime(timestamp).toLocalDate();
    }

    public static LocalTime convertUnixTimestampToLocalTime(long timestamp) {
        return convertUnixTimestampToLocalDateTime(timestamp).toLocalTime();
    }

    public static LocalDateTime convertUnixTimestampToLocalDateTime(long timestamp) {
        return Instant.ofEpochMilli(timestamp).atOffset(timeZone).toLocalDateTime();
    }

    public static long convertLocalDateTimeToUnixTimestamp(LocalDateTime dateTime) {
        return dateTime.toInstant(timeZone).toEpochMilli();
    }

    public static long unixTimestampNow() {
        return Instant.now().toEpochMilli();
    }

}
