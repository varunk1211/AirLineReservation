package com.VBook.AirLineReservation.repo;

import com.VBook.AirLineReservation.model.Flight;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface FlightRepository extends JpaRepository<Flight, Long> {
    List<Flight> findByDepartureTimeAfter(LocalDateTime time);

    @Query("SELECT f FROM Flight f WHERE " +
            "LOWER(f.source) LIKE LOWER(CONCAT('%', :source, '%')) AND " +
            "LOWER(f.destination) LIKE LOWER(CONCAT('%', :destination, '%')) AND " +
            "f.departureTime >= CURRENT_TIMESTAMP")
    List<Flight> searchFlights(@Param("source") String source,
                               @Param("destination") String destination);
    List<Flight> findByDestinationIgnoreCase(String destination);
}