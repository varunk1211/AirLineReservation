package com.VBook.AirLineReservation.Service;

import com.VBook.AirLineReservation.model.Destination;
import com.VBook.AirLineReservation.repo.DestinationRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DestinationService {

    @Autowired
    private DestinationRepo destinationRepo;

    // Add a new destination
    public Destination addDestination(Destination destination) {
        return destinationRepo.save(destination);
    }

    // Get all destinations
    public List<Destination> getAllDestinations() {
        return destinationRepo.findAll();
    }

    // Get destination by ID
    public Destination getDestinationById(Long id) {
        return destinationRepo.findById(id).orElse(null);
    }

    // Update destination
    public Destination updateDestination(Long id, Destination updatedDestination) {
        Destination existing = destinationRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Destination not found"));
        existing.setName(updatedDestination.getName());
        existing.setDescription(updatedDestination.getDescription());
        existing.setImageUrl(updatedDestination.getImageUrl());
        return destinationRepo.save(existing);
    }

    // Delete destination
    public void deleteDestination(Long id) {
        destinationRepo.deleteById(id);
    }
}