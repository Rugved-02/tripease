package com.tripease.flight.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tripease.flight.model.FlightSeat;

import java.util.List;

public interface FlightSeatRepository extends JpaRepository<FlightSeat, Long> {
    List<FlightSeat> findByFlightFlightId(Long flightId);
}