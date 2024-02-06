package com.example.haszelcast.controller;

import com.example.haszelcast.service.VehicleArchitectureServiceImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/vehicles")
public class VehicleController {

    private final VehicleArchitectureServiceImpl vehicleService;

    public VehicleController(VehicleArchitectureServiceImpl vehicleService) {
        this.vehicleService = vehicleService;
    }

    @GetMapping("/{squishVin}")
    public ResponseEntity<String> getVehicleBodyType(@PathVariable String squishVin) {
        String bodyType = vehicleService.getVehicleBodyType(squishVin);
        return ResponseEntity.ok(bodyType);
    }

//    @PutMapping("/{squishVin}")
//    public ResponseEntity<?> updateVehicleBodyType(@PathVariable String squishVin, @RequestBody String vehBodyType) {
//        vehicleService.updateVehicleBodyType(squishVin, vehBodyType);
//        return ResponseEntity.ok().build();
//    }
}
