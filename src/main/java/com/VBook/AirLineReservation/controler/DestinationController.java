package com.VBook.AirLineReservation.controler;

import com.VBook.AirLineReservation.Service.DestinationService;
import com.VBook.AirLineReservation.model.Destination;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/admin/destinations")
public class DestinationController {

    @Autowired
    private DestinationService destinationService;

    // List all destinations
    @GetMapping
    public String viewDestinations(Model model) {
        List<Destination> destinations = destinationService.getAllDestinations();
        model.addAttribute("destinations", destinations);
        return "destinations"; // Thymeleaf page to display as cards
    }

    // Show add destination page
    @GetMapping("/add")
    public String showAddPage(Model model) {
        model.addAttribute("destination", new Destination());
        return "add-destination";
    }

    // Add new destination
    @PostMapping("/add")
    public String addDestination(@ModelAttribute Destination destination) {
        destinationService.addDestination(destination);
        return "redirect:/admin/destinations";
    }

    // Show edit page
    @GetMapping("/edit/{id}")
    public String showEditPage(@PathVariable Long id, Model model) {
        Destination destination = destinationService.getDestinationById(id);
        model.addAttribute("destination", destination);
        return "edit-destination";
    }

    // Update destination
    @PostMapping("/edit/{id}")
    public String updateDestination(@PathVariable Long id, @ModelAttribute Destination destination) {
        destinationService.updateDestination(id, destination);
        return "redirect:/admin/destinations";
    }

    // Delete destination
    @GetMapping("/delete/{id}")
    public String deleteDestination(@PathVariable Long id) {
        destinationService.deleteDestination(id);
        return "redirect:/admin/destinations";
    }
}


