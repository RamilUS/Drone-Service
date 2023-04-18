package com.musalasoft.demo.controller;

import com.musalasoft.demo.model.drone.Drone;
import com.musalasoft.demo.model.drone.DroneType;
import com.musalasoft.demo.service.DroneManager;
import com.musalasoft.demo.view.LoadDroneResult;
import com.musalasoft.demo.view.RegisterDroneResult;
import com.musalasoft.demo.view.RegisterMedicationResult;
import jakarta.validation.constraints.Pattern;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

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
            @RequestParam(value = "type") DroneType type,
            @RequestParam(value = "battery") Integer batteryLevel
    ) {
        try {
            droneManager.registerDrone(serialNumber, type, batteryLevel);
        }catch (RuntimeException e){
            return ResponseEntity.ok(new RegisterDroneResult(-1L, e.getClass().getName(), e.getMessage(), null));
        }
        return ResponseEntity.ok(new RegisterDroneResult(0L, null, null, serialNumber));
    }

    @PostMapping(
            value = {"/registerMed"},
            consumes = {MediaType.APPLICATION_JSON_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE}
    )
    public ResponseEntity<RegisterMedicationResult> registerMedication(
            @PathVariable("version") String version,
            @Pattern(
                    regexp = "[A-Z0-9_]+",
                    message = "allowed only upper case letters, underscore and numbers"
            )
            @RequestParam(value = "medicationCode") String medicationCode,
            @Pattern(
                    regexp = "[a-zA-Z_0-9-]+",
                    message = "only letters, numbers, underscore and hyphen allowed"
            )
            @RequestParam(value = "name") String name,
            @RequestParam(value = "weight") Integer weight,
            @RequestBody byte[] image
    ) {
        try {
            droneManager.registerMed(medicationCode, name,weight,image);
        }catch (RuntimeException e){
            return ResponseEntity.ok(new RegisterMedicationResult(-1L, e.getClass().getName(), e.getMessage(), null));
        }
        return ResponseEntity.ok(new RegisterMedicationResult(0L, null, null, medicationCode));
    }

    @GetMapping("/getAvailableDrones")
    public ResponseEntity<List<Drone>> getAvailableDrones(@PathVariable String version) {
        return ResponseEntity.ok().body(droneManager.getAvailableDrones());
    }

    @GetMapping("/checkLoadedMedication")
    public ResponseEntity<Boolean> checkLoadedMedication(
            @RequestParam(value = "serialNumber") String serialNumber,
            @RequestParam(value = "medicationCode") String medicationCode,
            @PathVariable String version) {
        return ResponseEntity.ok().body(droneManager.checkLoadedMedication(serialNumber,medicationCode));
    }

    @PostMapping("/loadDrone")
    public ResponseEntity<LoadDroneResult> loadDrone(
            @RequestParam(value = "serialNumber") String droneId,
            @RequestParam(value = "medicationCode") String medicationCode,
            @PathVariable String version) {
        Map<String,Integer> resultMap;
        try {
            resultMap = droneManager.loadDrone(droneId,medicationCode);
        }catch (RuntimeException e){
            return ResponseEntity.ok(new LoadDroneResult(-1L,e.getClass().getName(), e.getMessage(), null));
        }
        return ResponseEntity.ok().body(new LoadDroneResult(0L,null, null, resultMap));
    }
}
