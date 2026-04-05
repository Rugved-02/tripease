package com.tripease.backend.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "ACTIVITY_DETAILS")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ActivityDetail {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer activityId;
    
    private String title;

    @Enumerated(EnumType.STRING)
    private ActivityType type; 
    
    private String location;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    
    @Column(columnDefinition = "TEXT")
    private String notes;

    @ManyToOne
    @JoinColumn(name = "trip_id")
    @JsonIgnore // Stops the infinite JSON loop!
    private Trip trip;
}