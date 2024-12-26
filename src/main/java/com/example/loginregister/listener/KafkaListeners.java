package com.example.loginregister.listener;

import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import com.example.loginregister.service.SellerService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component
public class KafkaListeners {
	
	private static final Logger logger = LoggerFactory.getLogger(SellerService.class);

	
	@EventListener
	public void handleContextRefresh(ContextRefreshedEvent event) {
		logger.info("\n\n!!kafka Application started!!!!!");
	}
}
