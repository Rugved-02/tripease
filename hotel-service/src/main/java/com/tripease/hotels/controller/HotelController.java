package com.tripease.hotels.controller;

import com.tripease.hotels.client.PaymentServiceClient;
import com.tripease.hotels.config.GlobalSecurityStore;
import com.tripease.hotels.dto.*;
import com.tripease.hotels.model.Hotel;
import com.tripease.hotels.service.HotelService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;


@RestController
@RequestMapping("/hotel")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
public class HotelController {

    private final HotelService hotelService;

    @GetMapping("/search")
    public ResponseEntity<List<HotelSearchResponseDTO>> search(
            @RequestParam String location,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate checkIn,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate checkOut) {

        return ResponseEntity.ok(hotelService.searchHotels(location, checkIn, checkOut));
    }

    @PostMapping("")
    public ResponseEntity<HotelResponseDTO> addHotel(@RequestBody HotelRequestDTO hotelRequestDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(hotelService.registerNewHotel(hotelRequestDTO));
    }
    @GetMapping
    public ResponseEntity<List<HotelResponseDTO>> getInitialHotels() {
        // This ensures the landing page only shows the top 15
        return ResponseEntity.ok(hotelService.getTopRatedHotels(15));
    }
   


//    @GetMapping("/hotels")
//    public ResponseEntity<List<HotelResponseDTO>> getHotels(@RequestParam(required = false) String location) {
//        List<HotelResponseDTO> hotels;
//        if (location != null && !location.isEmpty()) {
//            hotels = hotelService.searchByLocation(location);
//        } else {
//            hotels = hotelService.getAllHotels();
//        }
//        return ResponseEntity.ok(hotels); // Returns 200 OK
//    }

    @GetMapping("/hotels/{hotelId}")
    public ResponseEntity<HotelResponseDTO> getHotel(@PathVariable Long hotelId) {
        return ResponseEntity.ok(hotelService.getHotelById(hotelId)); // Returns 200 OK
    }

//    @PostMapping("/hotels")
//    public ResponseEntity<String> addHotel(@RequestBody HotelRequestDTO hotelDto) {
//        hotelService.addHotel(hotelDto);
//
//        return new ResponseEntity<>("Hotel added successfully", HttpStatus.CREATED); // Returns 201 Created
//
//    }

    @GetMapping("/{hotelId}/ratings")
    public ResponseEntity<List<RatingResponseDTO>> getRatings(@PathVariable Long hotelId) {
        return ResponseEntity.ok(hotelService.getRatingsForHotel(hotelId));
    }

    @PostMapping("/{hotelId}/ratings")
    public ResponseEntity<String> addRating(@PathVariable Long hotelId, @RequestBody RatingRequestDTO ratingDto) {
        hotelService.addRating(hotelId, ratingDto);
        return new ResponseEntity<>("Rating added successfully", HttpStatus.CREATED); // Returns 201 Created
    }

    @PatchMapping("/{hotelId}/activate")
    public ResponseEntity<String> activateHotel(@PathVariable Long hotelId) {
        hotelService.activateHotel(hotelId);
        return ResponseEntity.ok("Hotel activated successfully");
    }

    @PatchMapping("/{hotelId}/deactivate")
    public ResponseEntity<String> deactivateHotel(@PathVariable Long hotelId) {
        hotelService.deactivateHotel(hotelId);
        return ResponseEntity.ok("Hotel deactivated successfully");
    }
}