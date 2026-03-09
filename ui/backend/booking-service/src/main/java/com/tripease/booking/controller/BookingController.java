package com.tripease.booking.controller;

import com.tripease.booking.dto.BookingRequestDTO;
import com.tripease.booking.dto.BookingResponseDTO;
import com.tripease.booking.dto.DashboardStatsResponseDTO;
import com.tripease.booking.dto.RecentBookingsResponseDTO;
import com.tripease.booking.model.Booking;
import com.tripease.booking.model.BookingStatus;
import com.tripease.booking.service.BookingService;
import com.tripease.booking.service.DashboardStatsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Slice;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/booking")
@RequiredArgsConstructor
public class BookingController {

    private final BookingService bookingService;
    private final DashboardStatsService dashboardStatsService;

    //     Create booking (Flight or Hotel)

    @PostMapping
    public ResponseEntity<Booking> createBooking(@RequestHeader("X-User-Id") String userId, @RequestBody BookingRequestDTO incomingRequestDto) {

        BookingRequestDTO completeRequestDto = BookingRequestDTO.builder()
                .userId(userId) // Mapping the User Id from headers
                .resourceId(incomingRequestDto.resourceId())
                .resourceType(incomingRequestDto.resourceType())
                .subType(incomingRequestDto.subType())
                .startDate(incomingRequestDto.startDate())
                .endDate(incomingRequestDto.endDate())
                .quantity(incomingRequestDto.quantity())
                .totalAmount(incomingRequestDto.totalAmount())
                .build();

        Booking response = bookingService.initiateBooking(completeRequestDto);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    //     Confirm booking (Flight or Hotel)

    @PutMapping("/{bookingReferenceId}/confirm")
    public ResponseEntity<BookingResponseDTO> confirmBooking(@PathVariable String bookingReferenceId) {
        log.info("REST request to confirm booking: {}", bookingReferenceId);
        return ResponseEntity.ok(bookingService.confirmBooking(bookingReferenceId));
    }


//     Cancel booking (Flight or Hotel)

    @PutMapping("/{bookingReferenceId}/cancel")
    public ResponseEntity<BookingResponseDTO> cancelBooking(@PathVariable String bookingReferenceId) {
        log.info("REST request to cancel booking with Reference: {}", bookingReferenceId);

        try {
            // Execute the service logic and capture the returned DTO
            BookingResponseDTO response = bookingService.cancelBooking(bookingReferenceId);

            log.info("Successfully processed cancellation for booking Ref: {}", bookingReferenceId);
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            log.error("Failed to cancel booking Ref: {}. Error: {}", bookingReferenceId, e.getMessage());
            // Since we changed the return type to <BookingResponseDTO>,
            // we can't return a String body anymore. Best to throw the error or use a GlobalExceptionHandler.
            throw e;
        }
    }


//    Retrieve a booking by its Public UUID

    @GetMapping("/{refId}")
    public ResponseEntity<RecentBookingsResponseDTO> getBooking(@PathVariable String refId) {
        log.info("BookingController :: getBooking()  called");
        return ResponseEntity.ok(dashboardStatsService.getRecentBookingById(refId));
    }

    @GetMapping("dashboard/cardsData")
    public ResponseEntity<DashboardStatsResponseDTO> getDashboardStats(
            @RequestHeader("X-User-Id") String userId) {

        log.info("Fetching dashboard stats for User ID: {}", userId);

        // Call the service to get the DTO
        DashboardStatsResponseDTO stats = dashboardStatsService.getStatsByUserId(userId);

        // Return 200 OK with the body
        return ResponseEntity.ok(stats);
    }

    @GetMapping("dashboard/recentBookings")
    public ResponseEntity<Slice<RecentBookingsResponseDTO>> getRecentBookingsDetail(
            @RequestHeader("X-User-Id") String userId,@RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "3") int size) {

        log.info("Fetching dashboard stats for User ID: {}", userId);

        // Logic: If it's the first page, maybe override size to 5
        int actualSize = (page == 0) ? 5 : size;
        // Call the service to get the DTO
        Slice<RecentBookingsResponseDTO> recentBookings = dashboardStatsService.getRecentBookingsWithNames(userId, page, actualSize);

        // Return 200 OK with the body
        return ResponseEntity.ok(recentBookings);
    }


}