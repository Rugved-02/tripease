package com.tripease.booking.service;

import com.tripease.booking.client.FlightServiceClient;
import com.tripease.booking.client.HotelServiceClient;
import com.tripease.booking.dto.BookingRequestDTO;
import com.tripease.booking.dto.BookingResponseDTO;
import com.tripease.booking.model.Booking;
import com.tripease.booking.model.BookingStatus;
import com.tripease.booking.model.ResourceType;
import com.tripease.booking.repository.BookingRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class BookingService {

    private final BookingRepository bookingRepository;
    private final HotelServiceClient hotelClient;
    private final FlightServiceClient flightClient;


    @Transactional(readOnly = true)
    public BookingResponseDTO getBookingByRef(String refId) {
        log.debug("Fetching booking record for reference: {}", refId);
        return bookingRepository.findByBookingReference(refId)
                .map(this::mapToResponse)
                .orElseThrow(() -> {
                    log.warn("Search failed: Booking reference {} does not exist", refId);
                    return new RuntimeException("Booking not found with reference: " + refId);
                });
    }

//    @Transactional
//    public Booking initiateBooking(BookingRequestDTO dto) {
//        log.info("Initiating booking flow for User: {} on {} ID: {}",
//                dto.userId(), dto.resourceType(), dto.resourceId());
//
//            if ("HOTEL".equalsIgnoreCase(dto.resourceType())) {
//                if (dto.startDate() == null || dto.endDate() == null) {
//                    log.warn("Validation failed: Missing dates for Hotel booking. User: {}", dto.userId());
//                    throw new IllegalArgumentException("Start and End dates are required for Hotels");
//                }
//
//                log.info("Calling Hotel Service to decrement inventory for Hotel ID: {} ({} to {})",
//                        dto.resourceId(), dto.startDate(), dto.endDate());
//
//                ResponseEntity<String> response = hotelClient.updateInventory(
//                        dto.resourceId(),
//                        dto.startDate().toLocalDate(),
//                        dto.endDate().toLocalDate(),
//                        "BOOK"
//                );
//
//                if (!response.getStatusCode().is2xxSuccessful()) {
//                    log.error("External Call Failed: Hotel inventory unavailable for Hotel ID: {}. Status: {}",
//                            dto.resourceId(), response.getStatusCode());
//                    throw new RuntimeException("Rooms not available for selected dates");
//                }
//                log.info("Hotel inventory successfully reserved for Hotel ID: {}", dto.resourceId());
//            }
//            else if ("FLIGHT".equalsIgnoreCase(dto.resourceType())) {
//                // Flight-specific validation
//                if (dto.startDate() == null) {
//                    log.warn("Validation failed: Missing departure date for Flight booking. User: {}", dto.userId());
//                    throw new IllegalArgumentException("Departure date (startDate) is required for Flights");
//                }
//
//                log.info("Calling Flight Service for Flight ID: {}, Class: {}, Date: {}",
//                        dto.resourceId(), dto.subType(), dto.startDate());
//
//                // Call flightClient with subType (ClassType) and startDate
//                ResponseEntity<String> response = flightClient.reserveSeats(
//                        dto.resourceId(),
//                        dto.subType(), // e.g., "ECONOMY" or "BUSINESS"
//                        dto.startDate().toLocalDate(),
//                        dto.quantity()
//                );
//
//                if (!response.getStatusCode().is2xxSuccessful()) {
//                    log.error("Flight seats unavailable for ID: {}. Status: {}", dto.resourceId(), response.getStatusCode());
//                    throw new RuntimeException("Seats not available for the selected flight/class");
//                }
//
//                log.info("Flight seats successfully reserved for Flight ID: {}", dto.resourceId());
//            }
//
//        Booking booking = Booking.builder()
//                .userId(dto.userId())
//                .resourceId(dto.resourceId())
//                .resourceType(ResourceType.valueOf(dto.resourceType().toUpperCase()))
//                // SubType is only mapped for Flights (Seat Class)
//                .subType("FLIGHT".equalsIgnoreCase(dto.resourceType()) ? dto.subType() : null)
//                .startDate(dto.startDate())
//                // End date is only mapped for Hotels (Check-out)
//                .endDate("HOTEL".equalsIgnoreCase(dto.resourceType()) ? dto.endDate() : null)
//                .quantity(dto.quantity())
//                .totalAmount(dto.totalAmount())
//                .status(BookingStatus.CONFIRMED)
//                .build();
//
//        Booking saved = bookingRepository.save(booking);
//        log.info("Booking initialized and saved. Ref: {}, Status: {}",
//                saved.getBookingReference(), saved.getStatus());
//        return saved;
//    }

    @Transactional
    public Booking initiateBooking(BookingRequestDTO dto) {
        log.info("Initiating booking flow for User: {} on {} ID: {}",
                dto.userId(), dto.resourceType(), dto.resourceId());

        // --- HOTEL LOGIC ---
        if ("HOTEL".equalsIgnoreCase(dto.resourceType())) {
            if (dto.startDate() == null || dto.endDate() == null) {
                throw new IllegalArgumentException("Start and End dates are required for Hotels");
            }

            // Call Hotel Service to decrement
            ResponseEntity<String> response = hotelClient.updateInventory(
                    dto.resourceId(), dto.startDate(), dto.endDate(), "BOOK");

            if (!response.getStatusCode().is2xxSuccessful()) {
                throw new RuntimeException("Rooms not available for selected dates");
            }
        }
        // --- FLIGHT LOGIC ---
        else if ("FLIGHT".equalsIgnoreCase(dto.resourceType())) {
            if (dto.startDate() == null) {
                throw new IllegalArgumentException("Departure date (startDate) is required for Flights");
            }

            log.info("Reserving Flight Seats - ID: {}, Class: {}, Qty: {}",
                    dto.resourceId(), dto.subType(), dto.quantity());

            // Call flightClient to decrement FlightInventory
            ResponseEntity<String> response = flightClient.reserveSeats(
                    dto.resourceId(),
                    dto.subType(), // e.g., "ECONOMY"
                    dto.startDate(),
                    dto.quantity()
            );

            if (!response.getStatusCode().is2xxSuccessful()) {
                throw new RuntimeException("Seats not available for the selected flight/class");
            }
        }

        // --- SAVE BOOKING AS PENDING ---
        Booking booking = Booking.builder()
                .userId(dto.userId())
                .resourceId(dto.resourceId())
                .resourceType(ResourceType.valueOf(dto.resourceType().toUpperCase()))
                // SubType is only mapped for Flights (Seat Class)
                .subType("FLIGHT".equalsIgnoreCase(dto.resourceType()) ? dto.subType() : null)
                .startDate(dto.startDate())
                // End date is only mapped for Hotels (Check-out)
                .endDate("HOTEL".equalsIgnoreCase(dto.resourceType()) ? dto.endDate() : null)
                .quantity(dto.quantity())
                .totalAmount(dto.totalAmount())
                .status(BookingStatus.PENDING)
                .build();

        Booking saved = bookingRepository.save(booking);
        log.info("Booking reserved (PENDING). Ref: {}", saved.getBookingReference());
        return saved;
    }

    @Transactional
    public BookingResponseDTO confirmBooking(String bookingReferenceId) {
        log.info("Request received to confirm Booking Reference: {}", bookingReferenceId);

        // 1. Fetch the booking
        Booking booking = bookingRepository.findByBookingReference(bookingReferenceId)
                .orElseThrow(() -> {
                    log.error("Confirmation failed: Booking Reference {} not found", bookingReferenceId);
                    return new RuntimeException("Booking not found");
                });

        // 2. Validate current status (Optional but recommended)
        if (booking.getStatus() != BookingStatus.PENDING) {
            log.warn("Confirmation skipped: Booking {} is already in {} state",
                    bookingReferenceId, booking.getStatus());
            throw new IllegalStateException("Only PENDING bookings can be confirmed.");
        }

        // 3. Update status
        booking.setStatus(BookingStatus.CONFIRMED);
        log.debug("Booking Reference {} status updated to CONFIRMED in memory", bookingReferenceId);

        // 4. Save and Log
        Booking confirmedBooking = bookingRepository.save(booking);
        log.info("Booking Reference: {} successfully CONFIRMED.", bookingReferenceId);

        return mapToResponse(confirmedBooking);
    }

    @Transactional
    public BookingResponseDTO cancelBooking(String bookingReferenceId) {
        log.info("Request received to cancel Booking ID: {}", bookingReferenceId);

        Booking booking = bookingRepository.findByBookingReference(bookingReferenceId).orElseThrow(() -> {
            log.error("Cancellation aborted: Booking ID {} not found", bookingReferenceId);
            return new RuntimeException("Booking not found");
        });

//        if (booking.getStatus() != BookingStatus.PENDING) {
//            log.warn("Cancellation skipped: Booking {} is already in {} state",
//                    bookingReferenceId, booking.getStatus());
//            throw new IllegalStateException("Only PENDING bookings can be cancelled.");
//        }

        booking.setStatus(BookingStatus.CANCELLED);
        log.debug("Booking ID {} status set to CANCELLED", bookingReferenceId);

        log.info("Calling Service to restore inventory for {} ID: {}",
                booking.getResourceType(), booking.getResourceId());

        // 1. Release Hotel Inventory
        if (booking.getResourceType() == ResourceType.HOTEL) {
            hotelClient.updateInventory(
                    booking.getResourceId(),
                    booking.getStartDate(),
                    booking.getEndDate(),
                    "CANCEL"
            );
        }
        // 2. Release Flight Inventory
        else if (booking.getResourceType() == ResourceType.FLIGHT) {
            log.info("Releasing Flight Seats for Ref: {}", bookingReferenceId);
            flightClient.releaseSeats(
                    booking.getResourceId(),
                    booking.getSubType(), // The seat class (ECONOMY/BUSINESS)
                    booking.getStartDate(),
                    booking.getQuantity()
            );
        }

        booking.setStatus(BookingStatus.CANCELLED);
        Booking cancelledBooking = bookingRepository.save(booking);

        return mapToResponse(cancelledBooking);
    }


    private BookingResponseDTO mapToResponse(Booking booking) {
        return BookingResponseDTO.builder()
                .bookingReference(booking.getBookingReference())
                .userId(booking.getUserId())
                .resourceId(booking.getResourceId())
                .status(booking.getStatus().name())
                .totalAmount(booking.getTotalAmount())
                .build();
    }
}
