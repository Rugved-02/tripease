package com.tripease.auth.service;


import com.tripease.auth.dto.UserAnalyticsDTO;
import com.tripease.auth.model.User;
import com.tripease.auth.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserAnalyticsService {

    private final UserRepository userRepository;

    public Long getTotalUserCount() {
        return userRepository.count();
    }

    public List<UserAnalyticsDTO> getAllUsersForAnalytics() {
        return userRepository.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    private UserAnalyticsDTO mapToDTO(User user) {
        UserAnalyticsDTO dto = new UserAnalyticsDTO();

        // Safety: Attempt to parse UUID string to Long if possible,
        // otherwise use hashcode or null depending on Analytics requirements.
        try {
            // If your UUIDs are purely numeric strings:
            dto.setId(Long.parseLong(user.getUserId()));
        } catch (NumberFormatException e) {
            // If they are standard UUIDs (like "550e8400..."),
            // we use the hashcode as a numeric representation for the chart id.
            dto.setId((long) user.getUserId().hashCode());
        }

        dto.setCreatedAt(user.getCreatedAt());
        return dto;
    }
}
