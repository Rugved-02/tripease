package com.tripease.itinerary.dto;

import com.tripease.itinerary.model.ActivityType;
import lombok.Data;
import java.time.ZonedDateTime;

@Data
public class ActivityRequestDTO {
    private String title;
    private ActivityType type;//dropdown from frontend
    private String location;
    private ZonedDateTime startTime; // Captured based on specific location timezone
    private ZonedDateTime endTime;
    private String notes;
    private Long tripId; // Required to map activity to a trip
}