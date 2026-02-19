package com.tripease.payment.service;

import com.tripease.payment.dto.PaymentRequestDTO;
import com.tripease.payment.dto.PaymentResponseDTO;
import com.tripease.payment.model.Payment;
import com.tripease.payment.model.PaymentStatus;
import com.tripease.payment.repository.PaymentRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Random;

@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService{

    private final PaymentRepository paymentRepository;

    @Override
    @Transactional
    public PaymentResponseDTO processPayment(PaymentRequestDTO paymentRequestDTO) {


        Optional<Payment> idempotenceKeyExists = paymentRepository.findByIdempotencyKey(paymentRequestDTO.idempotencyKey());

        if( idempotenceKeyExists.isPresent()){
//            logic to return the existing record with same idempotency key in db
//            it prevents DataIntegrityViolationException
            Payment existingPayment = idempotenceKeyExists.get();

            log.info("Idempotency Key Exists :: Existing DB Record Returned");

            String msg = "Booking Already Exists with Payment Status - " + existingPayment.getStatus();
            return mapToPaymentResponseDTO(existingPayment, msg);

        }
        else {
//            checking if bookingId exists with CONFIRMED status
            if(paymentRepository.existsByBookingIdAndStatus(paymentRequestDTO.bookingId(), PaymentStatus.CONFIRMED)){
                log.info("Booking Id Exists -- Status - Confirmed :: Existing DB Record Returned");
                return PaymentResponseDTO.builder()
                        .displayMessage("Payment Already (CONFIRMED) for your Booking : "+paymentRequestDTO.bookingId())
                        .build();
            }
//            checking if bookingId exists with PENDING status if not found with CONFIRMED
            else if (paymentRepository.existsByBookingIdAndStatus(paymentRequestDTO.bookingId(), PaymentStatus.PENDING)) {
                log.info("Booking Id Exists -- Status - Pending :: Existing DB Record Returned");
                return PaymentResponseDTO.builder()
                        .displayMessage("Payment In Process (PENDING) for your Booking : "+paymentRequestDTO.bookingId())
                        .build();
            }
//            creating new record in db for if there is no CONFIRMED or PENDING status ;; or if bookingID is new
            else {

                log.info("New Booking Id or Booking Id Exists with -- Status - Failed :: New DB Record Created and Returned");


                Payment newPayment = Payment.builder()
                        .idempotencyKey(paymentRequestDTO.idempotencyKey())
                        .bookingId(paymentRequestDTO.bookingId())
                        .amount(paymentRequestDTO.amount())
                        .status(PaymentStatus.PENDING)
                        .build();

                Payment savedPayment = paymentRepository.save(newPayment);

                //        Returning response using PaymentResponseDTO
                String msg = "New Booking Saved with Payment Status  - " + savedPayment.getStatus();
                return mapToPaymentResponseDTO(savedPayment,msg);

            }

        }

    }

    @Override
    @Transactional
    public String getLatestPaymentStatusForBookingId(String bookingId) {

//        handle NoSuchElementException if bookingId doesn't exist
        Optional<Payment> paymentStatus = paymentRepository.findFirstByBookingIdOrderByCreatedAtDesc(bookingId);
        log.info("getPaymentStatus() executed");

        return paymentStatus.map(payment -> payment.getStatus().name()).orElse("STATUS : NOT FOUND");
    }

    public PaymentResponseDTO payNow(String idempotencyKey){
        Payment payment = paymentRepository.findByIdempotencyKey(idempotencyKey)
                .orElseThrow(() -> new RuntimeException("Payment record not found"));

        // Guard: Only PENDING payments can be confirmed
        if (!payment.getStatus().equals(PaymentStatus.PENDING)) {
            return mapToPaymentResponseDTO(payment, "Payment already processed.");
        }

        // SIMULATION: In a real app, this is where you'd call the Stripe/Bank API
        payment.setStatus(PaymentStatus.CONFIRMED);
        Payment savedPayment = paymentRepository.save(payment);

        // CRITICAL: Notify Flight Service via FeignClient or RestTemplate
//        try {
//            flightServiceClient.confirmBooking(payment.getBookingId());
//            log.info("Flight Service notified for Booking: {}", payment.getBookingId());
//        } catch (Exception e) {
//            log.error("Failed to notify Flight Service. Need Manual Intervention or Retry Queue.");
//            // In a pro setup, you'd use Kafka here to ensure the message eventually arrives
//        }

        return mapToPaymentResponseDTO(savedPayment, "Payment Successful! Flight Booked.");

    }


//    choosing random status as we are not directly using payment gateway so we will get different payment status everytime we save payment to db

    private PaymentStatus getRandomPaymentStatus(){
        PaymentStatus[] statuses = PaymentStatus.values();
        PaymentStatus randomStatus = statuses[new Random().nextInt(statuses.length)];

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
