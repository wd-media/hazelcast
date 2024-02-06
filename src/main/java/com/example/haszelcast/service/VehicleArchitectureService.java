package com.example.haszelcast.service;

import java.util.Map;

public interface  VehicleArchitectureService {

    Map<String, String> retrieveVehicleArchitectureMap();
    void updateVehicleArchitectureMap(Map<String, String> updates);

}
