package com.cts.tripease.controller;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.cts.tripease.dto.FlightRequestDTO;
import com.cts.tripease.model.Flight;
import com.cts.tripease.model.FlightSeat;
import com.cts.tripease.service.FlightService;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/flights")
public class FlightController {

    private final FlightService flightService;

    public FlightController(FlightService flightService) {
        this.flightService = flightService;
    }

    @PostMapping
    public ResponseEntity<Flight> addFlight(@RequestBody FlightRequestDTO flightDTO) {
        Flight savedFlight = flightService.createFlight(flightDTO);
        return new ResponseEntity<>(savedFlight, HttpStatus.CREATED);
    }

    @PostMapping("/{flightId}/seats")
    public ResponseEntity<FlightSeat> addSeat(@PathVariable String flightId, @RequestBody FlightSeat seat) {
        FlightSeat savedSeat = flightService.addSeat(flightId, seat);
        return new ResponseEntity<>(savedSeat, HttpStatus.CREATED);
    }

    @PutMapping("/seats/{seatId}/book")
    public ResponseEntity<FlightSeat> bookSeat(
            @PathVariable Integer seatId, 
            @RequestParam(defaultValue = "1") Integer count) {
        FlightSeat updatedSeat = flightService.bookSeat(seatId, count);
        return ResponseEntity.ok(updatedSeat);
    }

    @GetMapping
    public ResponseEntity<List<Flight>> search(
            @RequestParam String depPlace,
            @RequestParam String arrPlace,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm") LocalDateTime depTime) {
        List<Flight> results = flightService.searchFlights(depPlace, arrPlace, depTime);
        return ResponseEntity.ok(results);
    }

    @GetMapping("/{flightId}")
    public ResponseEntity<Flight> getDetails(@PathVariable String flightId) {
        Flight flight = flightService.getFlightById(flightId);
        return ResponseEntity.ok(flight);
    }

    @GetMapping("/{flightId}/seats")
    public ResponseEntity<List<FlightSeat>> getSeats(@PathVariable String flightId) {
        List<FlightSeat> seats = flightService.getSeatsByFlightId(flightId);
        return ResponseEntity.ok(seats);
    }
}