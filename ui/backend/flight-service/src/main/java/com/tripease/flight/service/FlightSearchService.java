package com.tripease.flight.service;

import com.tripease.flight.dto.FlightSearchResponseDTO;
import com.tripease.flight.model.Flight;
import com.tripease.flight.model.FlightInventory;
import com.tripease.flight.model.FlightSeat;
import com.tripease.flight.repo.FlightRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FlightSearchService {

    private final FlightRepository flightRepository;

    public List<FlightSearchResponseDTO> searchAvailableFlights(String from, String to, LocalDate date, int passengers) {
        // Query returns List of Object arrays [Flight, FlightSeat, FlightInventory]
        List<Object[]> results = flightRepository.searchAvailableFlights(from, to, date, passengers);

        return results.stream().map(record -> {
            Flight f = (Flight) record[0];
            FlightSeat s = (FlightSeat) record[1];
            FlightInventory i = (FlightInventory) record[2];

            // Logic: If no inventory record exists yet, the flight is empty (Full Capacity)
            int currentAvailable = (i != null) ? i.getAvailableSeats() : s.getTotalCapacity();

            // Calculate Dynamic Price immediately
            BigDecimal finalPrice = calculatePrice(s.getBasePrice(), currentAvailable, s.getTotalCapacity(), date);

            // Use Lombok Builder to create the Record
            return FlightSearchResponseDTO.builder()
                    .flightId(f.getFlightId())
                    .flightNo(f.getFlightNo())
                    .airline(f.getAirline())
                    .depPlace(f.getDepPlace())
                    .arrPlace(f.getArrPlace())
                    .depTime(f.getDepTime())
                    .arrTime(f.getArrTime())
                    .classType(s.getClassType())
                    .availableSeats(currentAvailable)
                    .price(finalPrice)
                    .build();
        }).collect(Collectors.toList());
    }

    public List<FlightSearchResponseDTO> getTop10FlightOfferings() {
        return flightRepository.findAll(PageRequest.of(0, 10))
                .getContent()
                .stream()
                .flatMap(flight -> flight.getSeats().stream()
                        .map(seat -> FlightSearchResponseDTO.builder()
                                .flightId(flight.getFlightId())
                                .flightNo(flight.getFlightNo())
                                .airline(flight.getAirline())
                                .depPlace(flight.getDepPlace())
                                .arrPlace(flight.getArrPlace())
                                .depTime(flight.getDepTime())
                                .arrTime(flight.getArrTime())
                                // Direct mapping from the FlightSeat entity
                                .classType(seat.getClassType())
                                .availableSeats(seat.getTotalCapacity())
                                .price(seat.getBasePrice())
                                .build()
                        )
                )
                .limit(10) // Ensures we only return 10 total rows/offerings
                .collect(Collectors.toList());
    }

    private BigDecimal calculatePrice(BigDecimal base, int available, int total, LocalDate travelDate) {
        double occupancy = (double) (total - available) / total;
        long daysToFlight = java.time.temporal.ChronoUnit.DAYS.between(LocalDate.now(), travelDate);

        // Start with the base price
        BigDecimal demandMultiplier = getOccupancyMultiplier(occupancy);
        BigDecimal urgencyMultiplier = getUrgencyMultiplier(daysToFlight);

        // Final Calculation: Base * Demand * Urgency
        BigDecimal finalPrice = base.multiply(demandMultiplier).multiply(urgencyMultiplier);

        // Industry Standard: Round to 2 decimal places (Half Up)
        return finalPrice.setScale(2, RoundingMode.HALF_UP);
    }

    // Case 1: Granular Occupancy Tiers
    private BigDecimal getOccupancyMultiplier(double occupancy) {
        if (occupancy >= 0.95) return BigDecimal.valueOf(2.0); // Sold out soon! 2x price
        if (occupancy >= 0.80) return BigDecimal.valueOf(1.5); // High demand
        if (occupancy >= 0.60) return BigDecimal.valueOf(1.3); // Moderate-High
        if (occupancy >= 0.40) return BigDecimal.valueOf(1.1); // Moderate
        if (occupancy <= 0.10) return BigDecimal.valueOf(0.9); // Early bird / Empty flight discount
        return BigDecimal.valueOf(1.0); // Standard
    }

    // Case 2: Advanced Booking Windows (Urgency)
    private BigDecimal getUrgencyMultiplier(long daysToFlight) {
        if (daysToFlight <= 1)  return BigDecimal.valueOf(1.8); // Same-day/Next-day premium
        if (daysToFlight <= 3)  return BigDecimal.valueOf(1.5); // Very urgent
        if (daysToFlight <= 7)  return BigDecimal.valueOf(1.3); // One week out
        if (daysToFlight <= 14) return BigDecimal.valueOf(1.1); // Two weeks out
        if (daysToFlight >= 60) return BigDecimal.valueOf(0.85); // Advanced booking reward (15% off)
        return BigDecimal.valueOf(1.0);
    }
}
