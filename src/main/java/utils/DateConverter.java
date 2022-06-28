package utils;

import java.time.*;
import java.util.Objects;

/**
 * This class is used to convert dates to and from unix timestamps as well as calculate time differences.
 */
public class DateConverter {

    private static final long oneYearInUnixMilli = 31556926000L;
    private static final long oneMonthInUnixMilli = 2629743000L;
    private static final long oneWeekInUnixMilli = 604800000L;
    private static final long oneDayInUnixMilli = 86400000L;

    /**
     * System default timezone.
     */
    public static final ZoneOffset timeZone = ZoneId.systemDefault().getRules().getOffset(Instant.now());

    /**
     * @param date The date string to convert to {@link LocalDate}.
     * @return The converted {@link LocalDate}.
     */
    public static LocalDate convertStringToLocalDate(String date) {
        if (Objects.equals(date, "")) throw new IllegalArgumentException("Datum darf nicht leer sein.");

        LocalDate result;
        String[] array = date.split("-");

        if (array.length >= 3) {
            result = LocalDate.of(Integer.parseInt(array[0]), Integer.parseInt(array[1]),
                    Integer.parseInt(array[2]));
            return result;
        } else throw new IllegalArgumentException("Das Datum muss in dem Format 'YYYY-MM-DD' angegeben werden.");

    }

    /**
     * @param time The time string to convert to {@link LocalTime}.
     * @return The converted {@link LocalTime}.
     */
    public static LocalTime convertStringToLocalTime(String time) {
        String[] array = time.split(":");
        LocalTime result;
        try {
            if (array.length >= 2) {
                result = LocalTime.of(Integer.parseInt(array[0]), Integer.parseInt(array[1]));
            } else {
                result = LocalTime.of(Integer.parseInt(array[0]), 0);
            }
            return result;
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Die Uhrzeit darf nicht leer sein und muss im Format 16:20 angegeben werden.");
        } catch (DateTimeException e) {
            throw new IllegalArgumentException("Bitte gib eine gültige Uhrzeit an. (00:00-23:59)");
        }
    }

    /**
     * @param date The date string to convert to {@link LocalDateTime}.
     * @return The converted {@link LocalDateTime}.
     */
    public static LocalDateTime convertStringToLocalDateTime(String date) {
        String[] dateArray = date.split("-");

        return LocalDateTime.of(Integer.parseInt(dateArray[0]), Integer.parseInt(dateArray[1]),
                Integer.parseInt(dateArray[2]), 0, 0, 0);
    }

    /**
     * @param date The date string to convert to {@link LocalDateTime}.
     * @param time The time string to convert to {@link LocalDateTime}.
     * @return The converted {@link LocalDateTime}.
     */
    public static LocalDateTime convertStringToLocalDateTime(String date, String time) {
        String[] dateArray = date.split("-");
        String[] timeArray = time.split(":");

        return LocalDateTime.of(Integer.parseInt(dateArray[0]), Integer.parseInt(dateArray[1]),
                Integer.parseInt(dateArray[2]), Integer.parseInt(timeArray[0]), Integer.parseInt(timeArray[1]),
                0);
    }

    /**
     * @param date The date string to convert to a unix timestamp.
     * @return The converted unix timestamp.
     */
    //    Convert string to unixTimestamp
    public static long convertStringToUnixTimestamp(String date) {
        return DateConverter.convertLocalDateTimeToUnixTimestamp(DateConverter.convertStringToLocalDateTime(date));
    }    //    Convert string to unixTimestamp

    /**
     * @param date The date string to convert to a unix timestamp.
     * @param time The time string to convert to a unix timestamp.
     * @return The converted unix timestamp.
     */
    public static long convertStringToUnixTimestamp(String date, String time) {
        return DateConverter.convertLocalDateTimeToUnixTimestamp(DateConverter.convertStringToLocalDateTime(date, time));
    }

    /**
     * @param timestamp The unix timestamp to convert to a {@link LocalDate}.
     * @return The converted {@link LocalDate}.
     */
    public static LocalDate convertUnixTimestampToLocalDate(long timestamp) {
        return convertUnixTimestampToLocalDateTime(timestamp).toLocalDate();
    }

    /**
     * @param timestamp The unix timestamp to convert to a {@link LocalTime}.
     * @return The converted {@link LocalTime}.
     */
    public static LocalTime convertUnixTimestampToLocalTime(long timestamp) {
        return convertUnixTimestampToLocalDateTime(timestamp).toLocalTime();
    }

    /**
     * @param timestamp The unix timestamp to convert to a {@link LocalDateTime}.
     * @return The converted {@link LocalDateTime}.
     */
    public static LocalDateTime convertUnixTimestampToLocalDateTime(long timestamp) {
        return Instant.ofEpochMilli(timestamp).atOffset(timeZone).toLocalDateTime();
    }

    /**
     * @param dateTime The {@link LocalDateTime} to convert to a unix timestamp.
     * @return The converted unix timestamp.
     */
    public static long convertLocalDateTimeToUnixTimestamp(LocalDateTime dateTime) {
        return dateTime.toInstant(timeZone).toEpochMilli();
    }

    /**
     * @return The current unix timestamp.
     */
    public static long unixTimestampNow() {
        return Instant.now().toEpochMilli();
    }

    /**
     * @param unixTime The unix timestamp to check.
     * @return True if the unix timestamp is in the past 10 years. False otherwise.
     */
    public static boolean isWithinLast10Years(long unixTime) {
        long tenYearsInUnixTime = oneYearInUnixMilli * 10;
        return (Instant.now().toEpochMilli() - tenYearsInUnixTime) < unixTime;
    }

    /**
     * @param timeAgo The time ago to check.
     * @return The unix time stamp of the time ago provided.
     */
    public static long getUnixMilliHowLongAgo(String timeAgo) {
        String[] arguments = timeAgo.split(" ");
        int amount = -1;
        String unit = null;
        try {
            amount = Integer.parseInt(arguments[0]);
            unit = arguments[1];
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (unit == null) return -1;

        switch (unit.toLowerCase()) {
            case "day":
            case "days":
                return Instant.now().toEpochMilli() - oneDayInUnixMilli * amount;
            case "week":
            case "weeks":
                return Instant.now().toEpochMilli() - oneWeekInUnixMilli * amount;
            case "month":
            case "months":
                return Instant.now().toEpochMilli() - oneMonthInUnixMilli * amount;
            case "year":
            case "years":
                return Instant.now().toEpochMilli() - oneYearInUnixMilli * amount;
            default:
                return -1;
        }
    }

}
