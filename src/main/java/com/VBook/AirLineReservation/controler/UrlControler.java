package com.VBook.AirLineReservation.controler;

import com.VBook.AirLineReservation.model.Booking;
import com.VBook.AirLineReservation.repo.BookingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class UrlControler {
    @Autowired
    BookingRepository bookingRepository;

    @GetMapping("/ticket/{bookingId}")
    public String viewTicket(@PathVariable Long bookingId,
                             Model model){

        Booking booking =
                bookingRepository.findById(bookingId)
                        .orElseThrow();

        model.addAttribute("booking", booking);

        return "ticket";
    }
}
