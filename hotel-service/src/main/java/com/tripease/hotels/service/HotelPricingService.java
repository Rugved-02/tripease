package com.tripease.hotels.service;

import com.tripease.hotels.model.Hotel;
import com.tripease.hotels.model.RoomInventory;
import com.tripease.hotels.repository.HotelRepository;
import com.tripease.hotels.repository.RoomInventoryRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.DayOfWeek;
import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class HotelPricingService {

    private final HotelRepository hotelRepository;
    private final RoomInventoryRepository inventoryRepository;

    /**
     * Calculates the "Live" price for a specific hotel on a specific date.
     */
    public BigDecimal getLivePrice(Long hotelId, LocalDate stayDate) {
        // 1. Fetch Hotel (for Base Price and Total Rooms)
        Hotel hotel = hotelRepository.findById(hotelId)
                .orElseThrow(() -> new EntityNotFoundException("Hotel not found"));

        // 2. Fetch Inventory (for Availability on that date)
        RoomInventory inventory = inventoryRepository.findByHotelIdAndInventoryDate(hotelId, stayDate)
                .orElseThrow(() -> new EntityNotFoundException("No inventory found for this date"));

        // 3. Perform Dynamic Calculation
        return calculateDynamicRate(
                BigDecimal.valueOf(hotel.getBasePrice()),
                inventory.getAvailableRooms(),
                hotel.getTotalRooms(),
                stayDate
        );
    }

    private BigDecimal calculateDynamicRate(BigDecimal base, int available, int total, LocalDate stayDate) {
        // Calculate Occupancy %
        double occupancy = (double) (total - available) / total;
        long daysToStay = java.time.temporal.ChronoUnit.DAYS.between(LocalDate.now(), stayDate);

        // Best Case Multipliers
        BigDecimal occupancyMultiplier = getOccupancyMultiplier(occupancy);
        BigDecimal windowMultiplier = getBookingWindowMultiplier(daysToStay);
        BigDecimal weekendMultiplier = getWeekendMultiplier(stayDate);

        // Math: Base * Occupancy * Time * DayOfWeek
        return base.multiply(occupancyMultiplier)
                .multiply(windowMultiplier)
                .multiply(weekendMultiplier)
                .setScale(2, RoundingMode.HALF_UP);
    }

    // --- Granular Multiplier Logic ---

    private BigDecimal getOccupancyMultiplier(double occupancy) {
        if (occupancy >= 0.95) return BigDecimal.valueOf(2.0); // Extreme Demand
        if (occupancy >= 0.80) return BigDecimal.valueOf(1.6); // High Demand
        if (occupancy >= 0.50) return BigDecimal.valueOf(1.3); // Moderate
        if (occupancy <= 0.15) return BigDecimal.valueOf(0.85); // Empty House Discount
        return BigDecimal.valueOf(1.0);
    }

    private BigDecimal getBookingWindowMultiplier(long daysToStay) {
        if (daysToStay <= 2)   return BigDecimal.valueOf(1.4); // Last-minute surge
        if (daysToStay <= 7)   return BigDecimal.valueOf(1.2); // Week-of surge
        if (daysToStay >= 60)  return BigDecimal.valueOf(0.8); // Early-bird reward (20% off)
        return BigDecimal.valueOf(1.0);
    }

    private BigDecimal getWeekendMultiplier(LocalDate date) {
        DayOfWeek day = date.getDayOfWeek();
        // Saturday and Sunday stays are premium for leisure hotels
        if (day == DayOfWeek.FRIDAY || day == DayOfWeek.SATURDAY) {
            return BigDecimal.valueOf(1.25);
        }
        return BigDecimal.valueOf(1.0);
    }
}