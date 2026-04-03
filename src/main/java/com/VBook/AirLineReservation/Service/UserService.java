package com.VBook.AirLineReservation.Service;

import com.VBook.AirLineReservation.model.AuthRequest;
import com.VBook.AirLineReservation.model.Users;
import com.VBook.AirLineReservation.repo.UserRepo;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Service
public class UserService {
    @Autowired
    private UserRepo userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private OtpService otpService;

    public Users saveUser(Users user) {

        return userRepository.save(user);
    }

    public Users findByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    public void registerUser(Users user) {
        // Check if username already exists
        if (userRepository.findByUsername(user.getUsername()).isPresent()) {
            throw new RuntimeException("Username already exists");
        }
        // Encode password
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        // Default role
        user.setRole("USER");
        // Generate verification token
        System.out.println("Registering user: " + user.getUsername());
        userRepository.save(user);
        // Send verification
    }

    public String generateVerificationToken() {
        return java.util.UUID.randomUUID().toString();
    }
    public List<Users> getAllUsers() {
        return userRepository.findAll();
    }

}
