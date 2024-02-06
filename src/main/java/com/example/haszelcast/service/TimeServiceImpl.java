package com.example.haszelcast.service;

import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class TimeServiceImpl implements TimeService {

    @Override
    public Date now() {
        return new Date();
    }

    @Override
    public boolean isWithinTimeWindow(final Date first, final Date second) {

        // Ensure first is not after second
        if (first.after(second)) {
            throw new IllegalArgumentException("first must be less than or equal to second");
        }

        // Convert dates to milliseconds since the epoch
        final long millis1 = first.getTime();
        final long millis2 = second.getTime();

        // Calculate the difference in milliseconds
        final long diff = millis2 - millis1; // Assuming second is always after or equal to first due to the check

        // Check if the difference is within 5000 milliseconds (5 seconds), inclusive
        return diff <= 5000;
    }

}
