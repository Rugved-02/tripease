package com.tripease.payment.model;


import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "payments", indexes = {
        @Index(name = "idx_idempotency_key", columnList = "idempotencyKey", unique = true),
        @Index(name = "idx_booking_id", columnList = "bookingId")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "payment_id")
    private Long id;

    // Idempotency Key: Client-generated UUID to prevent duplicate processing
    @Column(nullable = false, unique = true, length = 100)
    private String idempotencyKey;

    @Column(nullable = false)
    private String bookingId;

    @Column(nullable = false)
    private BigDecimal amount;

//    @Column(nullable = false, length = 3)/*
//    private String currency; // e.g., "USD", "INR"*/

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PaymentStatus status;

//    // Gateway Information (e.g., Stripe/Razorpay transaction ID)
//    private String providerTransactionId;
//
//    @Column(columnDefinition = "TEXT")
//    private String providerResponseRaw; // Store the JSON response for debugging

    @Version
    private Long version; // Optimistic Locking to prevent race conditions

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;


}
