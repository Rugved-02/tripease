package com.cts.tripease.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cts.tripease.model.FlightSeat;

import java.util.List;

public interface FlightSeatRepository extends JpaRepository<FlightSeat, Integer> {
    List<FlightSeat> findByFlightFlightId(String flightId);
}