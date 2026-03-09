package com.tripease.booking.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class BookingAnalyticsDTO {
    private Long userId;
    private String type;
    private Double amount;
    private LocalDateTime bookingDate;
}
