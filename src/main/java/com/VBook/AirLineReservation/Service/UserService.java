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
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    public void registerUser(Users user) {
        // Check if username already exists
        if (userRepository.findByUsername(user.getUsername()) != null) {
            throw new RuntimeException("Username already exists");
        }
        // Encode password
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        // Default role
        user.setRole("USER");
        System.out.println("Registering user: " + user.getUsername());
        userRepository.save(user);
    }
    public List<Users> getAllUsers() {
        return userRepository.findAll();
    }

}
