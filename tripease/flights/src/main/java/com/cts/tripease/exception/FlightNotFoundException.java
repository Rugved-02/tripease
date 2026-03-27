package com.cts.tripease.exception; 

// Custom exception for business logic errors
public class FlightNotFoundException extends RuntimeException {
    public FlightNotFoundException(String message) {
        super(message);
    }
}