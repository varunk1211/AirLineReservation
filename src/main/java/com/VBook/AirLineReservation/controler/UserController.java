package com.VBook.AirLineReservation.controler;

import com.VBook.AirLineReservation.Service.UserService;
import com.VBook.AirLineReservation.model.Users;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.ui.Model;

@Controller
public class UserController {

    @Autowired
    private UserService userService;
    @PostMapping("/register")
    public String register(Users users,Model model) {
        try {
            userService.registerUser(users);
            return "redirect:/login";
        } catch (RuntimeException e) {
            model.addAttribute("error", e.getMessage());
            return "register";
        }
    }
    @GetMapping("/register")
    public String registerPage() {
        return "register";
    }

}
