package com.tripease.flight.service;

import com.tripease.flight.dto.FlightResponseDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import com.tripease.flight.dto.FlightRequestDTO;
import com.tripease.flight.model.Flight;
import com.tripease.flight.model.FlightSeat;
import com.tripease.flight.repo.FlightRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class FlightServiceImpl implements FlightService {
    
    private final FlightRepository flightRepository;
    private final InventoryManagementService inventoryManagementService;

    @Transactional
    public FlightResponseDTO registerNewFlight(FlightRequestDTO dto) {
        // 1. Build the Flight object (Parent)
        // Note: seats list will be empty because of @Builder.Default in your Entity
        Flight flight = Flight.builder()
                .flightNo(dto.flightNo())
                .airline(dto.airline())
                .depPlace(dto.depPlace())
                .arrPlace(dto.arrPlace())
                .depTime(dto.depTime())
                .arrTime(dto.arrTime())
                .build();

        // 2. Map DTO seats to Entities and LINK the parent flight
        if (dto.seats() != null) {
            List<FlightSeat> seatEntities = dto.seats().stream()
                    .map(seatDto -> FlightSeat.builder()
                            .classType(seatDto.classType())
                            .basePrice(seatDto.basePrice())
                            .totalCapacity(seatDto.totalCapacity())
                            .flight(flight) // <--- CRITICAL: This sets the Foreign Key (flight_id)
                            .build())
                    .toList();

            // 3. Set the list into the flight object
            flight.setSeats(seatEntities);
        }

        // 3. Save and return
        Flight saved = flightRepository.save(flight);

        // 4. Initialize inventory (The "Business Rule" step)
        inventoryManagementService.initializeNewFlightInventory(saved);
log.info("Flight {} saved with {} seat classes", saved.getFlightNo(), saved.getSeats().size());

        return mapToResponseDTO(saved);
    }

private FlightResponseDTO mapToResponseDTO(Flight flight) {
    return FlightResponseDTO.builder()
            .flightNo(flight.getFlightNo())
            .airline(flight.getAirline())
            .depPlace(flight.getDepPlace())
            .arrPlace(flight.getArrPlace())
            .depTime(flight.getDepTime())
            .arrTime(flight.getArrTime())
            .seats(flight.getSeats().stream()
                    .map(s -> FlightResponseDTO.SeatConfigDTO.builder()
                            .classType(s.getClassType())
                            .basePrice(s.getBasePrice())
                            .totalCapacity(s.getTotalCapacity())
                            .build())
                    .toList())
            .build();
}

}