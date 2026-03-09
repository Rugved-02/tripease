package com.tripease.analytics.controller;

import com.tripease.analytics.dto.AnalyticsDTO;
import com.tripease.analytics.service.AnalyticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/analytics")
@RequiredArgsConstructor // Lombok generates the constructor for final fields
public class AnalyticsController {

    private final AnalyticsService service;

    @GetMapping("/details")
    public ResponseEntity<AnalyticsDTO> getAnalyticsDetails() {
        return ResponseEntity.ok(service.getCompleteAnalytics());
    }
}