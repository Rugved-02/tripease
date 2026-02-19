package com.cts.flightEntityTripease.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.cts.flightEntityTripease.model.Flight;
import com.cts.flightEntityTripease.repository.FlightRepository;

import lombok.AllArgsConstructor;

@Service("memoryServiceImpl")
@AllArgsConstructor
public class FlightServiceMemoryImpl implements FlightService {
	
	FlightRepository flightRepository;

	@Override
	public List<Flight> getAllFlights() {
		// TODO Auto-generated method stub
		return flightRepository.getAllFlights();
	}

	@Override
	public Flight getFlightById(int id) {
		// TODO Auto-generated method stub
		return flightRepository.getFlightById(id);
	}

	@Override
	public Flight addFlight(Flight flight) {
		// TODO Auto-generated method stub
		return flightRepository.addFlight(flight);
	}
	
	
}
