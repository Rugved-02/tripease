package com.tripease.hotels.repository;

import com.tripease.hotels.dto.HotelRecentBookingResponseDTO;
import com.tripease.hotels.model.Hotel;
import feign.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface HotelRepository extends JpaRepository<Hotel, Long> {
    
    // Key Update: Filters by the list of IDs from Inventory AND the location string
    List<Hotel> findAllByHotelIdInAndLocationContainingIgnoreCaseAndIsRegisteredTrue(List<Long> hotelIds, String location);

    @Query("SELECT new com.tripease.hotels.dto.HotelRecentBookingResponseDTO(h.hotelId, h.hotelName, h.location) " +
            "FROM Hotel h WHERE h.id IN :ids")
    List<HotelRecentBookingResponseDTO> findHotelsByIds(@Param("ids") List<Long> ids);

    @Query("SELECT new com.tripease.hotels.dto.HotelRecentBookingResponseDTO(h.hotelId, h.hotelName, h.location) " +
            "FROM Hotel h WHERE h.id = :id")
    HotelRecentBookingResponseDTO findHotelById(@Param("id") Long id);

    List<Hotel> findByLocationIgnoreCaseAndIsRegisteredTrue(String location);
    
    List<Hotel> findAllByHotelIdInAndIsRegisteredTrue(List<Long> hotelIds);
}
