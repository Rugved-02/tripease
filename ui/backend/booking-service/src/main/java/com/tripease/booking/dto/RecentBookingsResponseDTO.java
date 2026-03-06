package com.tripease.booking.dto;

import com.tripease.booking.model.BookingStatus;
import com.tripease.booking.model.ResourceType;
import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Builder
public record RecentBookingsResponseDTO(
        ResourceType resourceType,
        String subType,
        BookingStatus bookingStatus,
        LocalDate startDate,
        LocalDate endDate,
        BigDecimal totalAmount,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        ResourceDetailsDTO resourceDetails
        ) {}

