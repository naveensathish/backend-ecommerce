package com.example.loginregister;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.example.loginregister.service.SellerService;

import redis.clients.jedis.Jedis;

@Component
public class RedisTest {
	private static final Logger logger = LoggerFactory.getLogger(SellerService.class);

	@Value("${spring.redis.host:localhost}")
	private String redisHost;

	@Value("${spring.redis.port:6379}")
	private int redisPort;

	public void testRedisConnection() {
		try (Jedis jedis = new Jedis(redisHost, redisPort)) {
			jedis.set("foo", "bar");
			logger.info("\n\nConnected to Redis. Retrieved value: " + jedis.get("foo"));
		} catch (Exception e) {
			System.err.println("\n\nError connecting to Redis: " + e.getMessage());
		}
	}
}
