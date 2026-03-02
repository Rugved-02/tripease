package com.tripease.hotels.dto;
import java.util.List;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class HotelResponseDTO {
    private String hotelName;
    private String location;
    private double basePrice;
    private List<String> amenities;
    private int totalRooms;
    private boolean isRegistered;
    private double averageRating;
    private int reviewCount;
}