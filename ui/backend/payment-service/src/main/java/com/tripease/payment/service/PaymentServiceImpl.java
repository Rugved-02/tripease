package com.tripease.payment.service;

import com.tripease.payment.client.BookingServiceClient;
import com.tripease.payment.dto.PaymentRequestDTO;
import com.tripease.payment.dto.PaymentResponseDTO;
import com.tripease.payment.model.OutboxEvent;
import com.tripease.payment.model.Payment;
import com.tripease.payment.model.PaymentStatus;
import com.tripease.payment.repository.OutboxRepository;
import com.tripease.payment.repository.PaymentRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService{

    private final PaymentRepository paymentRepository;
    private final OutboxRepository outboxRepository;
    private final BookingServiceClient bookingServiceClient;


    @Override
    @Transactional
    public PaymentResponseDTO processPayment(PaymentRequestDTO paymentRequestDTO) {
        // 1. Check if this exact idempotency key has been used before
        return paymentRepository.findByIdempotencyKey(paymentRequestDTO.idempotencyKey())
                .map(existing -> {
                    log.info("Idempotency match found for key: {}", paymentRequestDTO.idempotencyKey());
                    return mapToPaymentResponseDTO(existing, "Returning existing payment record.");
                })
                .orElseGet(() -> {
                    // 2. If key is new, check if the Booking already has a SUCCESSFUL payment
                    // This prevents creating a second PENDING record for the same booking
                    Optional<Payment> lastPayment = paymentRepository.findFirstByBookingIdOrderByCreatedAtDesc(paymentRequestDTO.bookingId());

                    if (lastPayment.isPresent() && lastPayment.get().getStatus() == PaymentStatus.CONFIRMED) {
                        return mapToPaymentResponseDTO(lastPayment.get(), "Booking already confirmed via another session.");
                    }

                    log.info("Creating new PENDING payment for booking: {}", paymentRequestDTO.bookingId());
                    Payment newPayment = Payment.builder()
                            .bookingId(paymentRequestDTO.bookingId())
                            .amount(paymentRequestDTO.amount())
                            .idempotencyKey(paymentRequestDTO.idempotencyKey())
                            .status(PaymentStatus.PENDING)
                            .build();

                    return mapToPaymentResponseDTO(paymentRepository.save(newPayment), "Payment Initialized");
                });
    }





    @Override
    @Transactional
    public PaymentResponseDTO payNow(String idempotencyKey) {
        // 1. Fetch current record with a lock to prevent race conditions
        Payment currentPayment = paymentRepository.findByIdempotencyKey(idempotencyKey)
                .orElseThrow(() -> new RuntimeException("Payment record not found for key: " + idempotencyKey));

        // 2. Guard: If THIS specific request is already finished, return it
        if (currentPayment.getStatus() != PaymentStatus.PENDING) {
            return mapToPaymentResponseDTO(currentPayment, "This specific request was already processed.");
        }

        // 3. CROSS-CHECK: Check for other records with the same Booking ID
        // Logic: If another idempotency key for this booking already succeeded,
        // we must NOT process this one.
        List<Payment> allPaymentsForBooking = paymentRepository.findAllByBookingId(currentPayment.getBookingId());

        boolean alreadySucceeded = allPaymentsForBooking.stream()
                .anyMatch(p -> p.getStatus() == PaymentStatus.CONFIRMED);

        if (alreadySucceeded) {
                currentPayment.setStatus(PaymentStatus.FAILED);
            paymentRepository.save(currentPayment);
            return mapToPaymentResponseDTO(currentPayment, "Booking already paid via another request.");
        }

        // 4. Handle failed cases from other keys (Optional/Context Dependent)
        // If other keys failed, we proceed with this one.

        // 5. EXTERNAL CALL: Mock Gateway
        PaymentStatus resultStatus = mockGatewayPaymentStatus();
        currentPayment.setStatus(resultStatus);

        // 6. ATOMIC SAVE
        Payment savedPayment = paymentRepository.save(currentPayment);

//        // 7. RELIABLE NOTIFICATION
//        if (savedPayment.getStatus() == PaymentStatus.CONFIRMED) {
//            OutboxEvent eventPayment = OutboxEvent.builder()
//                    .aggregateId(savedPayment.getBookingId())
//                    .eventType("PAYMENT_CONFIRMED")
//                    .payload(String.format("{\"bookingId\":\"%s\"}", savedPayment.getBookingId()))
//                    .status("PENDING")
////                    .retryCount(0)
//                    .build();
//
//            outboxRepository.save(eventPayment);
//        }


            OutboxEvent eventPayment = OutboxEvent.builder()
                    .aggregateId(savedPayment.getBookingId())
                    .eventType(savedPayment.getStatus() == PaymentStatus.CONFIRMED?"PAYMENT_CONFIRMED":"PAYMENT_FAILED")
                    .payload(String.format("{\"bookingId\":\"%s\"}", savedPayment.getBookingId()))
                    .status("PENDING")
//                    .retryCount(0)
                    .build();

            outboxRepository.save(eventPayment);


        return mapToPaymentResponseDTO(savedPayment,
                resultStatus == PaymentStatus.CONFIRMED ? "Payment Successful!" : "Payment Failed.");
    }


    //    choosing random status as we are not directly using payment gateway so we will get different payment status everytime we save payment to db
    @Override
    @Transactional
    public String getLatestPaymentStatusForBookingId(String bookingId) {

//        handle NoSuchElementException if bookingId doesn't exist
        Optional<Payment> paymentStatus = paymentRepository.findFirstByBookingIdOrderByCreatedAtDesc(bookingId);
        log.info("getPaymentStatus() executed");

        return paymentStatus.map(payment -> payment.getStatus().name()).orElse("STATUS : NOT FOUND");
    }



    private PaymentStatus mockGatewayPaymentStatus(){

        //it maps the chance such that CONFIRMED comes 80% of the times

        double chance = ThreadLocalRandom.current().nextDouble(); // Generates 0.0 to 1.0

        PaymentStatus randomStatus = (chance < 0.8)
                ? PaymentStatus.CONFIRMED
                : PaymentStatus.FAILED;

        log.info("Random Payment Status : "+String.valueOf(randomStatus));
        return randomStatus;
    }

    private PaymentResponseDTO mapToPaymentResponseDTO(Payment p, String msg) {
        return PaymentResponseDTO.builder()
                .id(p.getId())
                .idempotencyKey(p.getIdempotencyKey())
                .bookingId(p.getBookingId())
                .amount(p.getAmount())
                .status(p.getStatus())
                .createdAt(p.getCreatedAt())
                .updatedAt(p.getUpdatedAt())
                .displayMessage(msg)
                .build();
    }
}
