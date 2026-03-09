package com.tripease.analytics.exception;

import feign.FeignException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    // 1. Handles User-Defined Analytics Errors
    @ExceptionHandler(AnalyticsException.class)
    public ResponseEntity<Object> handleAnalyticsException(AnalyticsException ex) {
        return buildResponse(ex.getStatus(), "Analytics Processing Error", ex.getMessage());
    }

    // 2. Handles Feign/Network Errors (If Booking or User service is offline)
    @ExceptionHandler(FeignException.class)
    public ResponseEntity<Object> handleFeignStatusException(FeignException ex) {
        HttpStatus status = HttpStatus.resolve(ex.status());
        if (status == null) status = HttpStatus.SERVICE_UNAVAILABLE;

        String message = "The Analytics service cannot reach TripEase dependencies. Ensure Booking (8084) and User (8081) services are running.";
        return buildResponse(status, "Dependency Service Failure", message);
    }

    // 3. Handles Math/Calculation Errors
    @ExceptionHandler(ArithmeticException.class)
    public ResponseEntity<Object> handleArithmeticException(ArithmeticException ex) {
        return buildResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Calculation Error",
                "Division by zero detected. Ensure your database contains booking and user records.");
    }

    // 4. Global Catch-All
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleGeneralException(Exception ex) {
        return buildResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error",
                "An unexpected error occurred: " + ex.getMessage());
    }

    // Helper method to keep the code DRY (Don't Repeat Yourself)
    private ResponseEntity<Object> buildResponse(HttpStatus status, String error, String message) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", status.value());
        body.put("error", error);
        body.put("message", message);
        return new ResponseEntity<>(body, status);
    }
}