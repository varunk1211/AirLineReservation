package com.VBook.AirLineReservation.controler;

import com.VBook.AirLineReservation.Service.FlightService;
import com.VBook.AirLineReservation.model.Flight;
import com.VBook.AirLineReservation.repo.FlightRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.ui.Model;

import java.util.List;

@Controller
@RequestMapping("/api")
public class SearchController {

    @Autowired
    private FlightRepository flightRepo;
    @Autowired
    private FlightService flightService;

    @GetMapping("/search")
    public String searchFlights(@RequestParam String source,
                                @RequestParam String destination,
                                Model model) {

        List<Flight> flights =
                flightService.searchFlights(source, destination);

        model.addAttribute("flights", flights);

        return "flights";
    }
}