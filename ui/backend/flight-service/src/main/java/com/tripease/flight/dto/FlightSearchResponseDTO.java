package com.tripease.flight.dto;

import lombok.Builder;
import com.tripease.flight.model.ClassType;
import java.math.BigDecimal;
import java.time.LocalTime;

@Builder
public record FlightSearchResponseDTO(
        Long flightId,
        String flightNo,
        String airline,
        String depPlace,
        String arrPlace,
        LocalTime depTime,
        LocalTime arrTime,
        ClassType classType,
        Integer availableSeats,
        BigDecimal price // This is the "calculated on the fly" price
) {}
