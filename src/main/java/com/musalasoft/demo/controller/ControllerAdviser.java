package com.musalasoft.demo.controller;

import com.musalasoft.demo.view.ExceptionResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ControllerAdviser {
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ExceptionResponse> handleException(Exception e) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ExceptionResponse(-1L, e.getClass().getName(), e.getMessage()));
    }
}