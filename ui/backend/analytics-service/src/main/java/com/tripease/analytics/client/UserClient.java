package com.tripease.analytics.client;

import com.tripease.analytics.dto.UserDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import java.util.List;

@FeignClient(name = "auth-service", path="/auth/analytics")
public interface UserClient {

    /**
     * Used for the "Total Customers" summary card.
     */
    @GetMapping("/users/count")
    Long getTotalUsers();

    /**
     * Fetches all user details (id, createdAt) required for
     * Customer Growth (Bar Chart) and Trend calculations.
     */
    @GetMapping("/users")
    List<UserDTO> getAllUsers();
}