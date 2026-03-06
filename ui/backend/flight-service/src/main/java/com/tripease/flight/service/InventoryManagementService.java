package com.tripease.flight.service;

import com.tripease.flight.exception.InsufficientSeatsException;
import com.tripease.flight.exception.ResourceNotFoundException;
import com.tripease.flight.exception.FlightOperationException; // Added for general logic errors
import com.tripease.flight.model.ClassType;
import com.tripease.flight.model.Flight;
import com.tripease.flight.model.FlightInventory;
import com.tripease.flight.model.FlightSeat;
import com.tripease.flight.repo.FlightInventoryRepository;
import com.tripease.flight.repo.FlightRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class InventoryManagementService {

    private final FlightInventoryRepository inventoryRepository;
    private final FlightRepository flightRepository;

    @Transactional
    public void reserveSeats(Long flightId, LocalDate date, ClassType type, int qty) {
        FlightInventory inv = inventoryRepository
                .findByFlight_FlightIdAndTravelDateAndClassType(flightId, date, type)
                .orElseThrow(() -> {
                    log.error("Inventory record missing for flight {} on {} for class {}", flightId, date, type);
                    return new ResourceNotFoundException("Inventory record missing for flight " + flightId + " on " + date + " for class " + type);
                });

        if (inv.getAvailableSeats() < qty) {
            log.warn("Insufficient seats: requested {} but only {} available for flight {} on {} for class {}", qty, inv.getAvailableSeats(), flightId, date, type);
            throw new InsufficientSeatsException("Not enough seats available for flight " + flightId + " on " + date + " for class " + type);
        }

        inv.setAvailableSeats(inv.getAvailableSeats() - qty);
        inventoryRepository.save(inv);
        log.info("Reserved {} seats for flight {} on {} for class {}", qty, flightId, date, type);
    }

    @Transactional
    public void releaseSeats(Long flightId, LocalDate date, ClassType type, int qty) {
        FlightInventory inv = inventoryRepository
                .findByFlight_FlightIdAndTravelDateAndClassType(flightId, date, type)
                .orElseThrow(() -> {
                    log.error("Unable to release seats. Inventory record missing for flight {} on {} for class {}", flightId, date, type);
                    return new ResourceNotFoundException("Inventory record missing for flight " + flightId + " on " + date + " for class " + type);
                });

        inv.setAvailableSeats(inv.getAvailableSeats() + qty);
        inventoryRepository.save(inv);
        log.info("Released {} seats for flight {} on {} for class {}", qty, flightId, date, type);
    }

    /**
     * Called from the Controller when a new flight is born.
     */
    @Transactional
    public void initializeNewFlightInventory(Flight flight) {
        if (flight == null || flight.getSeats() == null || flight.getSeats().isEmpty()) {
            log.error("Cannot initialize inventory: Flight or Seat Configuration is missing");
            throw new FlightOperationException("Flight seat configuration must be provided to initialize inventory.");
        }
        log.info("Initializing inventory for new Flight: {}", flight.getFlightNo());
        createInventoryForDateRange(flight, LocalDate.now(), LocalDate.now().plusDays(90));
    }

    /**
     * Scheduled Job to keep the 90-day window moving.
     */
    @Scheduled(cron = "0 0 0 * * *")
    @Transactional
    public void rotateDailyInventory() {
        log.info("Starting daily inventory rotation...");
        try {
            inventoryRepository.deleteByTravelDateBefore(LocalDate.now());

            LocalDate ninetiethDay = LocalDate.now().plusDays(90);
            List<Flight> allFlights = flightRepository.findAll();

            if (allFlights.isEmpty()) {
                log.warn("No active flights found to rotate inventory.");
                return;
            }

            for (Flight f : allFlights) {
                createInventoryForDateRange(f, ninetiethDay, ninetiethDay);
            }
            log.info("Daily inventory rotation completed for {} flights.", allFlights.size());
        } catch (Exception e) {
            log.error("Critical error during daily inventory rotation", e);
            throw new FlightOperationException("Failed to rotate daily inventory: " + e.getMessage());
        }
    }

    private void createInventoryForDateRange(Flight flight, LocalDate start, LocalDate end) {
        for (LocalDate date = start; !date.isAfter(end); date = date.plusDays(1)) {
            if (flight.getSeats() != null) {
                for (FlightSeat seatTemplate : flight.getSeats()) {
                    
                    // Logic remains unchanged: check existence to prevent duplicates
                    if (!inventoryRepository.existsByFlightAndTravelDateAndClassType(
                            flight, date, seatTemplate.getClassType())) {

                        FlightInventory inv = FlightInventory.builder()
                                .flight(flight)
                                .travelDate(date)
                                .classType(seatTemplate.getClassType())
                                .availableSeats(seatTemplate.getTotalCapacity())
                                .build();

                        inventoryRepository.save(inv);
                    }
                }
            } else {
                log.warn("No seat configuration found for flight {} while creating inventory for {}", flight.getFlightNo(), date);
            }
        }
    }
}