package com.musalasoft.demo.view;

public record RegisterDroneResult(
        Long errorCode,
        String errorCause,
        String errorMessage,
        String serialNumber
) {
}
