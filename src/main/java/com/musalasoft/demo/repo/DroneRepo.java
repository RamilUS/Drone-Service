package com.musalasoft.demo.repo;

import com.musalasoft.demo.model.Drone;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DroneRepo extends JpaRepository<Drone, String> {
    public Optional<Drone> findByTemplateId(String droneId);
}
