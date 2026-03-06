package com.tripease.booking.dto;

import lombok.Builder;

import java.math.BigDecimal;
import java.util.UUID;

@Builder
public record BookingResponseDTO(
        String bookingReference,
        String userId,
        Long resourceId,
        String status,
        BigDecimal totalAmount
) {}
