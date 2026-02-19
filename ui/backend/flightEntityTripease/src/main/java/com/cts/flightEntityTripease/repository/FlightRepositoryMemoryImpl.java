package com.cts.flightEntityTripease.repository;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.cts.flightEntityTripease.model.Flight;

@Repository
public class FlightRepositoryMemoryImpl implements FlightRepository {
	
	List<Flight> flightList;
	
	public FlightRepositoryMemoryImpl() {
		flightList = new ArrayList<Flight>();
		flightList.add(new Flight(101, "SkyBound 742", "JFK", "LHR"));
		flightList.add(new Flight(205, "Oceanic 815", "SYD", "LAX"));
		flightList.add(new Flight(312, "Global Express", "HND", "SIN"));
		flightList.add(new Flight(449, "Coastal Air", "SFO", "SEA"));
		flightList.add(new Flight(520, "Alpine Wing", "ZRH", "CDG"));
	}
	
	public List<Flight> getAllFlights(){
		
		return flightList;
	}

	@Override
	public Flight getFlightById(int id) {
		// TODO Auto-generated method stub
		return flightList.stream().filter(flt -> (flt.getId() == id)).findFirst().orElse(null);
	}

	@Override
	public Flight addFlight(Flight flight) {
		// TODO Auto-generated method stub
		flightList.add(flight);
		return flight;
	}

}
