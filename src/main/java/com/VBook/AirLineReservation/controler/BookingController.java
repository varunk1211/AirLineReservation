package com.VBook.AirLineReservation.controler;

import com.VBook.AirLineReservation.Service.BookingService;
import com.VBook.AirLineReservation.model.Booking;
import com.VBook.AirLineReservation.model.Passenger;
import com.VBook.AirLineReservation.model.Users;
import com.VBook.AirLineReservation.repo.FlightRepository;
import com.VBook.AirLineReservation.repo.UserRepo;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.util.List;

@Controller
public class BookingController {

    private final BookingService bookingService;
    private final FlightRepository flightRepository;
    private final UserRepo userRepository;

    public BookingController(BookingService bookingService,
                             FlightRepository flightRepository,
                             UserRepo userRepository) {
        this.bookingService = bookingService;
        this.flightRepository = flightRepository;
        this.userRepository = userRepository;
    }

    @PostMapping("/book/{flightId}")
    public String bookFlight(@PathVariable Long flightId,
                             Principal principal,
                             @RequestParam List<String> passengerNames) {

        Users user = userRepository
                .findByUsername(principal.getName())
                .orElseThrow();

        List<Passenger> passengers =
                passengerNames.stream().map(name -> {
                    Passenger p = new Passenger();
                    p.setFullName(name);
                    return p;
                }).toList();

        Booking booking =
                bookingService.createBooking(flightId,user,passengers);

        return "redirect:/ticket/" + booking.getId();
    }
    @GetMapping("/book/{flightId}")
    public String showBookingPage(@PathVariable Long flightId, Model model) {

        var flight = flightRepository.findById(flightId)
                .orElseThrow(() -> new RuntimeException("Flight not found"));

        model.addAttribute("flight", flight);

        return "booking-page"; // booking.html
    }
}
