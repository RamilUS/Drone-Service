package com.musalasoft.demo.service;

import com.musalasoft.demo.model.medication.Medication;
import com.musalasoft.demo.repo.MedicationRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MedicationManager {
    private final MedicationRepo medRepo;

    public String registerMedication(String code, String name, Integer weight, byte[] image) {
        Medication drone = new Medication(
                code,
                1L,
                name,
                weight,
                image
        );
        medRepo.save(drone);
        return code;
    }
}
