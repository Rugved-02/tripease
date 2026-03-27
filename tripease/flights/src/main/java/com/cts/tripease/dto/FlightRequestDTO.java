package com.cts.tripease.dto;

import lombok.Data;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

@Data
public class FlightRequestDTO {
    private String depPlace;
    private String arrPlace;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime depTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime arrTime;
    private String airline;  
    private String flightno;
}