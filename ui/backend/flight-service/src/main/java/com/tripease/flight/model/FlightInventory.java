package com.tripease.flight.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "flight_inventory")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FlightInventory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long inventoryId;

    @ManyToOne
    @JoinColumn(name = "flight_id")
    private Flight flight;

    private LocalDate travelDate; // The specific day of travel

    @Enumerated(EnumType.STRING)
    private ClassType classType; // Matches the template

    private Integer availableSeats; // Decrements on booking, increments on failure

    @Version
    private Integer version; // Optimistic locking to prevent overbooking 2 users on 1 seat
}
