package com.example.loginregister.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class RedisAppStartupListener {
	private static final Logger logger = LoggerFactory.getLogger(RedisAppStartupListener.class);

	@EventListener(ApplicationReadyEvent.class)
	public void onApplicationReady() {
		logger.info("\n\nRedis configuration has been initialized, and the application is ready!!!.\n\n");
	}
}
