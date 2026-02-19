package com.cts.flightEntityTripease.repository;

import java.util.List;

import com.cts.flightEntityTripease.model.Flight;

public interface FlightRepository {
	List<Flight> getAllFlights();
	Flight getFlightById(int id);
	Flight addFlight(Flight flight);
}
