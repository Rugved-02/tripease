package com.tripease.flight.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;

/**
 * Global Exception Handler for Tripease Flight Service.
 * Uses Spring 6+ ErrorResponse and ProblemDetail (RFC 7807) for standardized responses.
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    // 1. Handle 404 - Resource Not Found (e.g., specific flight, date, or class missing)
    @ExceptionHandler(ResourceNotFoundException.class)
    public ProblemDetail handleResourceNotFound(ResourceNotFoundException ex) {
        log.error("Resource not found: {}", ex.getMessage());

        return ErrorResponse.builder(ex, HttpStatus.NOT_FOUND, ex.getMessage())
                .title("Resource Missing")
                .property("timestamp", Instant.now())
                .build()
                .getBody(); // Ensures neat JSON output in Postman
    }

    // 2. Handle 400 - Business Logic Violations (e.g., Insufficient seats)
    @ExceptionHandler(InsufficientSeatsException.class)
    public ProblemDetail handleInsufficientSeats(InsufficientSeatsException ex) {
        log.warn("Business constraint violation: {}", ex.getMessage());

        return ErrorResponse.builder(ex, HttpStatus.BAD_REQUEST, ex.getMessage())
                .title("Insufficient Inventory")
                .property("timestamp", Instant.now())
                .build()
                .getBody();
    }

    // 3. Handle 409 - Concurrency Failures (Optimistic Locking)
    @ExceptionHandler(org.springframework.orm.ObjectOptimisticLockingFailureException.class)
    public ProblemDetail handleConflict(org.springframework.orm.ObjectOptimisticLockingFailureException ex) {
        log.error("Optimistic locking failure: record updated by another user");

        return ErrorResponse.builder(ex, HttpStatus.CONFLICT, 
                "The record was updated by another process. Please refresh and try again.")
                .title("Concurrency Conflict")
                .property("timestamp", Instant.now())
                .build()
                .getBody();
    }

    // 4. Handle 500 - Catch-all for unexpected internal errors
    @ExceptionHandler(Exception.class)
    public ProblemDetail handleGlobalException(Exception ex) {
        log.error("Internal Server Error: ", ex);

        return ErrorResponse.builder(ex, HttpStatus.INTERNAL_SERVER_ERROR, 
                "An unexpected error occurred on our end.")
                .title("System Error")
                .property("timestamp", Instant.now())
                .build()
                .getBody();
    }
    // 5. Handle Type Mismatch (e.g., sending "abc" for a numeric ID or invalid date format)
    @ExceptionHandler(org.springframework.web.method.annotation.MethodArgumentTypeMismatchException.class)
    public org.springframework.http.ProblemDetail handleTypeMismatch(
            org.springframework.web.method.annotation.MethodArgumentTypeMismatchException ex) {
        
        String detail = String.format("The parameter '%s' has an invalid value: '%s'. Expected type: %s", 
                ex.getName(), ex.getValue(), ex.getRequiredType().getSimpleName());
        
        log.warn("Parameter type mismatch: {}", detail);

        return org.springframework.web.ErrorResponse.builder(ex, HttpStatus.BAD_REQUEST, detail)
                .title("Invalid Request Parameter")
                .property("timestamp", Instant.now())
                .build()
                .getBody();
    }
}