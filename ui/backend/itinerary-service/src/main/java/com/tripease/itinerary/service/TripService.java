package com.tripease.itinerary.service;

import com.tripease.itinerary.dto.ActivityRequestDTO;
import com.tripease.itinerary.dto.ActivityResponseDTO;
import com.tripease.itinerary.dto.TripRequestDTO;
import com.tripease.itinerary.dto.TripResponseDTO;
import com.tripease.itinerary.exception.InvalidTripException;
import com.tripease.itinerary.exception.ResourceNotFoundException;
import com.tripease.itinerary.model.ActivityDetail;
import com.tripease.itinerary.model.Trip;
import com.tripease.itinerary.repository.TripRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TripService {

    private final TripRepository tripRepository;

    /**
     * GET /trips/user - Retrieve trips for the logged-in user
     * @param userId extracted from JWT token in the Controller
     */
    public List<TripResponseDTO> getTripsByUserId(String userId) {
        // No Feign call needed; JWT is already validated by security filter
        List<Trip> trips = tripRepository.findByUserIdAndIsEnabledTrue(userId);

        if (trips.isEmpty()) {
            throw new ResourceNotFoundException("No trips found for user id : " + userId);
        }

        return trips.stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    public TripResponseDTO getLatestTripByUserId(String userId) {
        // Calling the repository to find the top 1 record ordered by creation date descending
        Trip trip = tripRepository.findFirstByUserIdAndIsEnabledTrueOrderByCreatedAtDesc(userId)
                .orElseThrow(() -> new ResourceNotFoundException("No trips found for user id : " + userId));

        return mapToResponseDTO(trip);
    }

    /**
     * POST /trips - Create a new trip container
     * @param userId extracted from JWT token in the Controller
     */
    @Transactional
    public TripResponseDTO saveTrip(TripRequestDTO tripRequest, String userId) {
        // 1. Date Validation
        if (tripRequest.getStartDate().isAfter(tripRequest.getEndDate())) {
            throw new InvalidTripException("Start date cannot be after end date.");
        }

        // 2. Map Request -> Entity
        Trip trip = new Trip();
        trip.setTripName(tripRequest.getTripName());
        trip.setStartDate(tripRequest.getStartDate());
        trip.setEndDate(tripRequest.getEndDate());

        // 3. Use email from JWT as the owner identifier
        trip.setUserId(userId);
        trip.setActivities(new ArrayList<>());

        Trip savedTrip = tripRepository.save(trip);
        return mapToResponseDTO(savedTrip);
    }

    /**
     * PATCH /trips/:tripId - Update trip meta
     * Includes a security check to ensure the trip belongs to the email in JWT
     */

    @Transactional
    public TripResponseDTO updateTripMeta(Long tripId, TripRequestDTO updateRequest, String userId) {
        // 1. Fetch the trip
        Trip existingTrip = tripRepository.findById(tripId)
                .orElseThrow(() -> new ResourceNotFoundException("Trip not found with ID: " + tripId));

        // 2. Ownership Check
        if (!existingTrip.getUserId().equals(userId)) {
            throw new InvalidTripException("You do not have permission to update this trip.");
        }

        // 3. Update Trip Metadata
        if (updateRequest.getTripName() != null) existingTrip.setTripName(updateRequest.getTripName());
        if (updateRequest.getStartDate() != null) existingTrip.setStartDate(updateRequest.getStartDate());
        if (updateRequest.getEndDate() != null) existingTrip.setEndDate(updateRequest.getEndDate());

        // 4. Sync Activities (The Fix)
        if (updateRequest.getActivities() != null) {
            // Remove all current activities (orphanRemoval will delete them from DB)
            existingTrip.getActivities().clear();

            // Map and Link new activities
            for (ActivityRequestDTO actDto : updateRequest.getActivities()) {
                ActivityDetail activity = mapToEntity(actDto);

                // CRITICAL: You must set the parent side of the relationship
                activity.setTrip(existingTrip);

                // Add to the list
                existingTrip.getActivities().add(activity);
            }
        }

        // 5. Save the Parent (Cascades to Children)
        Trip savedTrip = tripRepository.save(existingTrip);
        return mapToResponseDTO(savedTrip);
    }

    @Transactional
    public void disableTrip(Long tripId, String userId) {
        // 1. Fetch the trip or throw 404
        Trip trip = tripRepository.findById(tripId)
                .orElseThrow(() -> new ResourceNotFoundException("Trip not found"));

        // 2. Security Check: Does this trip belong to the user?
        if (!trip.getUserId().equals(userId)) {
            throw new RuntimeException("You do not have permission to delete this trip");
        }

        // 3. Perform Soft Delete
        trip.setIsEnabled(false);

        // No need for explicit save if using @Transactional and JPA managed entities
        tripRepository.save(trip);
    }

    private TripResponseDTO mapToResponseDTO(Trip entity) {
        TripResponseDTO dto = new TripResponseDTO();

        dto.setTripId(entity.getTripId());
        dto.setTripName(entity.getTripName());
        dto.setStartDate(entity.getStartDate());
        dto.setEndDate(entity.getEndDate());

        if (entity.getActivities() != null) {
            List<ActivityResponseDTO> activityDTOs = entity.getActivities().stream()
                    .map(this::mapActivityToResponseDTO)
                    .collect(Collectors.toList());
            dto.setActivities(activityDTOs);
        } else {
            dto.setActivities(new ArrayList<>());
        }
        return dto;
    }

    private ActivityResponseDTO mapActivityToResponseDTO(ActivityDetail activity) {
        ActivityResponseDTO activityDTO = new ActivityResponseDTO();
        activityDTO.setActivityId(activity.getActivityId().longValue());
        activityDTO.setTitle(activity.getTitle());
        activityDTO.setType(activity.getType());
        activityDTO.setLocation(activity.getLocation());
        activityDTO.setStartTime(activity.getStartTime());
        activityDTO.setEndTime(activity.getEndTime());
        activityDTO.setNotes(activity.getNotes());
        return activityDTO;
    }

    /**
     * Helper to map individual Activity DTO (from request) to ActivityDetail Entity
     */
    private ActivityDetail mapToEntity(ActivityRequestDTO dto) {
        ActivityDetail detail = new ActivityDetail();
        detail.setTitle(dto.getTitle());
        detail.setType(dto.getType());
        detail.setLocation(dto.getLocation());

        // Ensure you handle the LocalDateTime conversion correctly
        // If your DTO uses ZonedDateTime, use .toLocalDateTime()
        detail.setStartTime(dto.getStartTime().toLocalDateTime());
        detail.setEndTime(dto.getEndTime().toLocalDateTime());

        detail.setNotes(dto.getNotes());
        detail.setIsEnabled(true); // Default to enabled
        return detail;
    }
}