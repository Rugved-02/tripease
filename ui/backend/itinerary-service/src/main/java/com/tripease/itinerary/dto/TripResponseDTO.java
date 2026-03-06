package com.tripease.itinerary.dto;

import lombok.Data;
import java.time.LocalDate;
import java.util.List;

@Data
public class TripResponseDTO {
    private Long tripId;
    private String tripName;
    private LocalDate startDate;
    private LocalDate endDate;
    private List<ActivityResponseDTO> activities;
}
