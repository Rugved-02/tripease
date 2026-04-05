package com.tripease.backend.repository;

import com.tripease.backend.model.Trip;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface TripRepository extends JpaRepository<Trip, Integer> {
    // The name must be EXACTLY this for Spring to understand the logic
    List<Trip> findAllByOrderByUserIdAscStartDateAsc();
}


