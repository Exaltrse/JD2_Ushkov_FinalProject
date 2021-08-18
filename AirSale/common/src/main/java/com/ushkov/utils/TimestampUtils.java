package com.ushkov.utils;

import java.sql.Timestamp;

public class TimestampUtils {
    public static Timestamp toBeginningOfDay(Timestamp timestamp){
        timestamp.setHours(0);
        timestamp.setNanos(0);
        timestamp.setMinutes(0);
        timestamp.setSeconds(0);
        return timestamp;
    }

    public static Timestamp toEndingOfDay(Timestamp timestamp){
        timestamp.setHours(23);
        timestamp.setNanos(0);
        timestamp.setMinutes(59);
        timestamp.setSeconds(59);
        return timestamp;
    }
}
//year – the year minus 1900
//        month – 0 to 11
//        date – 1 to 31
//        hour – 0 to 23
//        minute – 0 to 59
//        second – 0 to 59
//        nano – 0 to 999,999,999