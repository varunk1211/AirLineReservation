package com.VBook.AirLineReservation.controler;

import com.VBook.AirLineReservation.Service.UserService;
import com.VBook.AirLineReservation.model.Users;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Controller
public class DashboardController {
    @Autowired
    private UserService usersService;

    @GetMapping("/dashboard")
    public String dashboard(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        Users user = usersService.findByUsername(userDetails.getUsername());
        model.addAttribute("user", user);
        model.addAttribute("options", List.of("Search Flights", "View Bookings", "Manage Profile"));
        model.addAttribute("pageTitle", "Dashboard - VBook Airlines");
        return "layout";
    }
}
