package com.tripease.hotels.exception;

// Custom 400 Exception
public class InvalidInputException extends RuntimeException {
    public InvalidInputException(String message) {
        super(message);
    }
}