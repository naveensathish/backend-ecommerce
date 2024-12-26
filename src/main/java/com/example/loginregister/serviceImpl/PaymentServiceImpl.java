package com.example.loginregister.serviceImpl;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.example.loginregister.entity.PaymentRequest;
import com.example.loginregister.entity.PaymentVerificationRequest;
import com.example.loginregister.service.PaymentService;
import com.razorpay.Order;
import com.razorpay.RazorpayClient;

@Service
public class PaymentServiceImpl implements PaymentService {

	@Value("${razorpay.key}")
	private String apiKey;

	@Value("${razorpay.secret}")
	private String apiSecret;

	private final RazorpayClient razorpayClient;

	public PaymentServiceImpl(@Value("${razorpay.key}") String apiKey,
			@Value("${razorpay.secret}") String apiSecret) {
		try {
			this.razorpayClient = new RazorpayClient(apiKey, apiSecret);
		} catch (Exception e) {
			throw new RuntimeException("Failed to initialize Razorpay client", e);
		}
	}

	@Override
	public String createOrder(PaymentRequest paymentRequest) {
		try {
			JSONObject orderRequest = new JSONObject();
			orderRequest.put("amount", (int) (paymentRequest.getAmount() * 100));
			orderRequest.put("currency", paymentRequest.getCurrency());
			orderRequest.put("receipt", paymentRequest.getReceipt());
			orderRequest.put("payment_capture", 1);

			Order order = razorpayClient.Orders.create(orderRequest);
			return order.toString();
		} catch (Exception e) {
			throw new RuntimeException("Error while creating payment order", e);
		}
	}

	@Override
	public boolean verifyPayment(PaymentVerificationRequest verificationRequest) {
		try {
			String data = verificationRequest.getOrderId() + "|" + verificationRequest.getPaymentId();
			String providedSignature = verificationRequest.getSignature();

			String expectedSignature = generateSignature(data, apiSecret);

			if (!expectedSignature.equals(providedSignature)) {
				throw new RuntimeException("Invalid payment signature");
			}

			return true;
		} catch (Exception e) {
			throw new RuntimeException("Error while verifying payment", e);
		}
	}

	private String generateSignature(String data, String secret) {
		try {
			Mac sha256_HMAC = Mac.getInstance("HmacSHA256");
			SecretKeySpec secret_key = new SecretKeySpec(secret.getBytes(), "HmacSHA256");
			sha256_HMAC.init(secret_key);
			byte[] hash = sha256_HMAC.doFinal(data.getBytes());
			StringBuilder hexString = new StringBuilder();

			for (byte b : hash) {
				String hex = Integer.toHexString(0xff & b);
				if (hex.length() == 1) {
					hexString.append('0');
				}
				hexString.append(hex);
			}

			return hexString.toString();
		} catch (Exception e) {
			throw new RuntimeException("Error while generating signature", e);
		}
	}
}
