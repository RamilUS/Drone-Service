package com.musalasoft.demo.repo;

import com.musalasoft.demo.model.medication.Medication;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MedicationRepo extends JpaRepository<Medication, String> {
    public Optional<Medication> findById(String code);
}
