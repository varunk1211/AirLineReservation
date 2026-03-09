package com.VBook.AirLineReservation.Service;

import com.VBook.AirLineReservation.model.*;
import com.VBook.AirLineReservation.repo.BookingRepository;
import com.VBook.AirLineReservation.repo.FlightRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class BookingService {

    private final BookingRepository bookingRepository;
    private final FlightRepository flightRepository;

    public BookingService(BookingRepository bookingRepository,
                          FlightRepository flightRepository) {
        this.bookingRepository = bookingRepository;
        this.flightRepository = flightRepository;
    }

    public Booking createBooking(Long flightId,
                                 Users user,
                                 List<Passenger> passengers) {

        Flight flight = flightRepository.findById(flightId)
                .orElseThrow(() -> new RuntimeException("Flight not found"));

        int passengerCount = passengers.size();

        if (flight.getAvailableSeats() < passengerCount) {
            throw new RuntimeException("Not enough seats available");
        }

        flight.setAvailableSeats(
                flight.getAvailableSeats() - passengerCount
        );

        Booking booking = new Booking();

        booking.setUser(user);
        booking.setFlight(flight);
        booking.setBookingDate(LocalDateTime.now());
        booking.setBookingStatus("CONFIRMED");

        booking.setBookingReference(
                "VBK-" + UUID.randomUUID().toString().substring(0,6)
        );

        booking.setTotalAmount(
                passengerCount * flight.getPrice()
        );

        for (Passenger p : passengers) {
            p.setBooking(booking);
        }

        booking.setPassengers(passengers);

        flightRepository.save(flight);

        return bookingRepository.save(booking);
    }

}