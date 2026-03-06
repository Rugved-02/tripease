package com.tripease.itinerary.repository;

import com.tripease.itinerary.model.Trip;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface TripRepository extends JpaRepository<Trip, Long> {
    List<Trip> findByUserId(String userId);
    List<Trip> findByUserIdAndIsEnabledTrue(String userId);
}