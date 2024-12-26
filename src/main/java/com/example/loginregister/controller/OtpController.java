package com.example.loginregister.controller;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.loginregister.service.AuthService;
import com.example.loginregister.service.OtpService;


@RestController
@RequestMapping("/api/otp")
@CrossOrigin(origins = {"http://localhost:5000", "http://localhost:5001", "http://localhost:3000", "http://localhost:3001"})
public class OtpController {
	
    private static final Logger logger = LoggerFactory.getLogger(OtpController.class);

    @Autowired
    private OtpService otpService;
    
    @Autowired
    private AuthService authService;
    
    @PostMapping("/send")
    public ResponseEntity<String> sendOtp(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        logger.info("Received OTP request for email: {}", email); 
        
        String username = authService.getUsernameByEmail(email);
        if (username == null) {
            logger.warn("No account linked to this email: {}", email);
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                                 .body("No accounts linked to this email. Kindly check."); 
        }

        try {
            otpService.sendOtp(email);
            logger.info("\n\nOTP sent successfully to email: {}", email+"\n"); 
            return ResponseEntity.ok("\n\nOTP sent to email\n\n");
        } catch (Exception e) {
            logger.error("\n\nError sending OTP to email: {}", email, e); 
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("\nFailed to send OTP\n"); 
        }
    }

    @PostMapping("/verify")
    public ResponseEntity<String> verifyOtp(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        String otp = request.get("otp");
        logger.info("Received OTP verification request for email: {} with OTP: {}", email, otp);

        try {
            boolean isValid = otpService.verifyOtp(email, otp);
            if (isValid) {
                logger.info("\n\nOTP verified successfully for email: {}", email+"\n"); 
                return ResponseEntity.ok("\n\nOTP verified successfully");
            } else {
                logger.warn("\n\nInvalid OTP for email: {}", email); 
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid OTP or OTP expired. Please request a new one.");
            }
        } catch (Exception e) {
            logger.error("\n\nError verifying OTP for email: {}", email, e); 
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error verifying OTP");
        }
    }
}