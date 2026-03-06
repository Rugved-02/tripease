package com.tripease.booking.dto.hotel;

import lombok.Getter;
import lombok.Setter;

public record HotelRecentBookingResponseDTO(
        Long hotelId,
        String hotelName,
        String location
) {
}
