package com.VBook.AirLineReservation.Service;

import com.VBook.AirLineReservation.model.Flight;
import com.VBook.AirLineReservation.repo.FlightRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FlightService {


    @Autowired
    private FlightRepository flightRepo;

    // Add Flight
    public Flight addFlight(Flight flight) {
        return flightRepo.save(flight);
    }

    // Get All Flights
    public List<Flight> getAllFlights() {
        return flightRepo.findAll();
    }

    // Get Flight By ID
    public Flight getFlightById(Long id) {
        return flightRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Flight not found"));
    }

    // Delete Flight
    public void deleteFlight(Long id) {
        flightRepo.deleteById(id);
    }

    // Update Flight
    public Flight updateFlight(Flight flight) {
        return flightRepo.save(flight);
    }
}

