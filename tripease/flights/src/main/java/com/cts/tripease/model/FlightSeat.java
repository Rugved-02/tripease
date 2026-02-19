package com.cts.tripease.model;

import jakarta.persistence.*;
import lombok.Data;
import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "FLIGHT_SEATS")
@Data
public class FlightSeat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer seatId;

    @ManyToOne
    @JoinColumn(name = "flightId")
    @JsonIgnore
    private Flight flight;

    @Enumerated(EnumType.STRING)
    private ClassType classType; // e.g., ECONOMY, BUSINESS

    private BigDecimal price;
    private Integer availableSeats;
}

enum ClassType {
    ECONOMY, BUSINESS, FIRST
}