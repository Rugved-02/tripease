package com.tripease.hotels.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "ratings")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Rating {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int ratingId;

    private String userId;
    private int ratingValue;

    @Column(columnDefinition = "TEXT")
    private String reviewText;

    
    @ManyToOne
    @JoinColumn(name = "hotel_id") // Matches the parent column name exactly
    @JsonIgnore
    private Hotel hotel;
}
