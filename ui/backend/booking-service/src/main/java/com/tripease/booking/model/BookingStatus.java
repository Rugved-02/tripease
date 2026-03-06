package com.tripease.booking.model;

public enum BookingStatus {
    PENDING,    // Payment not yet confirmed
    CONFIRMED,  // Successfully booked
    CANCELLED,
    EXPIRED
}
