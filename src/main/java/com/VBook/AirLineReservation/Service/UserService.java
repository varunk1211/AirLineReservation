package com.VBook.AirLineReservation.Service;

import com.VBook.AirLineReservation.model.AuthRequest;
import com.VBook.AirLineReservation.model.Users;
import com.VBook.AirLineReservation.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    @Autowired
    private UserRepo userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    public Users saveUser(Users user) {

        return userRepository.save(user);
    }

    public Users findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public String registerUser(Users user) {
        // Check if username exists
        if (userRepository.findByUsername(user.getUsername()) != null) {
            return "Username already exists!";
        }
        // Encode password
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        // Save user
        System.out.printf("Registering user: %s with role: %s%n", user.getUsername(), user.getPassword());
        userRepository.save(user);
        return "User registered successfully";
    }
    public List<Users> getAllUsers() {
        return userRepository.findAll();
    }

}
