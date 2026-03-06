package com.tripease.auth.dto;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserResponseDTO {
    private String userId; // UUID from your entity
    private String name;
    private String email;
    private String mobile;
    private LocalDateTime createdAt;
}
