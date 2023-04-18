package com.musalasoft.demo.view;

import java.util.Map;

public record LoadDroneResult(
        Long errorCode,
        String errorCause,
        String errorMessage,
        Map<String, Integer> loadedMeds
) {
}