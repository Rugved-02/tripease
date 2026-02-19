package com.tripease.hotels.model;

import jakarta.persistence.*;
import lombok.*;
import java.util.List;

@Entity
@Table(name = "HOTELS")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Hotel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "hotel_id")
    private int hotelId;

    @Column(name = "hotel_name", nullable = false)
    private String hotelName;

    private String location;
    
    @Column(name = "basePrice") // Force Hibernate to use exactly this name
    private double basePrice;
    
    @ElementCollection
    @CollectionTable(name = "HOTEL_AMENITIES", joinColumns = @JoinColumn(name = "hotel_id"))
    @Column(name = "amenity_name")
    private List<String> amenities;

    private int totalRooms;

    @Builder.Default
    @Column(name = "is_registered", nullable = false, columnDefinition = "BIT DEFAULT 1")
    private boolean isRegistered = true;

    @OneToMany(mappedBy = "hotel", cascade = CascadeType.ALL)
    private List<Rating> ratings;
}