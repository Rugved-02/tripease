package com.tripease.payment.client;

import com.tripease.payment.dto.BookingRequestDTO;
import com.tripease.payment.dto.BookingResponseDTO;
import com.tripease.payment.model.BookingStatus;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@FeignClient(name = "booking-service")
public interface BookingServiceClient {

//    @PutMapping("/booking/{refId}/status")
//    public ResponseEntity<BookingResponseDTO> updateStatus(
//            @PathVariable String refId,
//            @RequestParam BookingStatus newStatus);

    @PutMapping("booking/{bookingReferenceId}/confirm")
    public ResponseEntity<BookingResponseDTO> confirmBooking(@PathVariable String bookingReferenceId);

    @PutMapping("booking/{bookingReferenceId}/cancel")
    public ResponseEntity<BookingResponseDTO> cancelBooking(@PathVariable String bookingReferenceId);

}
