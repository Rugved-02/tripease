package com.cts.authService.exception;

public class UserAlreadyExistsException extends RuntimeException{
	public UserAlreadyExistsException(String message) {
		super(message);
	}
}
