package com.tripease.itinerary.dto;

import com.tripease.itinerary.model.ActivityType;
import lombok.Data;
import java.time.LocalDateTime;

@Data
public class ActivityResponseDTO {
    private Long activityId;
    private String title;
    private ActivityType type;
    private String location;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String notes;
    private Long tripId;
}
