package com.example.loginregister.service;

import org.springframework.stereotype.Service;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class PushNotificationService {
	private static final Logger logger = LoggerFactory.getLogger(SellerService.class);
	
	public void sendPushNotification(String messageContent) {
		Notification notification = Notification.builder().setTitle("Cart Update").setBody(messageContent).build();

		Message message = Message.builder().setNotification(notification).setTopic("all-users").build();

		try {
			String response = FirebaseMessaging.getInstance().send(message);
			logger.info("\n\nSuccessfully sent message: " + response);
		} catch (Exception e) {
			System.err.println("\n\nError sending push notification: " + e.getMessage());
		}
	}
}
