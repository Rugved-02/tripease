package com.tripease.backend.service;

import com.tripease.backend.model.Trip;
import com.tripease.backend.repository.TripRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class TripService {

    @Autowired
    private TripRepository tripRepository;

    public Trip saveTrip(Trip trip) {
        return tripRepository.save(trip);
    }

    // This is the specific part you asked for:
    public List<Trip> getAllTrips() {
        return tripRepository.findAllByOrderByUserIdAscStartDateAsc();
    }
}