package com.example.loginregister.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.loginregister.service.MessageProducerService;
import com.example.loginregister.service.SellerService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
public class KafkaController {
	private static final Logger logger = LoggerFactory.getLogger(SellerService.class);

	private final MessageProducerService messageProducer;

	@Autowired
	public KafkaController(MessageProducerService messageProducer) {
		this.messageProducer = messageProducer;
	}

	@PostMapping("/send")
	public void sendMessage(@RequestParam String message) {
		messageProducer.sendMessage("kcg1", message);
		logger.info("Message sent: " + message);
	}
}