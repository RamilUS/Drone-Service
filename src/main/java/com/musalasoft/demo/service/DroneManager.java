package com.musalasoft.demo.service;

import com.musalasoft.demo.exception.DroneLoadingException;
import com.musalasoft.demo.exception.RegistrationException;
import com.musalasoft.demo.model.drone.Drone;
import com.musalasoft.demo.model.drone.DroneType;
import com.musalasoft.demo.model.drone.State;
import com.musalasoft.demo.model.medication.Medication;
import com.musalasoft.demo.repo.DroneRepo;
import com.musalasoft.demo.repo.MedicationRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DroneManager {
    private final DroneRepo droneRepo;
    private final MedicationRepo medRepo;

    /**
     * Registering a new drone;
     *
     * @param serialNumber
     * @param type
     * @return drone id;
     */
    public String registerDrone(String serialNumber, DroneType type, Integer batteryLevel) throws RegistrationException {
        Optional<Drone> drone = droneRepo.findById(serialNumber);

        if (drone.isPresent()) {
            throw new RegistrationException("Drone with this serial number is already registered");
        } else {
            droneRepo.save(new Drone(
                            serialNumber,
                            1L,
                            type,
                            0,
                            batteryLevel,
                            State.IDLE,
                            null
                    )
            );
        }
        return serialNumber;
    }

    public String registerMed(String medicationCode, String name, Integer weight, byte[] image) {
        Optional<Medication> medication = medRepo.findById(medicationCode);
        if (medication.isPresent()) {
            throw new RegistrationException("Medication with this code is already registered");
        } else {
            medRepo.save(new Medication(
                            medicationCode,
                            1L,
                            name,
                            weight,
                            image
                    )
            );
        }
        return medicationCode;
    }

    /**
     * Loading a drone with medication items;
     *
     * @param droneId        - drone serial number
     * @param medicationCode - medication code;
     * @throws DroneLoadingException
     */
    public Map<String, Integer> loadDrone(String droneId, String medicationCode) throws DroneLoadingException {
        Medication medication = medRepo.findById(medicationCode).orElseThrow(
                () -> new DroneLoadingException("Medication not found")
        );

        return droneRepo.findById(droneId)
                .map(drone -> {
                    if (medication.getWeight() + drone.getWeight() > 500)
                        throw new DroneLoadingException("Medication is too heavy for this drone");
                    if (drone.getBatteryLevel() < 25)
                        throw new DroneLoadingException("Drone battery less than 25%");
                    drone.setWeight(medication.getWeight() + drone.getWeight());
                    if (drone.getWeight() == 500) {
                        drone.setState(State.LOADED);
                    } else {
                        drone.setState(State.LOADING);
                    }
                    Map<String, Integer> myMap = drone.getLoadedMeds();
                    myMap.put(medicationCode, myMap.getOrDefault(medicationCode, 0) + 1);
                    droneRepo.save(drone);
                    return myMap;

                }).orElseThrow(() -> new DroneLoadingException("Drone not registered"));
    }

    /**
     * Check loaded medication items for a given drone
     *
     * @param droneId        drone serial number
     * @param medicationCode - medication code
     * @return true if the drone is loaded with medication, otherwise returns false
     */
    public boolean checkLoadedMedication(String droneId, String medicationCode) {
        return droneRepo.findById(droneId)
                .map(drone -> {
                    Map<String, Integer> myMap = drone.getLoadedMeds();
                    return myMap.containsKey(medicationCode);
                }).orElseThrow(
                        () -> new DroneLoadingException("Drone not registered")
                );

    }

    /**
     * checking available drones for loading;
     *
     * @return list of drones available for loading
     */
    public List<Drone> getAvailableDrones() {
        return droneRepo.findByStateInAndBatteryLevelGreaterThan(List.of(State.IDLE, State.LOADING), 25);
    }

    /**
     * check drone battery level for a given drone
     *
     * @param droneId -drone serial number
     * @return drone battery level;
     */
    public Integer getBatteryLevel(String droneId) {
        return droneRepo.findById(droneId)
                .map(Drone::getBatteryLevel
                ).orElseThrow(
                        () -> new DroneLoadingException("Drone not registered")
                );
    }
}
