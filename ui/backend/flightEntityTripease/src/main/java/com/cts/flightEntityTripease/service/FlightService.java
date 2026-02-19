package com.cts.flightEntityTripease.service;

import java.util.List;

import com.cts.flightEntityTripease.model.Flight;

public interface FlightService {
	List<Flight> getAllFlights();
	Flight getFlightById(int id);
	Flight addFlight(Flight flight);
}
