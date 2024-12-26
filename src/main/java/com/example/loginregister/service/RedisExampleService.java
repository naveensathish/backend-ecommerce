package com.example.loginregister.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class RedisExampleService {

	private static final Logger logger = LoggerFactory.getLogger(RedisExampleService.class);

	@Autowired
	private RedisTemplate<String, String> redisTemplate;

	public void saveData(String key, String value) {
		try {
			redisTemplate.opsForValue().set(key, value);
			logger.info("Successfully saved key '{}' with value '{}'", key, value);
		} catch (Exception e) {
			logger.error("Error saving data to Redis for key '{}': {}", key, e.getMessage());
		}
	}

	public String retrieveData(String key) {
		try {
			String value = redisTemplate.opsForValue().get(key);
			if (value != null) {
				logger.info("Successfully retrieved key '{}' with value '{}'", key, value);
			} else {
				logger.warn("No data found for key '{}'", key);
			}
			return value;
		} catch (Exception e) {
			logger.error("Error retrieving data from Redis for key '{}': {}", key, e.getMessage());
			return null;
		}
	}
}