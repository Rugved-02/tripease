package com.tripease.hotels.dto;
import lombok.Data;

@Data
public class RatingRequestDTO {
    private int userId;
    private int ratingValue;
    private String reviewText;
}