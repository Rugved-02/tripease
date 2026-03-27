package com.cts.authService.exception;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import jakarta.mail.MessagingException;

@RestControllerAdvice
public class GlobalExceptionHandler {
	
	@ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<Object> handleNotFound(UserNotFoundException ex) {
        return buildResponse(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<Object> handleConflict(UserAlreadyExistsException ex) {
        return buildResponse(ex.getMessage(), HttpStatus.CONFLICT);
    }

	@ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Object> handleRuntimeException(RuntimeException ex) {
        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("message", ex.getMessage());
        body.put("status", HttpStatus.BAD_REQUEST.value());
       
        
        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }
	
	@ExceptionHandler(InvalidOtpException.class)
	public ResponseEntity<Object> handleInvalidOtp(InvalidOtpException ex) {
	    return buildResponse(ex.getMessage(), HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(OtpExpiredException.class)
	public ResponseEntity<Object> handleOtpExpired(OtpExpiredException ex) {
	    return buildResponse(ex.getMessage(), HttpStatus.GONE); 
	}

	@ExceptionHandler(jakarta.mail.MessagingException.class)
	public ResponseEntity<Object> handleMessagingException(jakarta.mail.MessagingException ex) {
	    // We use 500 Internal Server Error because this is a server/SMTP failure
	    return buildResponse("Failed to send email. Please check your connection or SMTP settings.", 
	                         HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	

    private ResponseEntity<Object> buildResponse(String message, HttpStatus status) {
        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("message", message);
        body.put("status", status.value());
        return new ResponseEntity<>(body, status);
    }
}
