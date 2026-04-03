package com.VBook.AirLineReservation.controler;

import com.VBook.AirLineReservation.Service.OtpService;
import com.VBook.AirLineReservation.Service.UserService;
import com.VBook.AirLineReservation.model.Users;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class UserController {
    @Autowired
    private  OtpService otpService;
    @Autowired
    private UserService userService;
    @PostMapping("/register")
    public String register(Users users,HttpSession session,Model model) {
        try {
            Boolean isVerified = (Boolean) session.getAttribute("verified");
            String sessionEmail = (String) session.getAttribute("email");

            // 🔥 SECURITY CHECK
            if (isVerified == null || !isVerified) {
                model.addAttribute("error", "Please verify OTP first");
                return "register";
            }
            // 🔥 ENSURE EMAIL MATCHES SESSION
            if (!users.getEmail().equals(sessionEmail)) {
                model.addAttribute("error", "Email mismatch");
                return "register";
            }
            users.setEmailVerified(isVerified);
            userService.registerUser(users);
            model.addAttribute("message", "Registration successful! Please check your email to verify your account.");
            return "register-success";

        } catch (RuntimeException e) {
            model.addAttribute("error", e.getMessage());
            return "register";
        }
    }
    @GetMapping("/register")
    public String registerPage() {
        return "register";
    }

    @GetMapping("/send-otp")
    @ResponseBody
    public String sendOtp(@RequestParam String email, HttpSession session) {

        System.out.println("Received OTP request for email: " + email);
        String otp = otpService.generateOtp();

        session.setAttribute("otp", otp);
        session.setAttribute("email", email);
        session.setAttribute("verified", false);

        otpService.sendOtp(email, otp);

        return "SENT";
    }
    @GetMapping("/verify-otp")
    @ResponseBody
    public String verifyOtp(@RequestParam String email,
                            @RequestParam String otp,
                            HttpSession session) {

        String sessionOtp = (String) session.getAttribute("otp");
        String sessionEmail = (String) session.getAttribute("email");

        if (sessionOtp != null &&
                sessionOtp.equals(otp) &&
                sessionEmail.equals(email)) {

            session.setAttribute("verified", true);

            return "SUCCESS";
        }

        return "FAIL";
    }
}
