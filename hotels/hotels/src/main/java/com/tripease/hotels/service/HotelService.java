package com.tripease.hotels.service;

import com.tripease.hotels.dto.*;
import com.tripease.hotels.model.*;
import com.tripease.hotels.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class HotelService {

    private final HotelRepository hotelRepo;
    private final RatingRepository ratingRepo;

    public void addHotel(HotelRequestDTO dto) {
        Hotel hotel = Hotel.builder()
                .hotelName(dto.getHotelName())
                .location(dto.getLocation())
                .basePrice(dto.getBasePrice())
                .amenities(dto.getAmenities())
                .totalRooms(dto.getTotalRooms())
                .isRegistered(true)
                .build();
        hotelRepo.save(hotel);
    }

    public List<HotelResponseDTO> getAllHotels() {
        return hotelRepo.findAll().stream()
                .map(this::mapToHotelResponse)
                .collect(Collectors.toList());
    }

    public List<HotelResponseDTO> searchByLocation(String location) {
        return hotelRepo.findByLocationContainingIgnoreCase(location).stream()
                .map(this::mapToHotelResponse)
                .collect(Collectors.toList());
    }

    public HotelResponseDTO getHotelById(int id) {
        return hotelRepo.findById(id)
                .map(this::mapToHotelResponse)
                .orElseThrow(() -> new NoSuchElementException("Hotel not found with ID: " + id));
    }

    public void deactivateHotel(int id) {
        Hotel hotel = hotelRepo.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Hotel not found with ID: " + id));
        
        if (!hotel.isRegistered()) {
            throw new IllegalStateException("Hotel is already deactivated.");
        }
        
        hotel.setRegistered(false);
        hotelRepo.save(hotel);
    }

    public void activateHotel(int hotelId) {
        Hotel hotel = hotelRepo.findById(hotelId)
                .orElseThrow(() -> new NoSuchElementException("Hotel not found with ID: " + hotelId));
        
        if (hotel.isRegistered()) {
            throw new IllegalStateException("Hotel is already active.");
        }
        
        hotel.setRegistered(true);
        hotelRepo.save(hotel);
    }

    public List<RatingResponseDTO> getRatingsForHotel(int hotelId) {
        // Validation: Check if hotel exists before fetching ratings
        if (!hotelRepo.existsById(hotelId)) {
            throw new NoSuchElementException("Hotel not found with ID: " + hotelId);
        }

        return ratingRepo.findByHotelHotelId(hotelId).stream()
                .map(rating -> RatingResponseDTO.builder()
                        .ratingId(rating.getRatingId())
                        .userId(rating.getUserId())
                        .ratingValue(rating.getRatingValue())
                        .reviewText(rating.getReviewText())
                        .build())
                .collect(Collectors.toList());
    }

    public void addRating(int hotelId, RatingRequestDTO dto) {
        if (dto.getRatingValue() < 1 || dto.getRatingValue() > 5) {
            throw new IllegalArgumentException("Rating value must be between 1 and 5.");
        }

        Hotel hotel = hotelRepo.findById(hotelId)
                .orElseThrow(() -> new NoSuchElementException("Hotel not found with ID: " + hotelId));
        
        Rating rating = Rating.builder()
                .userId(dto.getUserId())
                .ratingValue(dto.getRatingValue())
                .reviewText(dto.getReviewText())
                .hotel(hotel)
                .build();
        ratingRepo.save(rating);
    }

    private HotelResponseDTO mapToHotelResponse(Hotel hotel) {
        return HotelResponseDTO.builder()
                .hotelId(hotel.getHotelId())
                .hotelName(hotel.getHotelName())
                .location(hotel.getLocation())
                .basePrice(hotel.getBasePrice())
                .amenities(hotel.getAmenities())
                .totalRooms(hotel.getTotalRooms())
                .isRegistered(hotel.isRegistered())
                .build();
    }
}