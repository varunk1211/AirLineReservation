package com.VBook.AirLineReservation.Service;

import com.VBook.AirLineReservation.model.Destination;
import com.VBook.AirLineReservation.model.DestinationFlightResponse;
import com.VBook.AirLineReservation.model.Flight;
import com.VBook.AirLineReservation.repo.DestinationRepo;
import com.VBook.AirLineReservation.repo.FlightRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FlightService {

    @Autowired
    private DestinationRepo destinationRepository;
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

    public List<Flight>  findByDepartureTimeAfter() {
        return flightRepo.findByDepartureTimeAfter(java.time.LocalDateTime.now());
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

    public List<Flight> searchFlights(String source, String destination) {
        System.out.printf("getting the source and destination in service %s %s \n", source, destination);
        System.out.printf(flightRepo.searchFlights(source, destination).stream().map(a -> a.getSource() + " " + a.getDestination()).toList().toString());
        return flightRepo.searchFlights(source, destination);
    }

    public DestinationFlightResponse getFlightsByDestination(Long destinationId) {

        Destination destination = destinationRepository.findById(destinationId)
                .orElseThrow(() -> new RuntimeException("Destination not found"));
        // 🔥 Match by name (STRING)
        List<Flight> flights = flightRepo
                .findByDestinationIgnoreCase(destination.getName());

        System.out.printf("Flights for destination %s: %s\n", destination.getName(), flights.stream().map(f -> f.getSource() + "->" + f.getDestination()).toList());
        return new DestinationFlightResponse(
                destination.getName(),
                destination.getDescription(),
                destination.getImageUrl(),
                flights
        );
    }

}

