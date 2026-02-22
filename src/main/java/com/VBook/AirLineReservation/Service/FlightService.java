package com.VBook.AirLineReservation.Service;

import com.VBook.AirLineReservation.model.Flight;
import com.VBook.AirLineReservation.repo.FlightRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FlightService {

    @Autowired
    private FlightRepo flightRepo;

    public Flight addFlight(Flight flight) {
        return flightRepo.save(flight);
    }

    public List<Flight> getAllFlights() {
        return flightRepo.findAll();
    }

}

