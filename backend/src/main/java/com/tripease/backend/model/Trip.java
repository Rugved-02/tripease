package com.tripease.backend.model;

import jakarta.persistence.*;
import lombok.Data;
import java.util.List;

@Entity
@Table(name = "TRIPS")
@Data
public class Trip {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer tripId;
    
    private String tripName;
    private String startDate;
    private String endDate;
    private Integer userId;

    @OneToMany(mappedBy = "trip", cascade = CascadeType.ALL, orphanRemoval = true)
    // DELETED @JsonManagedReference from here!
    private List<ActivityDetail> activities;
}