package com.tripease.booking.dto;

import lombok.Builder;
import java.math.BigDecimal;
import java.time.LocalDate;

@Builder
public record BookingRequestDTO(
        String userId,
        Long resourceId,
        String resourceType, // "FLIGHT" or "HOTEL"
        String subType,     // "ECONOMY", "BUSINESS", "DELUXE",
        LocalDate startDate, // Check-in for Hotels and Departure of flight
        LocalDate endDate,   // Check-out for Hotels and null for flight
        int quantity,
        BigDecimal totalAmount
) {}
