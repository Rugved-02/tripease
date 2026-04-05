package com.tripease.backend.controller;

import com.tripease.backend.model.Trip;
import com.tripease.backend.service.TripService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/trips")
public class TripController {

    @Autowired
    private TripService tripService;

    @PostMapping
    public ResponseEntity<Trip> createTrip(@RequestBody Trip trip) {
        Trip savedTrip = tripService.saveTrip(trip);
        return new ResponseEntity<>(savedTrip, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<Trip>> getAllTrips() {
        List<Trip> trips = tripService.getAllTrips();
        return new ResponseEntity<>(trips, HttpStatus.OK);
    }
}