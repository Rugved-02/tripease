package com.tripease.hotels.service;

import com.tripease.hotels.dto.*;
import com.tripease.hotels.model.*;
import com.tripease.hotels.repository.*;
import com.tripease.hotels.exception.*; // Import your custom exceptions
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
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

    public HotelResponseDTO getHotelById(String hotelId) {
        return hotelRepo.findById(hotelId)
                .map(this::mapToHotelResponse)
                // MODIFIED: Use ResourceNotFoundException
                .orElseThrow(() -> new ResourceNotFoundException("Hotel not found with ID: " + hotelId));
    }

    public void deactivateHotel(String hotelId) {
        Hotel hotel = hotelRepo.findById(hotelId)
                .orElseThrow(() -> new ResourceNotFoundException("Hotel not found with ID: " + hotelId));
        
        if (!hotel.isRegistered()) {
            // MODIFIED: Use HotelActionException for 409 Conflict
            throw new HotelActionException("Hotel is already deactivated.");
        }
        
        hotel.setRegistered(false);
        hotelRepo.save(hotel);
    }

    public void activateHotel(String hotelId) {
        Hotel hotel = hotelRepo.findById(hotelId)
                .orElseThrow(() -> new ResourceNotFoundException("Hotel not found with ID: " + hotelId));
        
        if (hotel.isRegistered()) {
            // MODIFIED: Use HotelActionException for 409 Conflict
            throw new HotelActionException("Hotel is already active.");
        }
        
        hotel.setRegistered(true);
        hotelRepo.save(hotel);
    }

    public List<RatingResponseDTO> getRatingsForHotel(String hotelId) {
        if (!hotelRepo.existsById(hotelId)) {
            throw new ResourceNotFoundException("Hotel not found with ID: " + hotelId);
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
    public void addRating(String hotelId, RatingRequestDTO dto) {
        // 1. Validate Rating Range
        if (dto.getRatingValue() < 1 || dto.getRatingValue() > 5) {
            throw new InvalidInputException("Rating value must be between 1 and 5.");
        }

        // 2. NEW: Check for existing rating by this user for this hotel
        if (ratingRepo.existsByUserIdAndHotelHotelId(dto.getUserId(), hotelId)) {
            throw new HotelActionException("User " + dto.getUserId() + " has already submitted a rating for hotel " + hotelId);
        }

        // 3. Find Hotel
        Hotel hotel = hotelRepo.findById(hotelId)
                .orElseThrow(() -> new ResourceNotFoundException("Hotel not found with ID: " + hotelId));
        
        // 4. Build and Save
        Rating rating = Rating.builder()
                .userId(dto.getUserId())
                .ratingValue(dto.getRatingValue())
                .reviewText(dto.getReviewText())
                .hotel(hotel)
                .build();
        ratingRepo.save(rating);
    }

    private HotelResponseDTO mapToHotelResponse(Hotel hotel) {
        // Calculate average from the list of ratings
        double avg = hotel.getRatings() != null && !hotel.getRatings().isEmpty() 
            ? hotel.getRatings().stream().mapToDouble(Rating::getRatingValue).average().orElse(0.0)
            : 0.0;

        return HotelResponseDTO.builder()
                .hotelId(hotel.getHotelId())
                .hotelName(hotel.getHotelName())
                .location(hotel.getLocation())
                .basePrice(hotel.getBasePrice())
                .amenities(hotel.getAmenities())
                .totalRooms(hotel.getTotalRooms())
                .isRegistered(hotel.isRegistered())
                .averageRating(avg) // Add this field to your DTO
                .reviewCount(hotel.getRatings() != null ? hotel.getRatings().size() : 0)
                .build();
    }
}