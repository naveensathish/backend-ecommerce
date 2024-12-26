package com.example.loginregister.service;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class MessageProducerService {

	private final KafkaTemplate<String, String> kafkaTemplate;

	public MessageProducerService(KafkaTemplate<String, String> kafkaTemplate) {
		this.kafkaTemplate = kafkaTemplate;
	}

	public void sendMessage(String topic, String message) {
		kafkaTemplate.send(topic, message);
	}
}
