package com.tripease.flight.controller;

import com.tripease.flight.model.ClassType;
import com.tripease.flight.service.InventoryManagementService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
@RequestMapping("flight/inventory")
@RequiredArgsConstructor
public class FlightInventoryController {

    private final InventoryManagementService inventoryService;

    @PutMapping("/reserve")
    public ResponseEntity<String> reserve(
            @RequestParam Long id,
            @RequestParam String classType,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @RequestParam int qty) {

        inventoryService.reserveSeats(id, date, ClassType.valueOf(classType.toUpperCase()), qty);
        return ResponseEntity.ok("Seats Reserved successfully");
    }

    @PutMapping("/release")
    public ResponseEntity<String> release(
            @RequestParam Long id,
            @RequestParam String classType,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @RequestParam int qty) {

        inventoryService.releaseSeats(id, date, ClassType.valueOf(classType.toUpperCase()), qty);
        return ResponseEntity.ok("Seats Released successfully");
    }
}
