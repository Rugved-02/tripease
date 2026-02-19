package com.cts.flightEntityTripease.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cts.flightEntityTripease.model.Flight;

public interface FlightDbRepository extends JpaRepository<Flight, Integer>{

}
