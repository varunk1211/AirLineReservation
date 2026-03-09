package com.VBook.AirLineReservation.repo;

import com.VBook.AirLineReservation.model.Flight;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface FlightRepository extends JpaRepository<Flight, Long> {
    List<Flight> findByDepartureTimeAfter(LocalDateTime time);
}