package com.tripease.analytics.client;

import com.tripease.analytics.dto.BookingDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import java.util.List;

@FeignClient(name = "booking-service",  path = "/booking/analytics")
public interface BookingClient {

    /**
     * Fetches all booking details (amount, type, date, userId)
     * required for trend and distribution calculations.
     */
    @GetMapping("/all")
    List<BookingDTO> getAllBookings();

    /**
     * Used for Total Revenue calculation.
     */
    @GetMapping("/prices")
    List<Double> getConfirmedPrices();

    /**
     * Used for the Flight Bookings summary card.
     */
    @GetMapping("/flights/count")
    Long getFlightCount();

    /**
     * Used for the Hotel Bookings summary card.
     */
    @GetMapping("/hotels/count")
    Long getHotelCount();

    /**
     * Used for the Customer Retention (repeat customers) calculation.
     */
    @GetMapping("/users/returning/count")
    Long getReturningUsers();
}