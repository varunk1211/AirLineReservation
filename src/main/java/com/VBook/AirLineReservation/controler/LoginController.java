package com.VBook.AirLineReservation.controler;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {

    @GetMapping("/login")
    public String loginPage() {
        return "login"; // Thymeleaf will render login.html
    }

    @GetMapping("/home")
    public String homePage() {
        return "home"; // create a simple home.html for testing
    }
}