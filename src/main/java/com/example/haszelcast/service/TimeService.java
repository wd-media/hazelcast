package com.example.haszelcast.service;

import java.util.Date;

public interface  TimeService {

    Date now();
    boolean isWithinTimeWindow(Date first, Date second);

}
