package com.musalasoft.demo.service;

import com.musalasoft.demo.exception.DroneLoadingException;
import com.musalasoft.demo.exception.DroneRegistrationException;
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
     * @param weight
     * @return drone id;
     */
    public String registerDrone(String serialNumber, DroneType type) {
        Optional<Drone> drone = droneRepo.findById(serialNumber);

        if (drone.isPresent()) {
            throw new DroneRegistrationException("Drone with this serial number already registered");
        } else {
            droneRepo.save(new Drone(
                            serialNumber,
                            1L,
                            type,
                            0,
                            0,
                            State.IDLE,
                            null
                    )
            );
        }
        return serialNumber;
    }

    /**
     * Loading a drone with medication items;
     *
     * @param droneId        - drone serial number
     * @param medicationCode - medication code;
     * @throws DroneLoadingException
     */
    public void loadDrone(String droneId, String medicationCode) throws DroneLoadingException {
        Medication medication = medRepo.findById(medicationCode).orElseThrow(
                () -> new DroneLoadingException("Medication not found")
        );

        droneRepo.findById(droneId)
                .ifPresentOrElse(drone -> {
                    if (medication.getWeight() + drone.getWeight() > 500)
                        throw new DroneLoadingException("Medication is too heavy for this drone");
                    if (drone.getBatteryLevel() < 25)
                        throw new DroneLoadingException("Drone battery less than 25%");
                    drone.setWeight(medication.getWeight() + drone.getWeight());
                    drone.setState(State.LOADING);
                    Map<String, Integer> myMap = drone.getLoadedMeds();
                    myMap.put(medicationCode, myMap.getOrDefault(medicationCode, 0) + 1);
                    droneRepo.save(drone);

                }, () -> new DroneLoadingException("Drone not registered"));
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
        return droneRepo.findAllByState(State.IDLE);
    }

    /**
     * check drone battery level for a given drone
     *
     * @param droneId -drone serial number
     * @return drone battery level;
     */
    public Integer getBatteryLevel(String droneId) {
        return droneRepo.findById(droneId)
                .map(drone -> drone.getBatteryLevel()
                ).orElseThrow(
                        () -> new DroneLoadingException("Drone not registered")
                );
    }
}
