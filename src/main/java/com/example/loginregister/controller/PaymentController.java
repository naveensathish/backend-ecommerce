package com.example.loginregister.controller;

import com.example.loginregister.entity.PaymentRequest;
import com.example.loginregister.security.JwtUtil;
import com.razorpay.RazorpayClient;
import com.razorpay.Order;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/payment")
public class PaymentController {

	@Value("${razorpay.key}")
	private String razorpayKey;

	@Value("${razorpay.secret}")
	private String razorpaySecret;

	private RazorpayClient razorpayClient;

	private final JwtUtil jwtUtil;

	public PaymentController(JwtUtil jwtUtil) {
		this.jwtUtil = jwtUtil;
	}

	@PostConstruct
	public void init() throws Exception {
		System.out
				.println("\nInitializing Razorpay Client with Key: " + razorpayKey + " and Secret: " + razorpaySecret);
		this.razorpayClient = new RazorpayClient(razorpayKey, razorpaySecret);
	}

	@PostMapping("/createOrder")
	public ResponseEntity<Map<String, Object>> createOrder(@RequestBody PaymentRequest paymentRequest,
			@RequestHeader("Authorization") String authorizationHeader) {
		try {
			String jwtToken = authorizationHeader.substring(7);
			String username = jwtUtil.extractUsername(jwtToken);

			if (username == null || !jwtUtil.validateToken(jwtToken, username)) {
				return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "Invalid or expired token"));
			}

			Map<String, Object> orderRequest = new HashMap<>();
			orderRequest.put("amount", paymentRequest.getAmount() * 100);
			orderRequest.put("currency", paymentRequest.getCurrency());
			orderRequest.put("receipt", paymentRequest.getReceipt());
			JSONObject jsonOrderRequest = new JSONObject(orderRequest);
			Order order = razorpayClient.Orders.create(jsonOrderRequest);
			Map<String, Object> response = new HashMap<>();
			response.put("id", order.get("id"));
			response.put("amount", order.get("amount"));
			response.put("currency", order.get("currency"));

			return ResponseEntity.ok(response);

		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(Map.of("error", "Error creating Razorpay order"));
		}
	}
}
