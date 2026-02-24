package com.tripease.hotels.controller;

import com.tripease.hotels.dto.*;
import com.tripease.hotels.service.HotelService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping 
@CrossOrigin(origins = "http://localhost:4200")
@RequiredArgsConstructor
public class HotelController {

    private final HotelService hotelService;

    @GetMapping("/hotels")
    public ResponseEntity<List<HotelResponseDTO>> getHotels(@RequestParam(required = false) String location) {
        List<HotelResponseDTO> hotels;
        if (location != null && !location.isEmpty()) {
            hotels = hotelService.searchByLocation(location);
        } else {
            hotels = hotelService.getAllHotels();
        }
        return ResponseEntity.ok(hotels); // Returns 200 OK
    }

    @GetMapping("/hotels/{hotelId}")
    public ResponseEntity<HotelResponseDTO> getHotel(@PathVariable String hotelId) {
        return ResponseEntity.ok(hotelService.getHotelById(hotelId)); // Returns 200 OK
    }

    @PostMapping("/hotels")
    public ResponseEntity<String> addHotel(@RequestBody HotelRequestDTO hotelDto) {
        hotelService.addHotel(hotelDto);
        return new ResponseEntity<>("Hotel added successfully", HttpStatus.CREATED); // Returns 201 Created
    }

    @GetMapping("/hotels/{hotelId}/ratings")
    public ResponseEntity<List<RatingResponseDTO>> getRatings(@PathVariable String hotelId) {
        return ResponseEntity.ok(hotelService.getRatingsForHotel(hotelId));
    }

    @PostMapping("/hotels/{hotelId}/ratings")
    public ResponseEntity<String> addRating(@PathVariable String hotelId, @RequestBody RatingRequestDTO ratingDto) {
        hotelService.addRating(hotelId, ratingDto);
        return new ResponseEntity<>("Rating added successfully", HttpStatus.CREATED); // Returns 201 Created
    }

    @PatchMapping("/hotels/{hotelId}/activate")
    public ResponseEntity<String> activateHotel(@PathVariable String hotelId) {
        hotelService.activateHotel(hotelId);
        return ResponseEntity.ok("Hotel activated successfully");
    }

    @PatchMapping("/hotels/{hotelId}/deactivate")
    public ResponseEntity<String> deactivateHotel(@PathVariable String hotelId) {
        hotelService.deactivateHotel(hotelId);
        return ResponseEntity.ok("Hotel deactivated successfully");
    }
}