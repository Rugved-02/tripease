package com.tripease.hotels.controller;

import com.tripease.hotels.dto.*;
import com.tripease.hotels.service.HotelService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping // Removed "/api" - endpoints now start directly from root
@CrossOrigin(origins = "http://localhost:4200")
@RequiredArgsConstructor
public class HotelController {

    private final HotelService hotelService;

    // Accessible at: GET /hotels or GET /hotels?location=...
    @GetMapping("/hotels")
    public List<HotelResponseDTO> getHotels(@RequestParam(required = false) String location) {
        if (location != null && !location.isEmpty()) {
            return hotelService.searchByLocation(location);
        }
        return hotelService.getAllHotels();
    }

    // Accessible at: GET /hotels/{hotelId}
    @GetMapping("/hotels/{hotelId}")
    public HotelResponseDTO getHotel(@PathVariable int hotelId) {
        return hotelService.getHotelById(hotelId);
    }

    // Accessible at: POST /hotels
    @PostMapping("/hotels")
    public String addHotel(@RequestBody HotelRequestDTO hotelDto) {
        hotelService.addHotel(hotelDto); 
        return "Hotel added successfully";
    }

    // Accessible at: GET /hotels/{hotelId}/ratings
    @GetMapping("/hotels/{hotelId}/ratings")
    public List<RatingResponseDTO> getRatings(@PathVariable int hotelId) {
        return hotelService.getRatingsForHotel(hotelId);
    }

    // Accessible at: POST /hotels/{hotelId}/ratings
    @PostMapping("/hotels/{hotelId}/ratings")
    public String addRating(@PathVariable int hotelId, @RequestBody RatingRequestDTO ratingDto) {
        hotelService.addRating(hotelId, ratingDto);
        return "Rating added successfully";
    }
    @PatchMapping("/hotels/{hotelId}/activate")
    public String activateHotel(@PathVariable int hotelId) {
        hotelService.activateHotel(hotelId);
        return "Hotel activated successfully";
    }

    // Accessible at: PATCH /hotels/{hotelId}/deactivate
    @PatchMapping("/hotels/{hotelId}/deactivate")
    public String deactivateHotel(@PathVariable int hotelId) {
        hotelService.deactivateHotel(hotelId);
        return "Hotel deactivated successfully";
    }
}