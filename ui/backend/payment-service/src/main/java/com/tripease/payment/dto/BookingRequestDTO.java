package com.tripease.payment.dto;

import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record BookingRequestDTO(
        String userId,
        Long resourceId,
        String resourceType, // Expecting "FLIGHT" or "HOTEL"
        BigDecimal totalAmount
) {}
