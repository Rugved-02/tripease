package com.tripease.hotels.service;

import com.tripease.hotels.model.Hotel;
import com.tripease.hotels.model.RoomInventory;
import com.tripease.hotels.repository.HotelRepository;
import com.tripease.hotels.repository.RoomInventoryRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class InventoryManagementService {

    private final RoomInventoryRepository inventoryRepository;
    private final HotelRepository hotelRepository;

    @Transactional
    public boolean updateInventory(Long hotelId, LocalDate checkIn, LocalDate checkOut, String action) {
        log.info("Request to {} inventory for Hotel ID: {} ({} to {})", action, hotelId, checkIn, checkOut);

        if ("BOOK".equalsIgnoreCase(action)) {
            long duration = ChronoUnit.DAYS.between(checkIn, checkOut);
            int updatedRows = inventoryRepository.decrementAvailability(hotelId, checkIn, checkOut);

            boolean success = updatedRows == duration;
            if (!success) {
                log.error("Failed to book inventory for Hotel ID: {}. Expected {} days, but updated {} rows.", hotelId, duration, updatedRows);
            }
            return success;
        } else if ("CANCEL".equalsIgnoreCase(action)) {
            inventoryRepository.incrementAvailability(hotelId, checkIn, checkOut);
            log.info("Inventory restored for Hotel ID: {} following cancellation.", hotelId);
            return true;
        }

        log.warn("Unknown inventory action received: {}", action);
        return false;
    }

    /**
     * Called from HotelService when a new hotel is created/registered.
     * Fills the full 180-day window immediately.
     */
    @Transactional
    public void initializeNewHotelInventory(Hotel hotel) {
        log.info("Initializing baseline inventory for Hotel: {}", hotel.getHotelName());
        createInventoryForDateRange(hotel, LocalDate.now(), LocalDate.now().plusDays(180));
    }

    /**
     * Scheduled Job to keep the 180-day window moving.
     * Runs at midnight daily.
     */
    @Scheduled(cron = "0 0 0 * * *")
    @Transactional
    public void rotateDailyInventory() {
        LocalDate today = LocalDate.now();
        inventoryRepository.deleteByInventoryDateBefore(today);
        log.info("Purged expired hotel inventory before {}", today);

        LocalDate checkDate = today.plusDays(180);
        List<Hotel> activeHotels = hotelRepository.findAll().stream()
                .filter(Hotel::isRegistered)
                .toList();

        for (Hotel hotel : activeHotels) {
            // Reusing the range logic for just the 180th day
            createInventoryForDateRange(hotel, checkDate, checkDate);
        }
        log.info("Rotated inventory window to include {}", checkDate);
    }

    /**
     * Core logic used by both the Manual trigger and the Scheduled task.
     */
    private void createInventoryForDateRange(Hotel hotel, LocalDate start, LocalDate end) {
        for (LocalDate date = start; !date.isAfter(end); date = date.plusDays(1)) {

            // Prevent duplicate entries if the job is re-run
            if (!inventoryRepository.existsByHotelIdAndInventoryDate(hotel.getHotelId(), date)) {
                RoomInventory inv = RoomInventory.builder()
                        .hotelId(hotel.getHotelId())
                        .inventoryDate(date)
                        .availableRooms(hotel.getTotalRooms())
                        .priceOnDate(hotel.getBasePrice())
                        .build();

                inventoryRepository.save(inv);
            }
        }
    }
}