package com.tripease.booking.service;

import com.tripease.booking.dto.BookingAnalyticsDTO;
import com.tripease.booking.model.Booking;
import com.tripease.booking.model.ResourceType;
import com.tripease.booking.repository.BookingRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class BookingAnalyticsService {

    private final BookingRepository bookingRepository;

    /**
     * Maps all bookings to BookingAnalyticsDTO.
     * Used by: GET /api/bookings
     */
    public List<BookingAnalyticsDTO> getAllBookingsForAnalytics() {
        return bookingRepository.findAll().stream()
                .map(this::mapToAnalyticsDTO)
                .collect(Collectors.toList());
    }

    /**
     * Retrieves all prices for confirmed bookings.
     * Used by: GET /bookings/prices
     */
    public List<Double> getConfirmedPrices() {
        return bookingRepository.findConfirmedPrices().stream()
                .map(BigDecimal::doubleValue)
                .collect(Collectors.toList());
    }

    /**
     * Counts resource by type (FLIGHT or HOTEL).
     * Used by: GET /bookings/flights/count and /bookings/hotels/count
     */
    public Long getCountByResourceType(ResourceType type) {
        return bookingRepository.countByResourceType(type);
    }

    /**
     * Calculates the number of unique users with > 1 booking.
     * Used by: GET /users/returning/count
     */
    public Long getReturningUserCount() {
        return bookingRepository.countReturningUsers();
    }

    /**
     * Internal mapper to convert Entity -> Analytics DTO.
     * Handles the String to Long conversion for userId.
     */
    private BookingAnalyticsDTO mapToAnalyticsDTO(Booking booking) {
        BookingAnalyticsDTO dto = new BookingAnalyticsDTO();

        // Safe conversion of String userId to Long
        try {
            if (booking.getUserId() != null) {
                dto.setUserId(Long.parseLong(booking.getUserId()));
            }
        } catch (NumberFormatException e) {
            log.error("Failed to parse userId {} to Long for booking ID {}",
                    booking.getUserId(), booking.getId());
            dto.setUserId(null);
        }

        dto.setType(booking.getResourceType().name());
        dto.setAmount(booking.getTotalAmount() != null ?
                booking.getTotalAmount().doubleValue() : 0.0);

        // Using createdAt as the primary date for analytics trends
        dto.setBookingDate(booking.getCreatedAt());

        return dto;
    }
}
