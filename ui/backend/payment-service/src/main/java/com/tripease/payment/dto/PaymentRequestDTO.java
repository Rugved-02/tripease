package com.tripease.payment.dto;


import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

import java.math.BigDecimal;
import java.util.UUID;
//import java.util.UUID;

public record PaymentRequestDTO(
        @NotBlank(message = "Idempotency key is required to prevent duplicate payments")
        @Pattern(
                regexp = "^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$",
                message = "Idempotency-Key must be a valid UUID format"
        )
        String idempotencyKey,

        @NotBlank(message = "Booking ID is mandatory")
        String bookingId,

        @NotNull(message = "Payment amount cannot be null")
        @DecimalMin(value = "0.01", message = "Amount must be greater than zero")
        BigDecimal amount
) {}