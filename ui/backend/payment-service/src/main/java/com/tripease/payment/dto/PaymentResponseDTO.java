package com.tripease.payment.dto;

import com.tripease.payment.model.PaymentStatus;
import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Builder
public record PaymentResponseDTO(
        Long id,
        String idempotencyKey,
        String bookingId,
        BigDecimal amount,
        PaymentStatus status,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        String displayMessage
) {}