package com.example.loginregister.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.MailSendException;
import org.springframework.stereotype.Service;

import com.example.loginregister.controller.OtpController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@Service
public class OtpService {

    @Autowired
    private JavaMailSender mailSender;

    private final Map<String, OtpDetails> otpStorage = new HashMap<>(); 
    private static final Logger logger = LoggerFactory.getLogger(OtpController.class);

    private static final String DEFAULT_SUBJECT = "Pevella";
    private static final String SENDER_NAME = "Pevellaa";
    private static final String SENDER_EMAIL = "iperformance91@gmail.com";
    private static final long OTP_EXPIRATION_TIME = 10 * 60 * 1000; 

    public void sendOtp(String email) {
        try {
            String otp = generateOtp();
            long timestamp = System.currentTimeMillis();

            OtpDetails otpDetails = new OtpDetails(otp, timestamp);
            otpStorage.put(email, otpDetails);

            String subject = DEFAULT_SUBJECT;
            String text = "Hi,\n\n" + "Your Pevella verification OTP is " + otp + "." +
                          "  Do not share it with anyone by any means. This is confidential and to be used by you only.\n\n" +
                          "If you receive a suspicious email with a link to update your account information, do not click on the link—instead, report the email to us for investigation.\n\n" +
                          "Regards,\n" + "Pevella";

            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(email);
            message.setSubject(subject);
            message.setText(text);
            message.setFrom(SENDER_EMAIL);

            mailSender.send(message);

            logger.info("\n\nOTP sent successfully to: " + email+"\n");
        } catch (MailSendException e) {
            System.err.println("Error while sending OTP to " + email + ": " + e.getMessage());
            throw new RuntimeException("Could not send OTP email to " + email, e);
        } catch (Exception e) {
            System.err.println("Unexpected error while sending OTP to " + email + ": " + e.getMessage());
            throw new RuntimeException("Unexpected error occurred while sending OTP", e);
        }
    }

    public boolean verifyOtp(String email, String otp) {
        OtpDetails storedOtpDetails = otpStorage.get(email);
        if (storedOtpDetails == null) {
            return false;
        }

        long currentTime = System.currentTimeMillis();
        long otpTimestamp = storedOtpDetails.getTimestamp(); 
        long elapsedTime = currentTime - otpTimestamp;

        if (elapsedTime > OTP_EXPIRATION_TIME) {
            otpStorage.remove(email);
            logger.info("\n\nOTP has expired. Please request a new one.\n");
            return false; 
        } 

        return storedOtpDetails.getOtp().equals(otp);
    }

    private String generateOtp() {
        Random random = new Random();
        int otp = 100000 + random.nextInt(900000); 
        return String.valueOf(otp);
    }

    private static class OtpDetails {
        private final String otp;
        private final long timestamp;

        public OtpDetails(String otp, long timestamp) {
            this.otp = otp;
            this.timestamp = timestamp;
        }

        public String getOtp() {
            return otp;
        }

        public long getTimestamp() {
            return timestamp;
        }
    }
}
