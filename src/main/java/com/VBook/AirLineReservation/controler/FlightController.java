package com.VBook.AirLineReservation.controler;

import com.VBook.AirLineReservation.Service.FlightService;
import com.VBook.AirLineReservation.model.Flight;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin/flights")
public class FlightController {

    FlightService flightService;
    @PostMapping("/add")
    public String addFlight(@RequestBody Flight flight) {
         ResponseEntity.ok(flightService.addFlight(flight));
        return "redirect:/admin/flights";
    }
}
