package com.tripease.payment.exception;

import org.springframework.context.MessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.validation.FieldError;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.HandlerMethodValidationException;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // 1. Handle 404 - Resource Not Found (e.g., specific Booking/Payment ID missing)
    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<ErrorResponse> handleNoSuchElementException(NoSuchElementException ex) {
        ErrorResponse body = ErrorResponse.builder(ex, HttpStatus.NOT_FOUND, ex.getMessage())
                .title("Resource Not Found")
                .property("timestamp", Instant.now())
                .build();
        return new ResponseEntity<>(body, HttpStatus.NOT_FOUND);
    }

    // 2. Handle 400 - Header/Parameter Validation (Spring 6+ specific)
    @ExceptionHandler(HandlerMethodValidationException.class)
    public ResponseEntity<ErrorResponse> handleMethodValidationException(HandlerMethodValidationException ex) {
        String detail = ex.getParameterValidationResults().stream()
                .flatMap(result -> result.getResolvableErrors().stream())
                .map(MessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.joining(", "));

        ErrorResponse body = ErrorResponse.builder(ex, HttpStatus.BAD_REQUEST, detail)
                .title("Validation Error")
                .property("timestamp", Instant.now())
                .build();
        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }

    // 3. Handle 400 - Request Body Validation (@Valid failures)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        // Industry standard: provide a clean map of field names to error messages
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage())
        );

        ErrorResponse body = ErrorResponse.builder(ex, HttpStatus.BAD_REQUEST, "Invalid request content")
                .title("Constraint Violation")
                .property("timestamp", Instant.now())
                .property("errors", errors)
                .build();
        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }

    // 4. Handle 409 - Concurrency Conflict (Optimistic Locking)
    @ExceptionHandler(ObjectOptimisticLockingFailureException.class)
    public ResponseEntity<ErrorResponse> handleObjectOptimisticLockingFailureException(ObjectOptimisticLockingFailureException ex) {
        ErrorResponse body = ErrorResponse.builder(ex, HttpStatus.CONFLICT,
                        "The record was updated by another process. Please refresh and try again.")
                .title("Concurrency Conflict")
                .property("timestamp", Instant.now())
                .build();
        return new ResponseEntity<>(body, HttpStatus.CONFLICT);
    }

    // 5. Handle 409 - Database Integrity (Duplicate Emails/Unique Constraints)
    @ExceptionHandler(org.springframework.dao.DataIntegrityViolationException.class)
    public ResponseEntity<ErrorResponse> handleDataIntegrity(org.springframework.dao.DataIntegrityViolationException ex) {
        String detail = "Database constraint violation.";
        if (ex.getMostSpecificCause().getMessage().contains("Duplicate entry")) {
            detail = "This record already exists in our system.";
        }

        ErrorResponse body = ErrorResponse.builder(ex, HttpStatus.CONFLICT, detail)
                .title("Data Integrity Error")
                .property("timestamp", Instant.now())
                .build();
        return new ResponseEntity<>(body, HttpStatus.CONFLICT);
    }

    // 6. Handle 500 - Generic Catch-All
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleLeftException(Exception ex) {
        // Logic: Log the full error internally, return generic message to user for security
        ErrorResponse body = ErrorResponse.builder(ex, HttpStatus.INTERNAL_SERVER_ERROR, "An unexpected error occurred")
                .title("Internal Server Error")
                .property("timestamp", Instant.now())
                .build();

        return new ResponseEntity<>(body, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}