package com.tripease.booking.client;

import com.tripease.booking.dto.hotel.HotelRecentBookingResponseDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.util.List;

@FeignClient(name = "hotel-service", path = "/hotel")
public interface HotelServiceClient {
    @PutMapping("/inventory/update")
    ResponseEntity<String> updateInventory(
            @RequestParam Long hotelId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate checkIn,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate checkOut,
            @RequestParam String action);// "BOOK" or "CANCEL"

    @GetMapping("/findAll/ids")
    public ResponseEntity<List<HotelRecentBookingResponseDTO>> getHotelsByIds(@RequestParam List<Long> hotelIds);

    @GetMapping("{hotelId}")
    public ResponseEntity<HotelRecentBookingResponseDTO> getHotelById(@PathVariable Long hotelId);
}
