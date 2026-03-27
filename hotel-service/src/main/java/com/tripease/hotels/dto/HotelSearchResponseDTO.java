package com.tripease.hotels.dto;

import com.tripease.hotels.model.Rating;
import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Builder
public record HotelSearchResponseDTO(
        String hotelName,
        String location,
        List<String> amenities,
        BigDecimal price,
        List<Rating> ratings,
        Integer totalRooms,
        Double averageRating
) {}
