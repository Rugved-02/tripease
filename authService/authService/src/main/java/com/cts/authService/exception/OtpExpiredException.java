package com.cts.authService.exception;

public class OtpExpiredException extends RuntimeException {
	public OtpExpiredException(String message) { 
		super(message); 
		}
}
