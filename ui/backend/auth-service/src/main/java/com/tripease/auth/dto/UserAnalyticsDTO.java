package com.tripease.auth.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UserAnalyticsDTO {
    private Long id;
    private LocalDateTime createdAt;
}
