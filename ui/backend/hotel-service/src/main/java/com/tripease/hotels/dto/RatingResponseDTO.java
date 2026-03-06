package com.tripease.hotels.dto;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RatingResponseDTO {
    private int ratingId;
    private String userId;
    private int ratingValue;
    private String reviewText;
}