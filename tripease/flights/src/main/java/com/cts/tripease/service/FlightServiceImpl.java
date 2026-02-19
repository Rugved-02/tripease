package com.cts.tripease.service;

import org.springframework.stereotype.Service;

import com.cts.tripease.dto.FlightRequestDTO;
import com.cts.tripease.exception.FlightNotFoundException;
import com.cts.tripease.exception.InsufficientSeatsException;
import com.cts.tripease.exception.InvalidFlightException;
import com.cts.tripease.exception.SeatNotFoundException;
import com.cts.tripease.model.Flight;
import com.cts.tripease.model.FlightSeat;
import com.cts.tripease.repo.FlightRepository;
import com.cts.tripease.repo.FlightSeatRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FlightServiceImpl implements FlightService {
    
    private final FlightRepository flightRepository;
    private final FlightSeatRepository seatRepository;

    // public FlightServiceImpl(FlightRepository flightRepository, FlightSeatRepository seatRepository) {
    //     this.flightRepository = flightRepository;
    //     this.seatRepository = seatRepository;
    // }
    @Override
    public Flight createFlight(FlightRequestDTO flightDTO) {
        Flight flight = new Flight();
        flight.setDepPlace(flightDTO.getDepPlace());
        flight.setArrPlace(flightDTO.getArrPlace());
        flight.setDepTime(flightDTO.getDepTime());
        flight.setAirline(flightDTO.getAirline());
    flight.setFlightNo(flightDTO.getFlightno());
        if (flightDTO.getDepTime() != null) {
        flight.setDepTime(flightDTO.getDepTime().truncatedTo(ChronoUnit.MINUTES));
    }
    if (flightDTO.getArrTime() != null) {
        flight.setArrTime(flightDTO.getArrTime().truncatedTo(ChronoUnit.MINUTES));
    }
       if (flight.getDepTime() != null && flight.getDepTime().isBefore(LocalDateTime.now())) {
            throw new InvalidFlightException("Cannot create a flight with a departure time in the past.");
        }
        if (flight.getDepPlace().equalsIgnoreCase(flight.getArrPlace())) {
            throw new InvalidFlightException("Departure and Arrival places cannot be the same.");
        }
        return flightRepository.save(flight);
    }
    @Override
    public List<Flight> searchFlights(String dep, String arr, LocalDateTime time) {
        List<Flight> flights = flightRepository.findByDepPlaceAndArrPlaceAndDepTimeAfter(dep, arr, time);
        if (flights.isEmpty()) {
            throw new FlightNotFoundException("No flights found from " + dep + " to " + arr + " after " + time);
        }
        return flights;
    }
    @Override
    public Flight getFlightById(String id) {
        return flightRepository.findById(id)
                .orElseThrow(() -> new FlightNotFoundException("Flight with ID " + id + " not found"));}

    @Override
    public List<FlightSeat> getSeatsByFlightId(String flightId) {
        List<FlightSeat> seats = seatRepository.findByFlightFlightId(flightId);
    if (seats.isEmpty()) {
        throw new SeatNotFoundException("No seats found for Flight ID " + flightId);
    }
    return seats;
}
    @Override
public FlightSeat addSeat(String flightId, FlightSeat seat) {
    Flight flight = flightRepository.findById(flightId)
            .orElseThrow(() -> new FlightNotFoundException("Flight " + flightId + " not found"));
    
    seat.setFlight(flight); 
    return seatRepository.save(seat);
}
@Override
@Transactional 
public FlightSeat bookSeat(Integer seatId, Integer count) {
    FlightSeat seat = seatRepository.findById(seatId)
        .orElseThrow(() -> new SeatNotFoundException("Seat category with ID " + seatId + " not found"));
        
    if (seat.getAvailableSeats() < count) {
        throw new InsufficientSeatsException("Insufficient seats available!");
    }

    seat.setAvailableSeats(seat.getAvailableSeats() - count);
    return seatRepository.save(seat);
}
}