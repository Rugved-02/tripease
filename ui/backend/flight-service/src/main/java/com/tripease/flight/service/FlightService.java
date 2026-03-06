package com.tripease.flight.service;

import com.tripease.flight.dto.FlightRequestDTO;
import com.tripease.flight.dto.FlightResponseDTO;

public interface FlightService {

//    List<Flight> getAllFlights();
//    Flight getFlightById(Long id);
//    List<FlightSeat> getSeatsByFlightId(Long flightId);

FlightResponseDTO registerNewFlight(FlightRequestDTO dto);

}