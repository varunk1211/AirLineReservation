package com.VBook.AirLineReservation.repo;
import com.VBook.AirLineReservation.model.Booking;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookingRepository extends JpaRepository<Booking, Long> {
}