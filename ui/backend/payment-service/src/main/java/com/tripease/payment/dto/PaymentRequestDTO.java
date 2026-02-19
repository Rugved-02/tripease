package com.tripease.payment.dto;


import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
//import java.util.UUID;

public record PaymentRequestDTO(
        @NotBlank(message = "Idempotency key is required to prevent duplicate payments")
        String idempotencyKey,

        @NotBlank(message = "Booking ID is mandatory")
        String bookingId,

        @NotNull(message = "Payment amount cannot be null")
        @DecimalMin(value = "0.01", message = "Amount must be greater than zero")
        BigDecimal amount
) {}