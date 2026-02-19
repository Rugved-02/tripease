package com.cts.flightEntityTripease.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.cts.flightEntityTripease.model.Flight;
import com.cts.flightEntityTripease.repository.FlightDbRepository;

import lombok.AllArgsConstructor;

@Service("dbServiceImpl")
@AllArgsConstructor
public class FlightServiceDbImpl implements FlightService {
	
	FlightDbRepository flightDbRepository;

	@Override
	public List<Flight> getAllFlights() {
		// TODO Auto-generated method stub
		return flightDbRepository.findAll();
	}

	@Override
	public Flight getFlightById(int id) {
		// TODO Auto-generated method stub
		return flightDbRepository.findById(id).orElse(null);
	}

	@Override
	public Flight addFlight(Flight flight) {
		// TODO Auto-generated method stub
		return flightDbRepository.save(flight);
	}
	
	
}
