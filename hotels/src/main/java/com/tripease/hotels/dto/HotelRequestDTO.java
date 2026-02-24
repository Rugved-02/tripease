package com.tripease.hotels.dto;
import java.util.List;

import lombok.Data;

@Data
public class HotelRequestDTO {
    private String hotelName;
    private String location;
    private double basePrice;
    private List<String> amenities;
    private int totalRooms;
}