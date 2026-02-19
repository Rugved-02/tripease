package com.tripease.hotels.repository;

import com.tripease.hotels.model.Rating;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface RatingRepository extends JpaRepository<Rating, Integer> {
    List<Rating> findByHotelHotelId(int hotelId);
}