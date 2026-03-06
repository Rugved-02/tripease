package com.tripease.booking.service;

import com.tripease.booking.client.FlightServiceClient;
import com.tripease.booking.client.HotelServiceClient;
import com.tripease.booking.model.Booking;
import com.tripease.booking.model.BookingStatus;
import com.tripease.booking.model.ResourceType;
import com.tripease.booking.repository.BookingRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class BookingCleanupScheduler {

    private final BookingRepository bookingRepository;
    private final FlightServiceClient flightClient;
    private final HotelServiceClient hotelClient;

    // Runs every 10 minutes (600,000 milliseconds)
    @Scheduled(fixedRate = 600000)
    @Transactional
    public void cleanupExpiredBookings() {
        // Any booking older than 10 minutes is considered "abandoned"
        LocalDateTime threshold = LocalDateTime.now().minusMinutes(10);

        List<Booking> expiredBookings = bookingRepository.findAllByStatusAndCreatedAtBefore(
                BookingStatus.PENDING, threshold);

        if (expiredBookings.isEmpty()) {
            return;
        }

        log.info("Found {} expired bookings. Starting cleanup...", expiredBookings.size());

        for (Booking booking : expiredBookings) {
            try {
                // 1. Release Inventory based on Resource Type
                if (ResourceType.FLIGHT.equals(booking.getResourceType())) {
                    log.info("Releasing seats for expired Flight Booking: {}", booking.getBookingReference());
                    flightClient.releaseSeats(
                            booking.getResourceId(),
                            booking.getSubType(),
                            booking.getStartDate(),
                            booking.getQuantity()
                    );
                }
                else if (ResourceType.HOTEL.equals(booking.getResourceType())) {
                    log.info("Releasing rooms for expired Hotel Booking: {}", booking.getBookingReference());
                    hotelClient.updateInventory(
                            booking.getResourceId(),
                            booking.getStartDate(),
                            booking.getEndDate(),
                            "CANCEL"
                    );
                }

                // 2. Mark the booking as EXPIRED so it's not picked up again
                booking.setStatus(BookingStatus.EXPIRED);
                bookingRepository.save(booking);

            } catch (Exception e) {
                log.error("Failed to release inventory for booking {}: {}",
                        booking.getBookingReference(), e.getMessage());
                // We don't throw an exception here so the loop can continue for other bookings
            }
        }
    }
}
