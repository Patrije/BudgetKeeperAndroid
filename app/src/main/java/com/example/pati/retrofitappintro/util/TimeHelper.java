package com.example.pati.retrofitappintro.util;

/**
 * Created by Pati on 12.12.2018.
 */

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class TimeHelper {

    private static CalendarFactory regularCalendarFactory = new CalendarFactory() {
        @Override
        public Calendar getNewInstance() {
            return Calendar.getInstance();
        }
    };
    private static CalendarFactory calendarFactory = regularCalendarFactory;

    public static Calendar getNow() {
        return calendarFactory.getNewInstance();
    }

    public static String getActualDate(){
        String pattern = "yyyy-MM-dd";
        DateFormat dateFormat = new SimpleDateFormat(pattern);
        Date date = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return dateFormat.format(date);
    }

    public static Calendar getCalendar(int year, int month, int day, int hour, int minute) {
        return TimeHelper.getCalendar(year, month, day, hour, minute, 0);
    }

    public static Calendar getCalendar(int year, int month, int day, int hour, int minute, int seconds) {
        java.util.Calendar calendar = java.util.Calendar.getInstance();
        calendar.set(year, month, day, hour, minute, seconds);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar;
    }

    public abstract static class CalendarFactory {

        public abstract Calendar getNewInstance();
    }
}
