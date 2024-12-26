package com.example.loginregister;

import java.security.SecureRandom;
import java.util.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.example.loginregister.service.SellerService;

public class KeyGenerator {
	private static final Logger logger = LoggerFactory.getLogger(SellerService.class);

	public static void main(String[] args) {
		SecureRandom secureRandom = new SecureRandom();
		byte[] key = new byte[32]; 
		secureRandom.nextBytes(key);
		String base64Key = Base64.getEncoder().encodeToString(key);
		logger.info("Base64 Encoded Key: " + base64Key);
	}
}
