package com.tripease.payment.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "payment_outbox")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OutboxEvent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "outbox_id")
    private Long id;

    private String aggregateId; // The Booking ID
    private String eventType;   // e.g., "PAYMENT_CONFIRMED"

    @Column(columnDefinition = "TEXT")
    private String payload;     // JSON data: { "bookingId": "123", "amount": 500 }

    private String status;      // PENDING, PROCESSED, FAILED

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @Builder.Default            // it sets the builder deafult value to 0 if we use builder to build OutboxEvent object
    private Integer retryCount = 0;
}
