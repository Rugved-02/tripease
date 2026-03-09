package com.tripease.booking.client;

import com.tripease.booking.dto.flight.FlightRecentBookingResponseDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.util.List;

@FeignClient(name = "flight-service", path = "/flight")
public interface FlightServiceClient {

    @PutMapping("/inventory/reserve")
    ResponseEntity<String> reserveSeats(
            @RequestParam("id") Long id,
            @RequestParam("classType") String classType,
            @RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @RequestParam("qty") int qty
    );

    @PutMapping("/inventory/release")
    ResponseEntity<String> releaseSeats(
            @RequestParam("id") Long id,
            @RequestParam("classType") String classType,
            @RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @RequestParam("qty") int qty
    );

    @GetMapping("/findAll/ids")
    public ResponseEntity<List<FlightRecentBookingResponseDTO>> getFlightsByIds( @RequestParam List<Long> flightIds);

    @GetMapping("{flightId}")
    public ResponseEntity<FlightRecentBookingResponseDTO> getFlightById(@PathVariable Long flightId);
}
