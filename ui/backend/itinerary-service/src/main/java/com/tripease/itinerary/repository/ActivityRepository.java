package com.tripease.itinerary.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tripease.itinerary.model.ActivityDetail;

import java.util.List;

@Repository
public interface ActivityRepository extends JpaRepository<ActivityDetail, Long> {
    // Spring Data JPA derives the query: find by the 'tripId' inside the 'trip' object
    List<ActivityDetail> findByTripTripId(Long tripId);
}