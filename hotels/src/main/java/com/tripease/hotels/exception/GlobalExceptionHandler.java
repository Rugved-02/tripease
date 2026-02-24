package com.tripease.hotels.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // Handles 404 - Resource Not Found
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleNotFound(ResourceNotFoundException ex) {
        return buildResponse(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    // Handles 409 - Business Logic Conflicts (e.g. Already Active)
    @ExceptionHandler(HotelActionException.class)
    public ResponseEntity<Map<String, Object>> handleConflict(HotelActionException ex) {
        return buildResponse(ex.getMessage(), HttpStatus.CONFLICT);
    }

    // Handles 400 - Custom Business Validation
    @ExceptionHandler(InvalidInputException.class)
    public ResponseEntity<Map<String, Object>> handleBadRequest(InvalidInputException ex) {
        return buildResponse(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    // Handles 400 - Field Validation (@Valid)
    // This catches errors like empty names or invalid email formats
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", HttpStatus.BAD_REQUEST.value());
        body.put("message", "Validation Failed");
        body.put("fieldErrors", errors);
        
        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }

    // Handles 400 - Type Mismatch (e.g. passing a string into a Long ID)
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<Map<String, Object>> handleTypeMismatch(MethodArgumentTypeMismatchException ex) {
        String message = String.format("Parameter '%s' expects type '%s', but received '%s'", 
                ex.getName(), ex.getRequiredType().getSimpleName(), ex.getValue());
        return buildResponse(message, HttpStatus.BAD_REQUEST);
    }

    // Catch-all for 500 - Internal Server Error
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGlobalException(Exception ex) {
        // You should log the actual exception 'ex' here for debugging
        return buildResponse("An unexpected internal error occurred", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private ResponseEntity<Map<String, Object>> buildResponse(String message, HttpStatus status) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", status.value());
        body.put("message", message);
        return new ResponseEntity<>(body, status);
    }
}
