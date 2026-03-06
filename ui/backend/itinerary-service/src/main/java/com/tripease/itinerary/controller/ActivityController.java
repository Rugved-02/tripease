package com.tripease.itinerary.controller;


import com.tripease.itinerary.dto.ActivityRequestDTO;
import com.tripease.itinerary.dto.ActivityResponseDTO;
import com.tripease.itinerary.service.ActivityService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("itinerary/activities")
@RequiredArgsConstructor
public class ActivityController {

    private final ActivityService activityService;

    // POST: Add an activity to a trip
    @PostMapping
    public ResponseEntity<ActivityResponseDTO> createActivity(@RequestBody ActivityRequestDTO activityRequest) {
        ActivityResponseDTO response = activityService.addActivity(activityRequest);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    // GET: Fetch all activities for a specific trip
    @GetMapping("/trip/{tripId}")
    public ResponseEntity<List<ActivityResponseDTO>> getActivitiesByTrip(@PathVariable Long tripId) {
        List<ActivityResponseDTO> activities = activityService.getActivitiesByTripId(tripId);
        return ResponseEntity.ok(activities);
    }
}
