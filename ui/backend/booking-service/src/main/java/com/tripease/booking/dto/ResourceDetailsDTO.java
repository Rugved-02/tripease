    package com.tripease.booking.dto;

    import lombok.Builder;

    @Builder
    public record ResourceDetailsDTO(
            String hotelName,
            String hotelLocation,
            String flightNo,
            String airline,
            String depPlace,
            String arrPlace

    ){}
