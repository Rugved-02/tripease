package com.tripease.payment.repository;

import com.tripease.payment.model.Payment;
import com.tripease.payment.model.PaymentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Integer> {

    Optional<Payment> findByIdempotencyKey(String idempotencyKey);
    List<Payment> findAllByBookingId(String bookingId);
    Optional<Payment> findFirstByBookingIdOrderByCreatedAtDesc(String bookingId);
    boolean existsByBookingIdAndStatus(String bookingId, PaymentStatus paymentStatus);
}
