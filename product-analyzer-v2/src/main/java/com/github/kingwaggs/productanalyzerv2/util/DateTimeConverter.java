package com.github.kingwaggs.productanalyzerv2.util;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.TimeZone;

public class DateTimeConverter {

    private static final String DATE_TIME_FORMAT = "yyyy-MM-dd'T'HH:mm:ss";
    private static final String DATE_FORMAT = "yyyy-MM-dd";

    private DateTimeConverter() {
    }

    public static LocalDateTime dateTimeString2LocalDateTime(String dateTimeString) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_TIME_FORMAT);
        return LocalDateTime.parse(dateTimeString, formatter);
    }

    public static LocalDate dateString2LocalDate(String dateString) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_FORMAT);
        return LocalDate.parse(dateString, formatter);
    }

    public static LocalDateTime dateString2LocalDateTimeStartOfDay(String dateString) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_FORMAT);
        LocalDate localDate = LocalDate.parse(dateString, formatter);
        return localDate.atStartOfDay();
    }

    public static String localDateTime2DateTimeString(LocalDateTime dateTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_TIME_FORMAT);
        return dateTime.format(formatter);
    }

    public static String localDateTime2DateString(LocalDateTime dateTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_FORMAT);
        return dateTime.format(formatter);
    }

    public static String localDate2DateString(LocalDate date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_FORMAT);
        return date.format(formatter);
    }

    public static LocalDateTime localDate2LocalDateTime(LocalDate date) {
        return date.atStartOfDay();
    }

    public static String unixToStringTime(Long unixTime) {
        Date date = new Date(unixTime);
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_TIME_FORMAT);
        sdf.setTimeZone(TimeZone.getTimeZone("Asia/Seoul"));
        return sdf.format(date);
    }

}
