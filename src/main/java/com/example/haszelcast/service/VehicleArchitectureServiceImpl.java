package com.example.haszelcast.service;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.map.IMap;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class VehicleArchitectureServiceImpl implements VehicleArchitectureService {

    private static final String VEH_ARCHITECTURE_MAP = "vehicleArchitectureMap";

    private final IMap<String, String> map;
    private final ElasticSearchService elasticSearchService;

    public VehicleArchitectureServiceImpl(final HazelcastInstance hazelcastInstance,
                                          final ElasticSearchService elasticSearchService) {
        this.map = hazelcastInstance.getMap(VEH_ARCHITECTURE_MAP);
        this.elasticSearchService = elasticSearchService;
    }

    public String getVehicleBodyType(final String squishVin) {
        return map.get(squishVin);
    }

    @Override
    public void updateVehicleArchitectureMap(final Map<String, String> updates) {
        this.map.putAll(updates);
    }

    @Override
    public Map<String, String> retrieveVehicleArchitectureMap() {
        return this.elasticSearchService.getMap();
    }

}
