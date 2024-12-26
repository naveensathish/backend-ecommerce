package com.example.loginregister.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.example.loginregister.controller.AuthController;
import com.example.loginregister.entity.Order;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

@Service
public class KafkaOrderProducerService {
	private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

	@Autowired
	private KafkaTemplate<String, String> kafkaTemplate;

	@Autowired
	private ObjectMapper objectMapper;

	private static final String TOPIC = "order-topic";
	private static final String REPORT_TOPIC = "report-generation-topic"; 

	@Scheduled(fixedRate = 15 * 60 * 1000) 
	public void publishOrderMessage() {
		try {
			Order order = new Order();
			order.setOrderId("11202412096410116177b39b");
			order.setUserId("11");
			order.setOrderDate("2024-12-09 15:58:04"); 
			order.setTotalAmount(6798.0);
			order.setStatus("Pending"); 
			order.setShippingAddress("1st Street, Las Vegas");
			order.setProduct_id("2");
			order.setEmail("hehee@gmail.com");
			order.setUsername("hehee");
			order.setProductName("Xbox Series X/S");
			order.setQty(1);
			order.setPincode("641011"); 
			order.setPhonenumber("+911525585158");

			Map<String, Object> message = new HashMap<>();
 
			LocalDate today = LocalDate.now();
			String date = today.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
			String cycletime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
			String name = "orders datareport";
			String monthdate = today.format(DateTimeFormatter.ofPattern("MM/dd"));
			String localdatetime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
			String systemreceivedtime = "";

			message.put("order", order);
			message.put("date", date);
			message.put("cycletime", cycletime);
			message.put("name", name);
			message.put("monthdate", monthdate);
			message.put("localdatetime", localdatetime);
			message.put("systemreceivedtime", systemreceivedtime); 

			String messageJson = objectMapper.writeValueAsString(message);

//			kafkaTemplate.send(TOPIC, messageJson); 
			kafkaTemplate.send(REPORT_TOPIC, messageJson);

			logger.info("\n\nPRODUCER HH-Scheduled message Published sent to Kafka topic: " + messageJson);
			logger.info("Message Content: " + messageJson);
		} catch (Exception e) {
			System.err.println("Error sending Kafka message: " + e.getMessage());
		}
	}

	
	@Scheduled(cron = "0 0 5 * * *")
	public void publishOrderMessageAt5AM() {
		try {
			Order order = new Order();
			order.setOrderId("11202412096410116177b39b");
			order.setUserId("11");
			order.setOrderDate("2024-12-09 15:58:04"); 
			order.setTotalAmount(6798.0);
			order.setStatus("Pending"); 
			order.setShippingAddress("1st Street, Las Vegas");
			order.setProduct_id("2");
			order.setEmail("hehee@gmail.com");
			order.setUsername("hehee");
			order.setProductName("Xbox Series X/S");
			order.setQty(1);
			order.setPincode("641011");
			order.setPhonenumber("+911525585158");

			Map<String, Object> message = new HashMap<>();

			LocalDate today = LocalDate.now();
			String date = today.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
			String cycletime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
			String name = "orders datareport";
			String monthdate = today.format(DateTimeFormatter.ofPattern("MM/dd"));
			String localdatetime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
			String systemreceivedtime = "";

			message.put("order", order);
			message.put("date", date);
			message.put("cycletime", cycletime);
			message.put("name", name);
			message.put("monthdate", monthdate);
			message.put("localdatetime", localdatetime);
			message.put("systemreceivedtime", systemreceivedtime);

			String messageJson = objectMapper.writeValueAsString(message);

//			kafkaTemplate.send(TOPIC, messageJson); 
			kafkaTemplate.send(REPORT_TOPIC, messageJson);

			logger.info("\n\nPRODUCER HH-Scheduled message Published sent to Kafka topic: " + messageJson);
			logger.info("Message Content: " + messageJson);
		} catch (Exception e) {
			System.err.println("Error sending Kafka message: " + e.getMessage());
		}
	}
	
}
