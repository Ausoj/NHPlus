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

//    Convert string to localdatetime (param only date) -> time = 00:00

//    Convert string to localdatetime (param only time) -> date = now()

//    Convert string to localdatetime (param date, time)

    //    Convert string to unixTimestamp
    public static long convertStringToUnixTimestamp(String date) {
        return DateConverter.convertLocalDateToUnixTimestamp(DateConverter.convertStringToLocalDate(date));
    }

    //    Convert localDate to unixTimestamp
    public static long convertLocalDateToUnixTimestamp(LocalDate date) {
        LocalDateTime converted = LocalDateTime.from(date.atStartOfDay(DateConverter.timeZone));
        long unixDate = DateConverter.convertLocalDateTimeToUnixTimestamp(converted);
        return unixDate;
    }

    public static LocalDate convertUnixTimestampToLocalDate(long timestamp) {
        return convertUnixTimestampToLocalDateTime(timestamp).toLocalDate();
    }

    public static LocalDateTime convertUnixTimestampToLocalDateTime(long timestamp) {
        return Instant.ofEpochMilli(timestamp).atOffset(timeZone).toLocalDateTime();
    }

    public static long convertLocalDateTimeToUnixTimestamp(LocalDateTime dateTime) {
        return dateTime.toInstant(ZoneOffset.UTC).toEpochMilli();
    }
}
