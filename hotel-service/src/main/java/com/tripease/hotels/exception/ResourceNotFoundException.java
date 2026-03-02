package com.tripease.hotels.exception;

//Custom 404 Exception
public class ResourceNotFoundException extends RuntimeException {
 public ResourceNotFoundException(String message) {
     super(message);
 }
}