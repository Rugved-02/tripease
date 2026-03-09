package com.tripease.auth.controller;

import com.tripease.auth.dto.UserAnalyticsDTO;
import com.tripease.auth.service.UserAnalyticsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth/analytics")
@Slf4j
public class UserAnalyticsController {

    private final UserAnalyticsService userAnalyticsService;

    /**
     * URL: GET /users/count
     * Used for the "Total Customers" summary card.
     */
    @GetMapping("/users/count")
    public ResponseEntity<Long> getTotalUsers() {
        log.info("UserAnalyticsController :: getTotalUsers()");
        return ResponseEntity.ok(userAnalyticsService.getTotalUserCount());
    }

    /**
     * URL: GET /api/users
     * Fetches user details for Customer Growth (Bar Chart).
     */
    @GetMapping("/users")
    public ResponseEntity<List<UserAnalyticsDTO>> getAllUsers() {
        log.info("UserAnalyticsController :: getAllUsers()");
        return ResponseEntity.ok(userAnalyticsService.getAllUsersForAnalytics());
    }
}

