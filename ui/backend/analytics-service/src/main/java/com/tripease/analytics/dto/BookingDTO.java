package com.tripease.analytics.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class BookingDTO {
    private Long userId;
    private String type;
    private Double amount;
    private LocalDateTime bookingDate;
}
