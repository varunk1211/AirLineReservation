package com.VBook.AirLineReservation.repo;

import com.VBook.AirLineReservation.model.Flight;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FlightRepo extends JpaRepository<Flight, Integer> {

}
