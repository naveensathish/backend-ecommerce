package com.example.loginregister.listener;

import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import com.example.loginregister.service.SellerService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component
public class StartupListener implements ApplicationListener<ContextRefreshedEvent> {
	private static final Logger logger = LoggerFactory.getLogger(SellerService.class);

	@Override
	public void onApplicationEvent(ContextRefreshedEvent event) {
		logger.info("\n\nkafka Application started!!!\n\n");
	}
}
