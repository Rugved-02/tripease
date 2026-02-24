package com.tripease.hotels.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "RATINGS")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Rating {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int ratingId;

    private int userId;
    private int ratingValue;

    @Column(columnDefinition = "TEXT")
    private String reviewText;

    
    @ManyToOne
    @JoinColumn(name = "hotel_id") // Matches the parent column name exactly
    private Hotel hotel;
}
