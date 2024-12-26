package com.example.loginregister.serviceImpl;

import java.time.LocalDateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.example.loginregister.controller.AuthController;
import com.example.loginregister.entity.Order;
import com.example.loginregister.entity.OrderItem;
import com.example.loginregister.entity.OrderRequest;
import com.example.loginregister.entity.StoredImage;
import com.example.loginregister.repository.OrderRepository;
import com.example.loginregister.repository.ProductRepository;
import com.example.loginregister.service.OrderService;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class OrderServiceImpl implements OrderService {
	private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

	@Autowired
	private OrderRepository orderRepository;

	@Autowired
	private ProductRepository productRepository;

	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	private KafkaTemplate<String, String> kafkaTemplate;
	
	LocalDateTime now = LocalDateTime.now(ZoneId.of("Asia/Kolkata"));
	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
	String formattedDate = now.format(formatter);

	public void createOrder(OrderRequest orderRequest) {
		String[] productIds = orderRequest.getProductIds().split(",");

		List<OrderItem> orderItems = orderRequest.getData();

		List<String> productNames = orderRequest.getProductNames();

		String orderId = generateOrderId(orderRequest.getUserId(), orderRequest.getPincode());

		if (orderId == null) {
			orderId = generateOrderId(orderRequest.getUserId(), orderRequest.getPincode());
		}

		for (int i = 0; i < orderItems.size(); i++) {
			OrderItem item = orderItems.get(i);

			String productId = productIds[i];

			String productName = productNames.get(i);

			StoredImage product = productRepository.findById(Long.valueOf(productId))
					.orElseThrow(() -> new RuntimeException("Product not found"));

			if (product.getStocks() < item.getQty()) {
				throw new RuntimeException("\n\nInsufficient stock for product: " + productName);
			}

			Order order = new Order();
			order.setUserId(orderRequest.getUserId());
			order.setOrderId(orderId);
			order.setProduct_id(productId);
			order.setEmail(orderRequest.getEmail());
			order.setQty(item.getQty());
			order.setUsername(orderRequest.getUsername());
			order.setLastUpdatedOn(formattedDate);
			order.setProductName(productName);
			order.setShippingAddress(orderRequest.getDeliveryAddress());
			order.setPincode(orderRequest.getPincode());
			order.setPhonenumber(orderRequest.getPhonenumber());
			order.setTotalAmount(calculateTotalAmount(orderRequest.getData()));
			order.setSellerId(product.getSellerId());
						order.setStatus("Pending");

			LocalDateTime now = LocalDateTime.now(ZoneId.of("Asia/Kolkata"));
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
			String formattedDate = now.format(formatter);

			order.setOrderDate(formattedDate);

			orderRepository.save(order);

			product.setStocks(product.getStocks() - item.getQty());
			productRepository.save(product);

			try {
				String orderJson = objectMapper.writeValueAsString(order);
				kafkaTemplate.send("order-topic", orderJson);
			} catch (Exception e) {
				System.err.println("Error serializing order to JSON: " + e.getMessage());
			}
		}
	}

	@Override
	public void cancelOrder(String orderId) {
	    try {
	        List<Order> orders = orderRepository.findByOrderId(orderId);

	        if (orders == null || orders.isEmpty()) {
	            throw new RuntimeException("Order not found for orderId: " + orderId);
	        }

	        for (Order order : orders) {
	            if ("Cancelled".equalsIgnoreCase(order.getStatus())) {
	                throw new RuntimeException("Order with orderId: " + orderId + " is already cancelled");
	            }
	            order.setStatus("Cancelled");
	            order.setLastUpdatedOn(formattedDate);
	            orderRepository.save(order); 
	            String productId = order.getProduct_id();
	            int qty = order.getQty();
	            StoredImage product = productRepository.findById(Long.valueOf(productId))
	                    .orElseThrow(() -> new RuntimeException("Product not found for productId: " + productId));

	            product.setStocks(product.getStocks() + qty);
	            productRepository.save(product); 
	        }

	        try {
	            for (Order canceledOrder : orders) {
	                String cancelOrderJson = objectMapper.writeValueAsString(canceledOrder);
	                kafkaTemplate.send("order-cancel-topic", cancelOrderJson);
	                logger.info("Order cancellation message sent to Kafka: " + cancelOrderJson);
	            }
	        } catch (Exception e) {
	            System.err.println("Error serializing order cancellation to JSON: " + e.getMessage());
	        }

	    } catch (Exception e) {
	        throw new RuntimeException("Error while cancelling order with orderId: " + orderId + " - " + e.getMessage());
	    }
	}

	private String generateOrderId(String userId, String pincode) {
		LocalDateTime now = LocalDateTime.now(ZoneId.of("Asia/Kolkata"));
		int year = now.getYear();
		int month = now.getMonthValue();
		int day = now.getDayOfMonth();

		String userIdStr = userId;
		String pincodeStr = pincode;

		String yearStr = String.format("%04d", year);
		String monthStr = String.format("%02d", month);
		String dayStr = String.format("%02d", day);

		Random rand = new Random();
		int randomSuffix = rand.nextInt(10000);
		String randomSuffixStr = String.format("%04d", randomSuffix);

		String uniqueSuffix = UUID.randomUUID().toString().substring(0, 4);

		return userIdStr + yearStr + monthStr + dayStr + pincodeStr + randomSuffixStr + uniqueSuffix;
	}

	@Override
	public List<Order> getOrderItemsByUserId(String userId, String email) {
		try {
	        return orderRepository.findByUserIdAndEmailAndStatusNot(userId, email, "Cancelled");

		} catch (Exception e) {
			throw new RuntimeException("Error fetching order items: " + e.getMessage());
		}
	}

	private double calculateTotalAmount(List<OrderItem> items) {

		items.forEach(item -> {
			logger.info("\n\nItem Price calcc: " + item.getPrice() + ", Item Qty: " + item.getQty());
		});
		return items.stream().mapToDouble(item -> item.getPrice() * item.getQty()).sum();
	}
}