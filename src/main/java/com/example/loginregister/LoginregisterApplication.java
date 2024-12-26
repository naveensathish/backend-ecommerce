package com.example.loginregister;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@ComponentScan(basePackages = { "com.example.loginregister",
		"com.example.loginregister.service, package com.example.loginregister.controller" })
@EntityScan(basePackages = "com.example.loginregister.entity")
@EnableTransactionManagement
@EnableCaching
@EnableScheduling 
public class LoginregisterApplication implements CommandLineRunner {

	private static final Logger logger = LoggerFactory.getLogger(LoginregisterApplication.class);

	@Autowired
	private RedisTest redisTest; 

	public static void main(String[] args) {
		SpringApplication.run(LoginregisterApplication.class, args);
		logger.info("\n\nAPPLICATION STARTED.....!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!\n");
	}

	@Override
	public void run(String... args) throws Exception {
		redisTest.testRedisConnection();
	}

}
