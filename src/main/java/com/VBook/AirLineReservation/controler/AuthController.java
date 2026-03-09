package com.VBook.AirLineReservation.controler;

import com.VBook.AirLineReservation.model.AuthRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.ui.Model;

@RequestMapping("/auth")
@RequiredArgsConstructor
@RestController
public class AuthController {
    private final AuthenticationManager authenticationManager;


    @PostMapping("/login")
    public String login(AuthRequest request, Model model) {

        try {

            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getUsername(),
                            request.getPassword()));

            return "redirect:/dashboard";

        } catch (Exception e) {

            model.addAttribute("error", "Invalid username or password");
            return "login";
        }
    }
}
