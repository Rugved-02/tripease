package com.tripease.flight.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class FlightOperationException extends RuntimeException {
    public FlightOperationException(String message) {
        super(message);
    }
}