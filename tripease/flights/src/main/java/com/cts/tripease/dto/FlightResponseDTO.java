package com.cts.tripease.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class FlightResponseDTO {
    private String flightId;
    private String depPlace;
    private String arrPlace;
    private LocalDateTime depTime;
    private String message; // Optional confirmation text
}
