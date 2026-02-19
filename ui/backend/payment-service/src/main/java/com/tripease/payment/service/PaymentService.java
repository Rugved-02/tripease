package com.tripease.payment.service;

import com.tripease.payment.dto.PaymentRequestDTO;
import com.tripease.payment.dto.PaymentResponseDTO;
import com.tripease.payment.model.Payment;

public interface PaymentService {
    PaymentResponseDTO processPayment(PaymentRequestDTO paymentRequestDTO);
    String getLatestPaymentStatusForBookingId(String bookingId);
}
