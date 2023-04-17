package com.musalasoft.demo.exception;

public class DroneLoadingException extends RuntimeException {

    public DroneLoadingException(String errorMessage) {
        super(errorMessage);
    }
}
