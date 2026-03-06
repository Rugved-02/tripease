package com.tripease.itinerary.model;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "trips")
@Data
public class Trip {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long tripId; // Internal Primary Key for this DB

    private String tripName;
    private LocalDate startDate;
    private LocalDate endDate;

    @Column(name = "user_id", nullable = false)
    private String userId;

    @JsonProperty("is_enabled") // This ensures the JSON key is "is_enabled"
    private Boolean isEnabled = true;

    @OneToMany(mappedBy = "trip", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ActivityDetail> activities = new java.util.ArrayList<>();

}
