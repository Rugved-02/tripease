package com.tripease.flight.repo;

import com.tripease.flight.model.ClassType;
import com.tripease.flight.model.Flight;
import com.tripease.flight.model.FlightInventory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface FlightInventoryRepository extends JpaRepository<FlightInventory, Long> {

    // Used for Midnight Cleanup
    @Modifying
    void deleteByTravelDateBefore(LocalDate date);

    // Used to avoid Primary Key/Unique constraints during generation
    boolean existsByFlightAndTravelDateAndClassType(Flight flight, LocalDate date, ClassType type);


    // Used for Booking/Search (Existing)
    Optional<FlightInventory> findByFlight_FlightIdAndTravelDateAndClassType(Long flightId, LocalDate date, ClassType type);

}
