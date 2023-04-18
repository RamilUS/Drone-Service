package com.musalasoft.demo.exception;

public class RegistrationException extends RuntimeException {

    public RegistrationException(String errorMessage) {
        super(errorMessage);
    }
}
