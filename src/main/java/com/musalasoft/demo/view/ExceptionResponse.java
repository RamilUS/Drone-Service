package com.musalasoft.demo.view;

public record ExceptionResponse(
        Long errorCode,
        String errorCause,
        String errorMessage) {
}
