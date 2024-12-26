package com.example.loginregister.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import org.springframework.stereotype.Service;

@Service
public class CartItemNotificationService {
	
	private static final Logger logger = LoggerFactory.getLogger(SellerService.class);

	@KafkaListener(topics = "user-login-topic", groupId = "user-login-group")
	public void listenForUserLogin(String message) {
		logger.info("\n\n!Received Kafka message: " + message);
	}

	@KafkaListener(topics = "user-registration-topic", groupId = "user-registration-group")
	public void listenForUserRegistration(String message) {
		logger.info("\n\n!Received Kafka message: " + message);
	}

	@KafkaListener(topics = "product-display-topic", groupId = "product-display-group")
	public void listenForProductDisplay(String message) {
		logger.info("\\n\\n!!Received Kafka message from product-display-topic: " + message);
	}

	@KafkaListener(topics = "seller-login-topic", groupId = "seller-login-group")
	public void listenForSellerLogin(String message) {
		logger.info("\\n\\n!!Received Kafka message from seller-login-topic: " + message);
	}

	@KafkaListener(topics = "seller-onboard-topic", groupId = "seller-onboard-group")
	public void listenForSellerOnboard(String message) {
		logger.info("\\n\\n!!Received Kafka message from seller-onboard-topic: " + message);
	}

	@KafkaListener(topics = "product-addition-topic", groupId = "product-addition-group")
	public void listenForProductAddition(String message) {
		logger.info("\n\n!Received Kafka message from product-addition-topic: " + message);
	}

	@KafkaListener(topics = "cart-items-topic", groupId = "cart-notifications")
	public void listenForCartItemNotifications(String message) {
		logger.info("\n\n!!Received Kafka message  from cart-items-topic: " + message);
		sendPushNotification(message, "Cart Item Update");
	}

	@KafkaListener(topics = "wishlist-items-topic", groupId = "wishlist-notifications")
	public void listenForWishlistItemNotifications(String message) {
		logger.info("\n\n!!Received Kafka message from wishlist-items-topic: " + message);
		sendPushNotification(message, "Wishlist Item Update");
	}

	private void sendPushNotification(String message, String title) {
		Notification notification = Notification.builder().setTitle("Cart Item Update").setBody(message).build();
		Message firebaseMessage = Message.builder().setNotification(notification).setTopic("all-users").build();
		try {
			String response = FirebaseMessaging.getInstance().send(firebaseMessage);
			logger.info("Successfully sent push notification: " + response); 
		} catch (Exception e) {
			System.err.println("Error sending push notification: " + e.getMessage());
		}
	}
}
