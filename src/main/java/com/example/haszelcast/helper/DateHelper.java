package com.example.haszelcast.helper;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateHelper {

    private DateHelper() {}

    private static final String DATE_FORMAT = "yyyyMMddHHmmss";

//    public boolean isWithinSame5Second(final Date first,final  Date second) {
//        // Ensure first is not after second
//        if (first.after(second)) {
//            throw new IllegalArgumentException("first must be less than or equal to second");
//        }
//
//        // Convert dates to milliseconds since the epoch
//        final long millis1 = first.getTime();
//        final long millis2 = second.getTime();
//
//        // Calculate the difference in milliseconds
//        final long diff = millis2 - millis1; // Assuming second is always after or equal to first due to the check
//
//        // Check if the difference is within 5000 milliseconds (5 seconds), inclusive
//        return diff <= 5000;
//    }


    public static  boolean isSameDay(final Date date1, final Date date2) {
        final SimpleDateFormat fmt = new SimpleDateFormat(DATE_FORMAT);
        return fmt.format(date1).equals(fmt.format(date2));
    }

}
