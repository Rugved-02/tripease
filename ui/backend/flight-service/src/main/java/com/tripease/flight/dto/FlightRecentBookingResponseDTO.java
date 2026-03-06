package com.tripease.flight.dto;

public record FlightRecentBookingResponseDTO(
        Long flightId,
        String flightNo,
        String airline,
        String depPlace,
        String arrPlace
) {
}
