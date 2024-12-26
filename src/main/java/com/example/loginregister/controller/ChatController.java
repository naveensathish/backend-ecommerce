package com.example.loginregister.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/api/chat")
public class ChatController {

	private static final Logger logger = LoggerFactory.getLogger(ChatController.class);

	@PostMapping("/process_message")
	public ResponseEntity<?> processMessage(@RequestBody MessageRequest request) {
		try {
			logger.info("Received message from frontend: {}", request.getMessage());
			String message = request.getMessage();
			String botReply = "Processed message: " + message;
			logger.info("Sending reply to frontend: {}", botReply);
			return ResponseEntity.ok(new MessageResponse(botReply));
		} catch (Exception e) {
			logger.error("Error processing message", e);
			return ResponseEntity.status(500).body("Error processing message");
		}
	}
}

class MessageRequest {
	private String message;

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}

class MessageResponse {
	private String reply;

	public MessageResponse(String reply) {
		this.reply = reply;
	}

	public String getReply() {
		return reply;
	}

	public void setReply(String reply) {
		this.reply = reply;
	}
}