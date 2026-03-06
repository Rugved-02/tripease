package com.tripease.flight.dto;

import com.tripease.flight.model.ClassType;
import lombok.Builder;
import java.time.LocalTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.math.BigDecimal;

@Builder
public record FlightRequestDTO(
        String flightNo,
        String airline,
        String depPlace,
        String arrPlace,
//        LocalDateTime depTime,
//        LocalDateTime arrTime,

        @JsonFormat(pattern = "HH:mm")
        LocalTime depTime,

        @JsonFormat(pattern = "HH:mm")
        LocalTime arrTime,

        List<SeatConfigDTO> seats
) {
    @Builder
    public record SeatConfigDTO(
            ClassType classType,
            BigDecimal basePrice,
            Integer totalCapacity
    ) {}
}

