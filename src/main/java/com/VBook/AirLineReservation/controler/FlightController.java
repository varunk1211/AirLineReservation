package com.VBook.AirLineReservation.controler;

import com.VBook.AirLineReservation.Service.FlightService;
import com.VBook.AirLineReservation.model.DestinationFlightResponse;
import com.VBook.AirLineReservation.model.Flight;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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

        return "flights";
    }

    @GetMapping("/flights")
    public String viewFlights(Model model) {

        List<Flight> flights = flightService.findByDepartureTimeAfter();

        model.addAttribute("flights", flights);

        return "flights";
    }

    @GetMapping("/flights/search")
    public String getFlightsByDestination(@RequestParam("dest") Long id, Model model) {

        DestinationFlightResponse response = flightService.getFlightsByDestination(id);

        model.addAttribute("data", response);

        return "flights-by-destination";
    }
}