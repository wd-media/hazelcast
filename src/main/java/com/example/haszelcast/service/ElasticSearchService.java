package com.example.haszelcast.service;

import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@Service
public class ElasticSearchService {

    public Map<String, String> getMap() {
        final Map<String, String> randomPairs = new HashMap<>();
        final Random random = new Random();
        for (int i = 0; i < 10; i++) {
            final String squishVin = "VIN" + random.nextInt(1000);
            final String vehBodyType = "Type" + random.nextInt(5);
            randomPairs.put(squishVin, vehBodyType);
        }
        return randomPairs;
    }

}
