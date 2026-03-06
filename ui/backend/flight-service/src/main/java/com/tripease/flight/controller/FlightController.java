package com.tripease.flight.controller;

import com.tripease.flight.dto.*;
import com.tripease.flight.repo.FlightRepository;
import com.tripease.flight.service.FlightSearchService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.tripease.flight.service.FlightService;

import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@RequestMapping("/flight")
@RequiredArgsConstructor
@RestController
public class FlightController {

    private final FlightService flightService;
    private final FlightSearchService searchService;
    private final FlightRepository flightRepository;


    @GetMapping("/search")
    public ResponseEntity<List<FlightSearchResponseDTO>> search(
            @RequestParam String from,
            @RequestParam String to,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @RequestParam int passengers) {

        //List<FlightSearchResponseDTO> flights = searchService.searchAvailableFlights(from, to, date, passengers);
        log.info("Searching flights from {} to {} on {}", from, to, date);
        return ResponseEntity.ok(searchService.searchAvailableFlights(from, to, date, passengers));
    
    }

    @PostMapping("/add")
    public ResponseEntity<FlightResponseDTO> addFlight(@RequestBody FlightRequestDTO requestDTO) {
       log.info("Registering new flight: {}", requestDTO.flightNo());
        return new ResponseEntity<>(flightService.registerNewFlight(requestDTO), HttpStatus.CREATED);
    }

    @GetMapping("findAll/ids")
    public ResponseEntity<List<FlightRecentBookingResponseDTO>> getFlightsByIds(@RequestParam List<Long> flightIds){

        List<FlightRecentBookingResponseDTO> hotels = flightRepository.findFlightsByIds(flightIds);
        return ResponseEntity.ok(hotels);
    }
}