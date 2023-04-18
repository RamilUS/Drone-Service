package com.musalasoft.demo.repo;

import com.musalasoft.demo.model.drone.Drone;
import com.musalasoft.demo.model.drone.State;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface DroneRepo extends JpaRepository<Drone, String> {
    Optional<Drone> findById(String droneId);

    List<Drone> findAllByState(State state);

    List<Drone> findByStateInAndBatteryLevelGreaterThan(List<State> state, int value);

}
