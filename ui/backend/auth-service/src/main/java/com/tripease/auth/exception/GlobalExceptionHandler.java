package com.tripease.auth.exception;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import lombok.extern.slf4j.Slf4j;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
	
	@ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<Object> handleNotFound(UserNotFoundException ex) {
		log.warn("User not found: {}", ex.getMessage()); // WARN: User-side error
        return buildResponse(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<Object> handleConflict(UserAlreadyExistsException ex) {
    	log.warn("Conflict error: {}", ex.getMessage());
        return buildResponse(ex.getMessage(), HttpStatus.CONFLICT);
    }
	
	@ExceptionHandler(InvalidOtpException.class)
	public ResponseEntity<Object> handleInvalidOtp(InvalidOtpException ex) {
		log.warn("Invalid OTP attempt: {}", ex.getMessage());
	    return buildResponse(ex.getMessage(), HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(OtpExpiredException.class)
	public ResponseEntity<Object> handleOtpExpired(OtpExpiredException ex) {
		log.warn("Expired OTP attempt: {}", ex.getMessage());
	    return buildResponse(ex.getMessage(), HttpStatus.GONE); 
	}

	@ExceptionHandler(jakarta.mail.MessagingException.class)
	public ResponseEntity<Object> handleMessagingException(jakarta.mail.MessagingException ex) {
	    // We use 500 Internal Server Error because this is a server/SMTP failure
		log.error("Mail service failure: ", ex);
	    return buildResponse("Failed to send email. Please check your connection or SMTP settings.", 
	                         HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	@ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Object> handleRuntimeException(RuntimeException ex) {	
		log.error("Generic RuntimeException occurred: {}", ex.getMessage());
        return buildResponse(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }
	
	@ExceptionHandler(Exception.class) // Catch-all for any other unhandled errors
    public ResponseEntity<Object> handleGlobalException(Exception ex) {
        log.error("UNEXPECTED SYSTEM ERROR: ", ex); 
        return buildResponse("An internal server error occurred.", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private ResponseEntity<Object> buildResponse(String message, HttpStatus status) {
        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("message", message);
        body.put("status", status.value());
        
        log.debug("Returning error response: [Status: {}] [Message: {}]", status.value(), message);
        
        return new ResponseEntity<>(body, status);
    }
}
