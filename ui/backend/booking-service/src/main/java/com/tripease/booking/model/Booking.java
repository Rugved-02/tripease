package com.tripease.booking.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;
@Entity
@Table(name = "bookings")
@Getter @Setter @Builder
@NoArgsConstructor @AllArgsConstructor
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "booking_id")
    private Long id;

    @Column(unique = true, nullable = false, updatable = false)
    private String bookingReference;

    @Column(nullable = false)
    private String userId;

    @Column(nullable = false)
    private Long resourceId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ResourceType resourceType;


    private String subType; // null for hotel and carrries economy, business value for flights

    @Enumerated(EnumType.STRING)
    private BookingStatus status;

    /**
     * NEW FIELDS FOR HOTEL SUPPORT
     */
    @Column(name = "start_date")
    private LocalDate startDate; // Hotel Check-in or Flight Departure

    @Column(name = "end_date")
    private LocalDate endDate;   // Hotel Check-out (Can be null for Flights)

    @Builder.Default
    private int quantity = 1;        // Number of rooms or seats booked

    @Column(precision = 19, scale = 2)
    private BigDecimal totalAmount;

    @CreationTimestamp
    @Column(updatable = false, nullable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(nullable = false)
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        if (this.bookingReference == null) {
            this.bookingReference = UUID.randomUUID().toString();
        }
        if (this.status == null) {
            this.status = BookingStatus.PENDING;
        }
    }
}