package com.VBook.AirLineReservation.controler;

import com.VBook.AirLineReservation.Service.FlightService;
import com.VBook.AirLineReservation.model.Flight;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/admin/flights")
public class FlightController {

    @Autowired
    private FlightService flightService;

    @GetMapping("/add")
    public String showAddFlightPage(Model model) {
        model.addAttribute("flight", new Flight());
        return "add-flight";
    }

    @PostMapping("/add")
    public String addFlight(@ModelAttribute Flight flight) {

        flightService.addFlight(flight);

        return "redirect:/admin/flights";
    }

    @GetMapping("/flights")
    public String viewFlights(Model model) {

        List<Flight> flights = flightService.getAllFlights();

        model.addAttribute("flights", flights);

        return "flights";
    }
}