package com.tripease.flight.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "flight_seats")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FlightSeat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seatId;

    @ManyToOne
    @JoinColumn(name = "flight_id")
    @JsonIgnore
    private Flight flight;

    @Enumerated(EnumType.STRING)
    private ClassType classType; // ECONOMY, BUSINESS, FIRST

    private BigDecimal basePrice;

    private Integer totalCapacity; // e.g., 150 for Economy
}