package com.musalasoft.demo.controller;

import com.musalasoft.demo.exception.DroneRegistrationException;
import com.musalasoft.demo.model.drone.Drone;
import com.musalasoft.demo.model.drone.DroneType;
import com.musalasoft.demo.service.DroneManager;
import com.musalasoft.demo.view.RegisterDroneResult;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Validated
@RestController
@RequestMapping("/drone/{version}/api")
@RequiredArgsConstructor
public class DroneController {
    private final DroneManager droneManager;

    @PostMapping(
            value = {"/registerDrone"},
            consumes = {MediaType.APPLICATION_JSON_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE}
    )
    public ResponseEntity<RegisterDroneResult> registerDrone(
            @PathVariable("version") String version,
            @RequestParam(value = "serialNumber") String serialNumber,
            @RequestParam(value = "type") DroneType type
    ) {
        try {
            droneManager.registerDrone(serialNumber, type);
        }catch (DroneRegistrationException e){
            return ResponseEntity.ok(new RegisterDroneResult(-1L, e.getClass().getName(), e.getMessage(), null));
        }
        return ResponseEntity.ok(new RegisterDroneResult(0L, null, null, serialNumber));
    }

    @GetMapping("getAvailableDrones")
    public ResponseEntity<List<Drone>> getAvailableDrones() {
        return ResponseEntity.ok().body(droneManager.getAvailableDrones());
    }
}
