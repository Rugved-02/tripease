package com.tripease.hotels.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // 1. Handle 404 - Resource Not Found (Hotel/Rating not in DB)
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFound(ResourceNotFoundException ex) {
        ErrorResponse body = ErrorResponse.builder(ex, HttpStatus.NOT_FOUND, ex.getMessage())
                .title("Resource Not Found")
                .property("timestamp", Instant.now())
                .build();
        return new ResponseEntity<>(body, HttpStatus.NOT_FOUND);
    }

    // 2. Handle 409 - Business Logic Conflict (e.g., Deactivating an already inactive hotel)
    @ExceptionHandler(HotelActionException.class)
    public ResponseEntity<ErrorResponse> handleConflict(HotelActionException ex) {
        ErrorResponse body = ErrorResponse.builder(ex, HttpStatus.CONFLICT, ex.getMessage())
                .title("Business Logic Conflict")
                .property("timestamp", Instant.now())
                .build();
        return new ResponseEntity<>(body, HttpStatus.CONFLICT);
    }

    // 3. Handle 400 - Validation Errors (@Valid in RequestBody)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error -> 
            errors.put(error.getField(), error.getDefaultMessage())
        );

        ErrorResponse body = ErrorResponse.builder(ex, HttpStatus.BAD_REQUEST, "Input validation failed")
                .title("Constraint Violation")
                .property("timestamp", Instant.now())
                .property("errors", errors)
                .build();
        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }

    // 4. Handle 400 - Type Mismatch (e.g., sending 'abc' for hotelId)
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorResponse> handleTypeMismatch(MethodArgumentTypeMismatchException ex) {
        String detail = String.format("Parameter '%s' expects type '%s', but received '%s'", 
                ex.getName(), ex.getRequiredType().getSimpleName(), ex.getValue());
        
        ErrorResponse body = ErrorResponse.builder(ex, HttpStatus.BAD_REQUEST, detail)
                .title("Type Mismatch")
                .property("timestamp", Instant.now())
                .build();
        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }

    // 5. Handle 400 - Custom Business Validation (Invalid dates, etc.)
    @ExceptionHandler(InvalidInputException.class)
    public ResponseEntity<ErrorResponse> handleInvalidInput(InvalidInputException ex) {
        ErrorResponse body = ErrorResponse.builder(ex, HttpStatus.BAD_REQUEST, ex.getMessage())
                .title("Invalid Input")
                .property("timestamp", Instant.now())
                .build();
        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }

    // 6. Handle 500 - Generic Catch-All
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGlobalException(Exception ex) {
        ErrorResponse body = ErrorResponse.builder(ex, HttpStatus.INTERNAL_SERVER_ERROR, "An unexpected internal error occurred")
                .title("Internal Server Error")
                .property("timestamp", Instant.now())
                .build();
        return new ResponseEntity<>(body, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}