package com.ep.linkedlist.util;

import android.util.Log;

import org.apache.commons.lang3.time.DateFormatUtils;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TimeZone;

/**
 * Created by jiwon on 2017-02-03.
 */

public class CalendarFactory {
    public static Calendar createInstance(){
        return new GregorianCalendar(TimeZone.getTimeZone("Asia/Seoul"));
    }

    public static Calendar createInstance(long timestamp) {
        Calendar calendar = CalendarFactory.createInstance();
        calendar.setTimeInMillis(timestamp);
        return calendar;
    }
}
