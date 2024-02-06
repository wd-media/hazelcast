package com.example.haszelcast.service;

import com.example.haszelcast.helper.DateHelper;
import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class TimeServiceImplTest {

    private final TimeService timeService = new TimeServiceImpl();



    @Test
    void datesAreExactlyTheSame() {
        Date first = new Date();
        assertTrue(this.timeService.isWithinTimeWindow(first, first));
    }

    @Test
    void datesWithinSame5SecondWindowIncludingMilliseconds() {
        Date first = new Date();
        Date second = new Date(first.getTime() + 2500); // 2.5 seconds later
        assertTrue(this.timeService.isWithinTimeWindow(first, second));
    }

    @Test
    void datesExactly5SecondsApartToMillisecond() {
        Date first = new Date();
        Date second = new Date(first.getTime() + 5000); // Exactly 5 seconds later
        assertTrue(this.timeService.isWithinTimeWindow(first, second));
    }

    @Test
    void datesJustOver5SecondsApartByMilliseconds() {
        Date first = new Date();
        Date second = new Date(first.getTime() + 5001); // Just over 5 seconds
        assertFalse(this.timeService.isWithinTimeWindow(first, second));
    }

    @Test
    void datesFarApartBeyond5Seconds() {
        Date first = new Date();
        Date second = new Date(first.getTime() + 10000); // 10 seconds later
        assertFalse(this.timeService.isWithinTimeWindow(first, second));
    }

    @Test
    void firstDateAfterSecondDate() {
        Date first = new Date();
        Date second = new Date(first.getTime() - 1000); // first is 1 second after second
        Exception exception = assertThrows(IllegalArgumentException.class, () -> this.timeService.isWithinTimeWindow(first, second));
        assertEquals("first must be less than or equal to second", exception.getMessage());
    }

    @Test
    void edgeCasesAround5SecondBoundary() {
        Date first = new Date();
        Date second = new Date(first.getTime() + 4999); // Just inside 5 seconds
        assertTrue(this.timeService.isWithinTimeWindow(first, second));

        Date third = new Date(first.getTime() + 5001); // Just outside 5 seconds
        assertFalse(this.timeService.isWithinTimeWindow(first, third));
    }

    @Test
    void datesAcrossMinuteBoundary() {
        // Assuming `first` is at the end of a minute and `second` at the start of the next
        Date first = new Date(); // Mock the date to be at the very end of a minute if needed
        Date second = new Date(first.getTime() + 1000); // 1 second into the next minute
        assertTrue(this.timeService.isWithinTimeWindow(first, second));
    }


}
