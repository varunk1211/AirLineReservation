package com.VBook.AirLineReservation.repo;
import com.VBook.AirLineReservation.model.Booking;
import com.VBook.AirLineReservation.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findByUser(Users user);
}