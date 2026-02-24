package com.tripease.hotels.repository;

import com.tripease.hotels.model.Hotel;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface HotelRepository extends JpaRepository<Hotel, String> {
    List<Hotel> findByLocationContainingIgnoreCase(String location);
}
