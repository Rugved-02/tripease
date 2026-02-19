package com.tripease.backend.model;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum ActivityType {
    FLIGHT,
    HOTEL,
    SIGHTSEEING,
    RESTAURANT,
    TRANSPORT,
    WORK,  // <-- Added WORK here!
    OTHER;

    @JsonCreator
    public static ActivityType fromString(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }
        return ActivityType.valueOf(value.toUpperCase());
    }
}