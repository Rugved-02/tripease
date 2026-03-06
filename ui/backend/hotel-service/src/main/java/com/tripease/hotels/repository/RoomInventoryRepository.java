package com.tripease.hotels.repository;

import com.tripease.hotels.model.Hotel;
import com.tripease.hotels.model.RoomInventory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface RoomInventoryRepository extends JpaRepository<RoomInventory, Long> {

    @Query("SELECT i.hotelId FROM RoomInventory i " +
            "WHERE i.inventoryDate >= :checkIn AND i.inventoryDate < :checkOut " +
            "AND i.availableRooms > 0 " +
            "GROUP BY i.hotelId " +
            "HAVING COUNT(i.hotelId) = :duration")
    List<Long> findAvailableHotelIds(LocalDate checkIn, LocalDate checkOut, long duration);

    @Modifying
    @Query("UPDATE RoomInventory i SET i.availableRooms = i.availableRooms - 1 " +
            "WHERE i.hotelId = :hotelId " +
            "AND i.inventoryDate >= :checkIn " +
            "AND i.inventoryDate < :checkOut " +
            "AND i.availableRooms > 0")
    int decrementAvailability(Long hotelId, LocalDate checkIn, LocalDate checkOut);

    @Modifying
    @Query("UPDATE RoomInventory i SET i.availableRooms = i.availableRooms + 1 " +
            "WHERE i.hotelId = :hotelId " +
            "AND i.inventoryDate >= :checkIn " +
            "AND i.inventoryDate < :checkOut")
    void incrementAvailability(Long hotelId, LocalDate checkIn, LocalDate checkOut);

    void deleteByInventoryDateBefore(LocalDate date);

    boolean existsByHotelIdAndInventoryDate(Long hotelId, LocalDate date);

    Optional<RoomInventory> findByHotelIdAndInventoryDate(Long hotelId, LocalDate stayDate);

    @Query("SELECT MIN(r.availableRooms) FROM RoomInventory r " +
            "WHERE r.hotelId = :hotelId " +
            "AND r.inventoryDate >= :checkIn " +
            "AND r.inventoryDate < :checkOut")
    Integer findMinAvailableRooms(Long hotelId, LocalDate checkIn, LocalDate checkOut);
}