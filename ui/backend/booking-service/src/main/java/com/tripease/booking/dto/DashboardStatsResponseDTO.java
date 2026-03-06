package com.tripease.booking.dto;

import lombok.Builder;

@Builder
public record DashboardStatsResponseDTO(
        String totalBookings,
        String confirmedBookings,
        String pendingBookings,
        String expiredBookings,
        String cancelledBookings,
        String totalSpent
) {}
