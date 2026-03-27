package com.cts.tripease.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;

@RestControllerAdvice // Intercepts exceptions globally
public class GlobalExceptionHandler {

    @ExceptionHandler(FlightNotFoundException.class)
    public ResponseEntity<ErrorDetails> handleFlightNotFound(FlightNotFoundException ex, WebRequest request) {
        ErrorDetails error = new ErrorDetails(
                LocalDateTime.now(), 
                ex.getMessage(), 
                request.getDescription(false));
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorDetails> handleGlobalException(Exception ex, WebRequest request) {
        ex.printStackTrace();
        ErrorDetails error = new ErrorDetails(
                LocalDateTime.now(), 
                "An internal server error occurred", 
                request.getDescription(false));
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }
    @ExceptionHandler(SeatNotFoundException.class)
public ResponseEntity<ErrorDetails> handleSeatNotFound(SeatNotFoundException ex, WebRequest request) {
    ErrorDetails error = new ErrorDetails(LocalDateTime.now(), ex.getMessage(), request.getDescription(false));
    return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
}

@ExceptionHandler(InsufficientSeatsException.class)
public ResponseEntity<ErrorDetails> handleInsufficientSeats(InsufficientSeatsException ex, WebRequest request) {
    ErrorDetails error = new ErrorDetails(LocalDateTime.now(), ex.getMessage(), request.getDescription(false));
    return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
}
@ExceptionHandler(InvalidFlightException.class)
public ResponseEntity<ErrorDetails> handleInvalidFlight(InvalidFlightException ex, WebRequest request) {
    ErrorDetails error = new ErrorDetails(LocalDateTime.now(), ex.getMessage(), request.getDescription(false));
    return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
}
}