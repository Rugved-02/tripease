package com.tripease.booking.dto.flight;

public record FlightRecentBookingResponseDTO(
        Long flightId,
        String flightNo,
        String airline,
        String depPlace,
        String arrPlace
) {
}
