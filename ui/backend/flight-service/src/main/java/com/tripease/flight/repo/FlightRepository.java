package com.tripease.flight.repo;

import com.tripease.flight.dto.FlightRecentBookingResponseDTO;
import org.springframework.data.jpa.repository.JpaRepository;

import com.tripease.flight.model.Flight;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface FlightRepository extends JpaRepository<Flight, Long> {
    List<Flight> findByDepPlaceIgnoreCaseAndArrPlaceIgnoreCaseAndDepTimeBetween(String depPlace, String arrPlace, LocalDateTime startOfDay, LocalDateTime endOfDay);

//    @Query("SELECT DISTINCT f FROM Flight f JOIN f.seats s " +
//            "WHERE f.depPlace = :from " +
//            "AND f.arrPlace = :to " +
//            "AND CAST(f.depTime AS date) = :depDate " +
//            "AND s.availableSeats >= :passengers")
//    List<Flight> findAvailableFlights(
//            @Param("from") String from,
//            @Param("to") String to,
//            @Param("depDate") LocalDate depDate,
//            @Param("passengers") int passengers);

        @Query("SELECT f, s, i FROM Flight f " +
                "JOIN f.seats s " +
                "LEFT JOIN FlightInventory i ON f.flightId = i.flight.flightId " +
                "AND s.classType = i.classType " +
                "AND i.travelDate = :date " +
                "WHERE LOWER(f.depPlace) = LOWER(:from) " +
                "AND LOWER(f.arrPlace) = LOWER(:to) " +
                "AND CAST(f.depTime AS date) = :date " +
                "AND (i.availableSeats IS NULL OR i.availableSeats >= :passengers) " +
                "AND s.totalCapacity >= :passengers")
        List<Object[]> searchFlights(
                @Param("from") String from,
                @Param("to") String to,
                @Param("date") LocalDate date,
                @Param("passengers") int passengers);

    @Query("SELECT f, s, i FROM Flight f " +
            "JOIN f.seats s " +
            "JOIN FlightInventory i ON f.flightId = i.flight.flightId " +
            "AND s.classType = i.classType " +
            "WHERE f.depPlace = :from " +
            "AND f.arrPlace = :to " +
            "AND i.travelDate = :searchDate " + // Search against the Inventory Date
            "AND i.availableSeats >= :passengers")
    List<Object[]> searchAvailableFlights(
            @Param("from") String from,
            @Param("to") String to,
            @Param("searchDate") LocalDate searchDate,
            @Param("passengers") int passengers);


    @Query("SELECT new com.tripease.flight.dto.FlightRecentBookingResponseDTO(f.flightId, f.flightNo, f.airline, f.depPlace, f.arrPlace) " +
            "FROM Flight f WHERE f.id IN :ids")
    List<FlightRecentBookingResponseDTO> findFlightsByIds(@Param("ids") List<Long> ids);

    @Query("SELECT new com.tripease.flight.dto.FlightRecentBookingResponseDTO(f.flightId, f.flightNo, f.airline, f.depPlace, f.arrPlace) " +
            "FROM Flight f WHERE f.id  = :id")
    FlightRecentBookingResponseDTO findFlightById(@Param("id") Long id);

}