package com.example.loginregister.service;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class MessageConsumerService {

	private static final Logger logger = LogManager.getLogger(MessageConsumerService.class);
	private final BlockingQueue<String> messageQueue = new LinkedBlockingQueue<>();
	private final ExecutorService executorService = Executors
			.newFixedThreadPool(Math.max(10, Runtime.getRuntime().availableProcessors() * 2));

	public MessageConsumerService() {
		startMessageProcessor();
	}

	@KafkaListener(topics = "kcg1", groupId = "group_id")
	public void listen(String message) {
		logger.info("\n\nReceived message: {}", message + "\n");
		try {
			messageQueue.put(message);
		} catch (InterruptedException e) {
			logger.error("Error adding message to the queue: {}", e.getMessage());
		}
	}

	private void startMessageProcessor() {
		for (int i = 0; i < 10; i++) {
			executorService.submit(() -> {
				while (true) {
					try {
						String message = messageQueue.take();
						processMessage(message);
					} catch (InterruptedException e) {
						logger.error("Error taking message from the queue: {}", e.getMessage());
					}
				}
			});
		}
	}

	private void processMessage(String message) {
		logger.info("\n\nProcessing message: {}", message + "\n");
	}
}