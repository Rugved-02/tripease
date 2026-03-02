package com.tripease.hotels.exception;

// Custom 409 Exception
public class HotelActionException extends RuntimeException {
    public HotelActionException(String message) {
        super(message);
    }
}
