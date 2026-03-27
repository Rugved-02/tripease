package com.tripease.hotels.dto;
import lombok.Data;

@Data
public class RatingRequestDTO {
    private String userId;
    private int ratingValue;
    private String reviewText;
}