package com.tripease.itinerary.controller;

import com.tripease.itinerary.dto.TripRequestDTO;
import com.tripease.itinerary.dto.TripResponseDTO;
import com.tripease.itinerary.service.TripService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("itinerary/trips")
@RequiredArgsConstructor
public class TripController {

    private final TripService tripService;

    @PostMapping
    public ResponseEntity<TripResponseDTO> createTrip(
            @RequestBody TripRequestDTO request,
         //   @AuthenticationPrincipal String userId // Automatically injected from your filter,
            @RequestHeader("X-User-Id") String userId
    ) {
        // Pass the extracted userId directly to the service
        TripResponseDTO response = tripService.saveTrip(request, userId);

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/my-trips")
    @Transactional(readOnly = true)
    public ResponseEntity<List<TripResponseDTO>> getMyTrips(@RequestHeader("X-User-Id") String userId) {
        // email will contain the value from the 'sub' claim of your JWT
        List<TripResponseDTO> trips = tripService.getTripsByUserId(userId);
        return ResponseEntity.ok(trips);
    }

    @PostMapping("/{tripId}")
    public ResponseEntity<TripResponseDTO> updateTrip(
            @PathVariable Long tripId,
            @RequestBody TripRequestDTO updateRequest,
            @RequestHeader("X-User-Id") String userId
    ) {
        return ResponseEntity.ok(tripService.updateTripMeta(tripId, updateRequest, userId));
    }


    @DeleteMapping("/{tripId}")
    public ResponseEntity<Void> softDeleteTrip(
            @PathVariable Long tripId,
            @RequestHeader("X-User-Id") String userId
    ) {
        tripService.disableTrip(tripId, userId);
        return ResponseEntity.noContent().build(); // 204 No Content is standard for successful deletes
    }


}