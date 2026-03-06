package com.tripease.hotels.service;

import com.tripease.hotels.dto.*;
import com.tripease.hotels.model.*;
import com.tripease.hotels.repository.*;
import com.tripease.hotels.exception.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Slf4j
@Service
@RequiredArgsConstructor
public class HotelService {

    private final HotelRepository hotelRepository;
    private final RatingRepository ratingRepository;
    private final RoomInventoryRepository inventoryRepository;

    private final InventoryManagementService inventoryManagementService;
    private final HotelPricingService hotelPricingService;
    
    public List<HotelSearchResponseDTO> searchHotels(String location, LocalDate checkIn, LocalDate checkOut) {
        log.info("Searching for hotels in {} from {} to {}", location, checkIn, checkOut);
        long duration = ChronoUnit.DAYS.between(checkIn, checkOut);

        // 1. Get IDs from Database - Database operations are faster than Java loops
        List<Long> availableIds = inventoryRepository.findAvailableHotelIds(checkIn, checkOut, duration);

        if (availableIds.isEmpty()) {
            return Collections.emptyList();
        }

        // 2. Filter by Location directly in the SQL query
        List<Hotel> results = hotelRepository.findAllByHotelIdInAndLocationContainingIgnoreCaseAndIsRegisteredTrue(
                availableIds, location.trim());

        return results.parallelStream() // Use parallelStream for faster concurrent pricing
                .map(hotel -> {
                    BigDecimal totalStayPrice = BigDecimal.ZERO;
                    
                    // Potential Bottleneck: If getLivePrice hits a DB or API, this is slow
                    for (LocalDate date = checkIn; date.isBefore(checkOut); date = date.plusDays(1)) {
                        totalStayPrice = totalStayPrice.add(hotelPricingService.getLivePrice(hotel.getHotelId(), date));
                    }

                    Integer roomsLeft = inventoryRepository.findMinAvailableRooms(hotel.getHotelId(), checkIn, checkOut);

                    // 3. Calculate Average Rating
                    double avg = hotel.getRatings().stream()
                            .mapToDouble(Rating::getRatingValue)
                            .average().orElse(0.0);

                    return new HotelSearchResponseDTO(
                            hotel.getHotelId(),
                            hotel.getHotelName(),
                            hotel.getLocation(),
                            hotel.getAmenities(),
                            totalStayPrice,
                            hotel.getRatings(),
                            roomsLeft != null ? roomsLeft : 0,
                            avg
                    );
                })
                .toList();
    }
    @Transactional
    public HotelResponseDTO registerNewHotel(HotelRequestDTO hotelRequestDTO) {

        Hotel hotel = Hotel.builder()
                .hotelName(hotelRequestDTO.hotelName())
                .location(hotelRequestDTO.location())
                .basePrice(hotelRequestDTO.basePrice())
                .amenities(hotelRequestDTO.amenities())
                .totalRooms(hotelRequestDTO.totalRooms())
                .build();

        log.info("Creating new hotel: {}", hotel.getHotelName());

        Hotel savedHotel = hotelRepository.save(hotel);

        inventoryManagementService.initializeNewHotelInventory(savedHotel);

        log.info("Hotel created successfully with ID: {} and 180 days of inventory.", savedHotel.getHotelId());
        return mapToHotelResponse(savedHotel);
    }


//    public List<HotelResponseDTO> getAllHotels() {
//        log.debug("Fetching all hotels from database.");
//        return hotelRepository.findAll().stream()
//                .map(this::mapToHotelResponse)
//                .collect(Collectors.toList());
//    }
 // This handles the logic in one place
    public List<HotelResponseDTO> getTopRatedHotels(int limit) {
        return hotelRepository.findAll().stream()
                .map(this::mapToHotelResponse)
                .sorted((h1, h2) -> Double.compare(h2.getAverageRating(), h1.getAverageRating()))
                .limit(limit)
                .collect(Collectors.toList());
    }

    // Your main landing page call simply uses the method above
    public List<HotelResponseDTO> getAllHotels() {
        log.debug("Fetching Top 15 hotels for the landing page.");
        return getTopRatedHotels(15); 
    }
    
    public HotelResponseDTO getHotelById(Long hotelId) {
        return hotelRepository.findById(hotelId)
                .map(hotel -> {
                    log.debug("Hotel found with ID: {}", hotelId);
                    return mapToHotelResponse(hotel);
                })
                .orElseThrow(() -> {
                    log.error("Hotel search failed: ID {} not found", hotelId);
                    return new ResourceNotFoundException("Hotel not found with ID: " + hotelId);
                });
    }

    public void deactivateHotel(Long hotelId) {
        Hotel hotel = hotelRepository.findById(hotelId)
                .orElseThrow(() -> {
                    log.error("Deactivation failed: Hotel ID {} not found", hotelId);
                    return new ResourceNotFoundException("Hotel not found with ID: " + hotelId);
                });

        if (!hotel.isRegistered()) {
            log.warn("Attempted to deactivate an already inactive hotel ID: {}", hotelId);
            throw new HotelActionException("Hotel is already deactivated.");
        }

        hotel.setRegistered(false);
        hotelRepository.save(hotel);
        log.info("Hotel ID: {} has been deactivated.", hotelId);
    }

    public void activateHotel(Long hotelId) {
        Hotel hotel = hotelRepository.findById(hotelId)
                .orElseThrow(() -> {
                    log.error("Activation failed: Hotel ID {} not found", hotelId);
                    return new ResourceNotFoundException("Hotel not found with ID: " + hotelId);
                });

        if (hotel.isRegistered()) {
            log.warn("Attempted to activate an already active hotel ID: {}", hotelId);
            throw new HotelActionException("Hotel is already active.");
        }

        hotel.setRegistered(true);
        hotelRepository.save(hotel);
        log.info("Hotel ID: {} has been activated.", hotelId);
    }

    public List<RatingResponseDTO> getRatingsForHotel(Long hotelId) {
        if (!hotelRepository.existsById(hotelId)) {
            log.error("Failed to fetch ratings: Hotel ID {} does not exist", hotelId);
            throw new ResourceNotFoundException("Hotel not found with ID: " + hotelId);
        }

        log.debug("Fetching ratings for Hotel ID: {}", hotelId);
        return ratingRepository.findByHotelHotelId(hotelId).stream()
                .map(rating -> RatingResponseDTO.builder()
                        .ratingId(rating.getRatingId())
                        .userId(rating.getUserId())
                        .ratingValue(rating.getRatingValue())
                        .reviewText(rating.getReviewText())
                        .build())
                .collect(Collectors.toList());
    }

    public void addRating(Long hotelId, RatingRequestDTO dto) {
        log.info("User {} is adding a rating for Hotel ID: {}", dto.getUserId(), hotelId);

        if (dto.getRatingValue() < 1 || dto.getRatingValue() > 5) {
            log.warn("Invalid rating value: {}. Must be between 1-5", dto.getRatingValue());
            throw new InvalidInputException("Rating value must be between 1 and 5.");
        }

        if (ratingRepository.existsByUserIdAndHotelHotelId(dto.getUserId(), hotelId)) {
            log.warn("Duplicate rating attempt by User {} for Hotel {}", dto.getUserId(), hotelId);
            throw new HotelActionException("User " + dto.getUserId() + " has already submitted a rating for hotel " + hotelId);
        }

        Hotel hotel = hotelRepository.findById(hotelId)
                .orElseThrow(() -> new ResourceNotFoundException("Hotel not found with ID: " + hotelId));

        Rating rating = Rating.builder()
                .userId(dto.getUserId())
                .ratingValue(dto.getRatingValue())
                .reviewText(dto.getReviewText())
                .hotel(hotel)
                .build();
        ratingRepository.save(rating);
        log.info("Rating successfully saved for Hotel ID: {}", hotelId);
    }

    private HotelResponseDTO mapToHotelResponse(Hotel hotel) {
        double avg = hotel.getRatings() != null && !hotel.getRatings().isEmpty()
                ? hotel.getRatings().stream().mapToDouble(Rating::getRatingValue).average().orElse(0.0)
                : 0.0;

        return HotelResponseDTO.builder()
                .hotelName(hotel.getHotelName())
                .location(hotel.getLocation())
                .basePrice(hotel.getBasePrice())
                .amenities(hotel.getAmenities())
                .totalRooms(hotel.getTotalRooms())
                .isRegistered(hotel.isRegistered())
                .averageRating(avg)
                .reviewCount(hotel.getRatings() != null ? hotel.getRatings().size() : 0)
                .build();
    }
}