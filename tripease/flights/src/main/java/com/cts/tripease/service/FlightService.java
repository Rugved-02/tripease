package com.cts.tripease.service;

import java.time.LocalDateTime;
import java.util.List;

import com.cts.tripease.dto.FlightRequestDTO;
import com.cts.tripease.model.Flight;
import com.cts.tripease.model.FlightSeat;

public interface FlightService {
    List<Flight> searchFlights(String dep, String arr, LocalDateTime time);
    Flight getFlightById(String id);
    List<FlightSeat> getSeatsByFlightId(String flightId);
    Flight createFlight(FlightRequestDTO flightDTO);
    FlightSeat addSeat(String flightId, FlightSeat seat);
    FlightSeat bookSeat(Integer seatId, Integer count);
}