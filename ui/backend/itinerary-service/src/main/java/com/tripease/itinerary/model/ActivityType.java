package com.tripease.itinerary.model;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum ActivityType {
    FLIGHT,
    HOTEL,
    LOCATION,   // For location markers in the timeline
    SIGHTSEEING,
    RESTAURANT,
    TRANSPORT,
    WORK,
    OTHER;

    @JsonCreator
    public static ActivityType fromString(String value) {
        if (value == null || value.isBlank()) return null;
        return ActivityType.valueOf(value.toUpperCase());
    }
}
