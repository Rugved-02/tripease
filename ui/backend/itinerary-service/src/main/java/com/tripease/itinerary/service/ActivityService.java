package com.tripease.itinerary.service;

import com.tripease.itinerary.dto.ActivityRequestDTO;
import com.tripease.itinerary.dto.ActivityResponseDTO;
import com.tripease.itinerary.model.ActivityDetail;
import com.tripease.itinerary.model.Trip;
import com.tripease.itinerary.repository.ActivityRepository;
import com.tripease.itinerary.repository.TripRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ActivityService {

    private final ActivityRepository activityRepository;
    private final TripRepository tripRepository;

    @Transactional
    public ActivityResponseDTO addActivity(ActivityRequestDTO request) {
        // 1. Find the parent trip - CRITICAL: This prevents the "id must not be null" error
        Trip trip = tripRepository.findById(request.getTripId())
                .orElseThrow(() -> new RuntimeException("Trip not found with ID: " + request.getTripId()));

        // 2. Map RequestDTO -> Entity
        ActivityDetail activity = new ActivityDetail();
        activity.setTitle(request.getTitle());

        // Use your Enum's fromString method for safety
        activity.setType(request.getType());

        activity.setLocation(request.getLocation());

        // Mapping times
        if (request.getStartTime() != null) {
            activity.setStartTime(request.getStartTime().toLocalDateTime());
        }
        if (request.getEndTime() != null) {
            activity.setEndTime(request.getEndTime().toLocalDateTime());
        }

        activity.setNotes(request.getNotes());
        activity.setIsEnabled(true); // Default value

        // 3. Establish Relationship
        activity.setTrip(trip);

        // 4. Save
        ActivityDetail savedActivity = activityRepository.save(activity);
        return mapToResponseDTO(savedActivity);
    }

    public List<ActivityResponseDTO> getActivitiesByTripId(Long tripId) {
        return activityRepository.findByTripTripId(tripId).stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    private ActivityResponseDTO mapToResponseDTO(ActivityDetail entity) {
        ActivityResponseDTO dto = new ActivityResponseDTO();
        dto.setActivityId(entity.getActivityId());
        dto.setTitle(entity.getTitle());
        dto.setType(entity.getType());
        dto.setLocation(entity.getLocation());
        dto.setStartTime(entity.getStartTime());
        dto.setEndTime(entity.getEndTime());
        dto.setNotes(entity.getNotes());
        // Return tripId so frontend can verify the link
        dto.setTripId(entity.getTrip().getTripId());
        return dto;
    }
}