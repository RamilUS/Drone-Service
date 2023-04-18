package com.musalasoft.demo.view;

public record RegisterMedicationResult(
        Long errorCode,
        String errorCause,
        String errorMessage,
        String medicationCode
) {
}