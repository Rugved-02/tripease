package com.tripease.hotels.dto;

public record HotelRecentBookingResponseDTO(
        Long hotelId,
        String hotelName,
        String location
) {
}
