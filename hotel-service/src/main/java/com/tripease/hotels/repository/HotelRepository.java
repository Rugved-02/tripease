package com.tripease.hotels.repository;

import com.tripease.hotels.model.Hotel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface HotelRepository extends JpaRepository<Hotel, Long> {
    
    // Key Update: Filters by the list of IDs from Inventory AND the location string
    List<Hotel> findAllByHotelIdInAndLocationContainingIgnoreCaseAndIsRegisteredTrue(List<Long> hotelIds, String location);

    List<Hotel> findByLocationIgnoreCaseAndIsRegisteredTrue(String location);
    
    List<Hotel> findAllByHotelIdInAndIsRegisteredTrue(List<Long> hotelIds);
}
