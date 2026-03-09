package com.tripease.analytics.service;

import com.tripease.analytics.dto.BookingDTO;
import com.tripease.analytics.dto.UserDTO;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class MockDataService {

    /**
     * Generates a list of users spanning the last 6 months
     * to test Customer Growth (Bar Chart) and Trends.
     */
    public List<UserDTO> getAllUsers() {
        List<UserDTO> users = new ArrayList<>();
        LocalDateTime now = LocalDateTime.now();

        // Adding users registered in different months
        users.add(createUser(1L, now.minusMonths(5))); // Jan
        users.add(createUser(2L, now.minusMonths(4))); // Feb
        users.add(createUser(3L, now.minusMonths(3))); // Mar
        users.add(createUser(4L, now.minusMonths(2))); // Apr
        users.add(createUser(5L, now.minusMonths(1))); // May
        users.add(createUser(6L, now));                // Jun

        return users;
    }

    /**
     * Generates bookings to test Revenue Trend (Line Chart),
     * Distribution (Pie Chart), and Retention logic.
     */
    public List<BookingDTO> getAllBookings() {
        List<BookingDTO> bookings = new ArrayList<>();
        LocalDateTime now = LocalDateTime.now();

        // Current Month Bookings
        bookings.add(createBooking(1L, "FLIGHT", 1200.0, now.minusDays(2)));
        bookings.add(createBooking(2L, "HOTEL", 800.0, now.minusDays(5)));
        bookings.add(createBooking(1L, "HOTEL", 450.0, now.minusDays(1))); // Repeat User 1

        // Previous Month Bookings (for Trend calculation)
        bookings.add(createBooking(3L, "FLIGHT", 1100.0, now.minusMonths(1)));
        bookings.add(createBooking(4L, "ITINERARY", 2500.0, now.minusMonths(1).minusDays(10)));

        return bookings;
    }

    // Methods to mock specific summary card endpoints
    public Long getTotalUsersCount() { return 3890L; }
    public List<Double> getConfirmedPrices() { return List.of(328000.0); }
    public Long getFlightCount() { return 1250L; }
    public Long getHotelCount() { return 890L; }
    public Long getReturningUsersCount() { return 3384L; } // ~87% of 3890

    // Helper methods to keep mock data clean
    private UserDTO createUser(Long id, LocalDateTime date) {
        UserDTO user = new UserDTO();
        user.setId(id);
        user.setCreatedAt(date);
        return user;
    }

    private BookingDTO createBooking(Long userId, String type, Double amount, LocalDateTime date) {
        BookingDTO booking = new BookingDTO();
        booking.setUserId(userId);
        booking.setType(type);
        booking.setAmount(amount);
        booking.setBookingDate(date);
        return booking;
    }
}