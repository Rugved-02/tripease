package com.cts.authService.exception;

public class UserNotFoundException extends RuntimeException {
	public UserNotFoundException(String message) {
        super(message);
    }
}
