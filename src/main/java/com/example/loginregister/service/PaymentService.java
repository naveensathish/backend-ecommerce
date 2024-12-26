package com.example.loginregister.service;

import org.springframework.stereotype.Service;

import com.example.loginregister.entity.PaymentRequest;
import com.example.loginregister.entity.PaymentVerificationRequest;

@Service
public interface PaymentService {

	String createOrder(PaymentRequest paymentRequest);

	boolean verifyPayment(PaymentVerificationRequest verificationRequest);
}
