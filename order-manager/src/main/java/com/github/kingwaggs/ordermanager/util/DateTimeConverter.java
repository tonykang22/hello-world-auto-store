package com.github.kingwaggs.ordermanager.util;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class DateTimeConverter {

    public static final String DATE_TIME_FORMAT = "yyyy-MM-dd'T'HH:mm:ss";
    public static final String DATE_FORMAT = "yyyy-MM-dd";

    private DateTimeConverter() {}

    public static LocalDateTime dateTimeString2LocalDateTime(String dateTimeString) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_TIME_FORMAT);
        return LocalDateTime.parse(dateTimeString, formatter);
    }

    public static LocalDateTime zincDateTimeString2LocalDateTime(String dateTimeString) {
        String formatLengthDateTimeString = dateTimeString.substring(0, DATE_TIME_FORMAT.length() - 2);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_TIME_FORMAT);
        return LocalDateTime.parse(formatLengthDateTimeString, formatter);
    }

    public static String localDateTime2DateTimeString(LocalDateTime dateTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_TIME_FORMAT);
        return dateTime.format(formatter);
    }

    public static String localDate2DateString(LocalDate date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_FORMAT);
        return date.format(formatter);
    }

    public static Long localDateTime2Unix(LocalDateTime dateTime) {
        Instant instant = dateTime.atZone(ZoneId.systemDefault()).toInstant();
        return instant.toEpochMilli();
    }
}
