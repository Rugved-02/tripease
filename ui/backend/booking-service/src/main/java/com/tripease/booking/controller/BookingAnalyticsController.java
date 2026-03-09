package com.tripease.booking.controller;

import com.tripease.booking.dto.BookingAnalyticsDTO;
import com.tripease.booking.model.ResourceType;
import com.tripease.booking.service.BookingAnalyticsService;
import com.tripease.booking.service.BookingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/booking/analytics")
@RequiredArgsConstructor
public class BookingAnalyticsController {

    private final BookingAnalyticsService bookingService;

    /**
     * URL: GET /booking/analytics/all
     * Fetches the full list of booking data for trend analysis.
     */
    @GetMapping("/all")
    public ResponseEntity<List<BookingAnalyticsDTO>> getAllBookings() {
        return ResponseEntity.ok(bookingService.getAllBookingsForAnalytics());
    }

    /**
     * URL: GET /booking/analytics/prices
     * Returns a list of all confirmed booking amounts.
     */
    @GetMapping("/prices")
    public ResponseEntity<List<Double>> getConfirmedPrices() {
        return ResponseEntity.ok(bookingService.getConfirmedPrices());
    }

    /**
     * URL: GET /booking/analytics/flights/count
     */
    @GetMapping("/flights/count")
    public ResponseEntity<Long> getFlightCount() {
        return ResponseEntity.ok(bookingService.getCountByResourceType(ResourceType.FLIGHT));
    }

    /**
     * URL: GET /booking/analytics/hotels/count
     */
    @GetMapping("/hotels/count")
    public ResponseEntity<Long> getHotelCount() {
        return ResponseEntity.ok(bookingService.getCountByResourceType(ResourceType.HOTEL));
    }

    /**
     * URL: GET /booking/analytics/users/returning
     */
    @GetMapping("/users/returning/count")
    public ResponseEntity<Long> getReturningUsers() {
        return ResponseEntity.ok(bookingService.getReturningUserCount());
    }
}