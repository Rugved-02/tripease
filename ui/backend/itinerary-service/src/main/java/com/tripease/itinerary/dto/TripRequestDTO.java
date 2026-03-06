package com.tripease.itinerary.dto;

import lombok.Data;
import java.time.LocalDate;
import java.util.List;

@Data
public class TripRequestDTO {
    private String tripName;
    private LocalDate startDate;
    private LocalDate endDate;
    private String userId; // UUID as String
    private List<ActivityRequestDTO> activities;
}