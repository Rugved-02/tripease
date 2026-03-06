package com.tripease.itinerary.model;


import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Table(name = "activity_details")
@Data
public class ActivityDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long activityId; // Primary Key

    private String title; // Maps to 'title' in schema

    @Enumerated(EnumType.STRING)
    private ActivityType type;

    private String location; // Maps to 'location'

    private LocalDateTime startTime; // Maps to 'startTime' datetime

    private LocalDateTime endTime; // Maps to 'endTime' datetime

    @Column(columnDefinition = "TEXT",nullable = true)
    private String notes; // Maps to 'notes' text

    @Column(name = "booking_id", nullable = true) // Explicitly allow null in DB
    private String bookingId;

    @Column(name = "is_enabled", nullable = false)
    private Boolean isEnabled = true;


    // The JPA relationship (Internal logic)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "trip_id", nullable = false) // Ensures every activity MUST belong to a trip
    @com.fasterxml.jackson.annotation.JsonIgnore
    private Trip trip;

    // The Reference Field (API/JSON logic)
    @com.fasterxml.jackson.annotation.JsonProperty("tripId")
    public Long getTripId() {
        return (this.trip != null) ? this.trip.getTripId() : null;
    }
}