package com.cts.tripease.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cts.tripease.model.Flight;

import java.time.LocalDateTime;
import java.util.List;

public interface FlightRepository extends JpaRepository<Flight, String> {
    List<Flight> findByDepPlaceAndArrPlaceAndDepTimeAfter(String depPlace, String arrPlace, LocalDateTime depTime);
}