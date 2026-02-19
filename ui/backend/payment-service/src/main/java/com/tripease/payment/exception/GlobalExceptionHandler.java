package com.tripease.payment.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;
import java.util.NoSuchElementException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(NoSuchElementException.class)
    public ErrorResponse handleNoSuchElementException(NoSuchElementException ex){
        return ErrorResponse.builder(ex, HttpStatus.NOT_FOUND, "No Entry Found for Booking Id")
                .title("error-not found")
                .property("timestamp", Instant.now())
                .build();
    }

//    @ExceptionHandler(NoSuchElementException.class)
//    public ProblemDetail handleNoSuchElementException(NoSuchElementException ex) {
//        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, "No Entry Found for Booking Id via Problem Detail");
//        problemDetail.setProperty("name","kartik");
//        return problemDetail;
//    }

//    @ExceptionHandler(MethodArgumentNotValidException.class)
//    public String handleMethodArgumentNotValidException(MethodArgumentNotValidException ex){
//        return ex+"isisf";
//    }
    @ExceptionHandler(Exception.class)
    public ErrorResponse handleLeftException(Exception ex){

        return ErrorResponse.builder(ex, HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error")
                .build();
    }
}
