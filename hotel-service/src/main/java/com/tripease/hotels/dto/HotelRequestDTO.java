package com.tripease.hotels.dto;

import java.util.List;

// Request DTO for creating/updating a hotel
public record HotelRequestDTO(
        String hotelName,
        String location,
        double basePrice,
        List<String> amenities,
        int totalRooms
) {}
