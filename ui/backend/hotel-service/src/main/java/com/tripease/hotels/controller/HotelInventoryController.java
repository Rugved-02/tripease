package com.tripease.hotels.controller;

import com.tripease.hotels.service.HotelService;
import com.tripease.hotels.service.InventoryManagementService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@Slf4j
@RestController
@RequestMapping("hotel/inventory")
@RequiredArgsConstructor
public class HotelInventoryController {

    private final HotelService hotelService;
    private final InventoryManagementService inventoryManagementService;

    @PutMapping("/update")
    public ResponseEntity<String> updateInventory(
            @RequestParam Long hotelId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate checkIn,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate checkOut,
            @RequestParam String action) {

        log.info("Received request to update inventory: Action={}, HotelID={}, CheckIn={}, CheckOut={}",
                action, hotelId, checkIn, checkOut);

        boolean success = inventoryManagementService.updateInventory(hotelId, checkIn, checkOut, action);

        if (success) {
            log.info("Inventory successfully updated for HotelID: {} (Action: {})", hotelId, action);
            return ResponseEntity.ok("Inventory Updated");
        } else {
            log.warn("Inventory update failed for HotelID: {} - likely due to insufficient availability for action: {}",
                    hotelId, action);
            return ResponseEntity.status(HttpStatus.CONFLICT).body("No Rooms Available");
        }
    }
}