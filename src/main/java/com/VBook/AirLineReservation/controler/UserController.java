package com.VBook.AirLineReservation.controler;

import com.VBook.AirLineReservation.Service.UserService;
import com.VBook.AirLineReservation.model.Users;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    @Autowired
    private UserService userService;
    @PostMapping("/register")
    public String register(@RequestBody Users users) {
        System.out.printf("Received user registration: %s", users.getUsername());
        userService.registerUser(users);
        return "User registered successfully!";
    }
}
