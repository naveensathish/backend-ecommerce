package com.example.loginregister.controller;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.loginregister.entity.CartItem;
import com.example.loginregister.entity.Order;
import com.example.loginregister.entity.OrderRequest;
import com.example.loginregister.exception.DuplicateCartItemException;
import com.example.loginregister.security.JwtUtil;
import com.example.loginregister.service.CartService;
import com.example.loginregister.service.OrderService;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/cart")
@CrossOrigin(origins = { "http://localhost:5000", "http://localhost:5001", "http://localhost:3000",
		"http://localhost:3001" })
public class CartController {

	private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

	@Autowired
	private JwtUtil jwtUtil;

	@Autowired
	private CartService cartService;

	@Autowired
	private OrderService orderService;

	@Autowired
	private KafkaTemplate<String, String> kafkaTemplate;

	public CartController(CartService cartService) {
		this.cartService = cartService;
	}

	@PutMapping("/addItem/{userId}/{productId}")
	public ResponseEntity<?> addItemToCart(@PathVariable String userId, @PathVariable String productId) {
		try {
			cartService.addItemToCartInBag(userId, productId);
			return ResponseEntity.ok("\n\nItem quantity increased.");
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Error adding item to cart: " + e.getMessage());
		}
	}

	@PutMapping("/removeItem/{userId}/{productId}")
	public ResponseEntity<?> removeItemFromCart(@PathVariable String userId, @PathVariable String productId) {
		try {
			cartService.removeItemFromCart(userId, productId);
			return ResponseEntity.ok("\n\nItem quantity reduced.");
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Error removing item from cart: " + e.getMessage());
		}
	}

	@DeleteMapping("/removeAllItems/{userId}/{productId}")
	public ResponseEntity<?> removeAllItems(@PathVariable String userId, @PathVariable String productId) {
		try {
			cartService.removeAllItemsFromCart(userId, productId);
			return ResponseEntity.ok("All items removed.");
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error removing all items.");
		}
	}

	@GetMapping("/fetchordersincart/{userId}")
	public ResponseEntity<List<Order>> getOrderItems(@PathVariable String userId, @RequestParam String email) {
		logger.info("Received request to fetch order items for userId: {}", userId);

		logger.info("Email received: {}", email);

		try {
			List<Order> orderItems = orderService.getOrderItemsByUserId(userId, email);

			if (orderItems != null && !orderItems.isEmpty()) {
				logger.info("Successfully fetched {} order items for userId: {}", orderItems.size(), userId);
//				logger.info("\n\nshowing the fetched {} order items for userId: {}. Orders: {}", orderItems.size(),
//						userId, orderItems);
				return ResponseEntity.ok(orderItems);
			} else {
				logger.warn("No items found in the order for userId: {}", userId);
				return ResponseEntity.status(404).body(null);
			}
		} catch (Exception e) {
			logger.error("Error occurred while fetching order items for userId: {}", userId, e);
			return ResponseEntity.status(500).body(null);
		}
	}

	@GetMapping("/fetchordersincartforsuperadmin/{userId}")
	public ResponseEntity<List<Order>> getSuperAdminDetailsOrderItems(@PathVariable String userId,
			@RequestParam String email) {
		logger.info("Received request to fetch order items for userId: {}", userId);

		logger.info("Email received: {}", email);

		try {
			List<Order> orderItems = orderService.getOrderItemsByUserId(userId, email);

			if (orderItems != null && !orderItems.isEmpty()) {
				logger.info("Successfully fetched {} order items for userId: {}", orderItems.size(), userId);
				logger.info("\n\nshowing the fetched {} order items for userId: {}. Orders: {}", orderItems.size(),
						userId, orderItems);
				return ResponseEntity.ok(orderItems);
			} else {
				logger.warn("No items found in the order for userId: {}", userId);
				return ResponseEntity.status(404).body(null);
			}
		} catch (Exception e) {
			logger.error("Error occurred while fetching order items for userId: {}", userId, e);
			return ResponseEntity.status(500).body(null);
		}
	}

	@GetMapping("/Adminfetchordersincart/{userId}")
	public ResponseEntity<List<Order>> getOrderItemsAdmin(@PathVariable String userId, @RequestParam String email) {
		logger.info("Received request to fetch order items for userId: {}", userId);

		logger.info("Email received: {}", email);

		try {
			List<Order> orderItems = orderService.getOrderItemsByUserId(userId, email);

			if (orderItems != null && !orderItems.isEmpty()) {
				logger.info("Successfully fetched {} order items for userId: {}", orderItems.size(), userId);
				return ResponseEntity.ok(orderItems);
			} else {
				logger.warn("No items found in the order for userId: {}", userId);
				return ResponseEntity.status(404).body(null);
			}
		} catch (Exception e) {
			logger.error("Error occurred while fetching order items for userId: {}", userId, e);
			return ResponseEntity.status(500).body(null);
		}
	}

	@PostMapping("/addOrderedItems")
	public ResponseEntity<String> addOrderedItems(@RequestBody OrderRequest orderRequest) {
		try {
			orderService.createOrder(orderRequest);
			return ResponseEntity.ok("\n\nOrder placed successfully.");
		} catch (RuntimeException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error placing order.");
		}
	}

	@PutMapping("/cancel/{orderId}")
	public ResponseEntity<?> cancelOrder(@PathVariable String orderId) {
		logger.info("Received request to cancel order with orderId: {}", orderId);

		try {
			orderService.cancelOrder(orderId);
			logger.info("Successfully cancelled the order with orderId: {}", orderId);
			return ResponseEntity.ok("Order cancelled successfully");

		} catch (RuntimeException ex) {
			logger.warn("Failed to cancel order with orderId: {}. Reason: {}", orderId, ex.getMessage());
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed to cancel the order: " + ex.getMessage());
		} catch (Exception e) {
			logger.error("Error occurred while cancelling order with orderId: {}", orderId, e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("An error occurred while processing your request");
		}
	}

	@GetMapping("/fetchitemsincart/{userId}")
	public ResponseEntity<List<CartItem>> getCartItems(@PathVariable String userId) {
		logger.info("Received request to fetch cart items for userId: {}", userId);

		try {
			List<CartItem> cartItems = cartService.getCartItemsByUserId(userId);

			if (cartItems != null && !cartItems.isEmpty()) {
				logger.info("\n\nFetched cart items for userId {}: {}", userId, cartItems.toString());
				logger.info("\n\nSuccessfully fetched {} cart items for userId: {}", cartItems.size(), userId);
			} else {
				logger.warn("\n\nNo items found in the cart for userId: {}", userId);
			}

			return ResponseEntity.ok(cartItems);
		} catch (Exception e) {
			logger.error("\n\nError occurred while fetching cart items for userId: {}", userId, e);
			return ResponseEntity.status(500).body(null);
		}
	}

	@PostMapping("/add")
	public ResponseEntity<?> addCartItem(@RequestBody CartItem cartItem, Authentication authentication,
			HttpServletRequest request) {
		try {
			String userName = authentication.getName();
			String authorizationHeader = request.getHeader("Authorization");
			String token = authorizationHeader.substring(7);
			logger.info("\n\nExtracted JWT Token: {}", token);
			String userId = jwtUtil.extractUserIdFromToken(token);
			logger.info("\n\nUser with userId {} is attempting to add an item to the cart", userId);
			logger.info("\n\nUser with userId {} is attempting to add an item to the cart", userName);

			CartItem savedCartItem = cartService.addCartItem(cartItem);
			String message = "Item added to cart: " + savedCartItem.getProductName();
			kafkaTemplate.send("cart-items-topic", message);
			return ResponseEntity.status(HttpStatus.CREATED).body("\n\nItem added to cart");
		} catch (DuplicateCartItemException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("\n\nThis item is already in the cart");
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("\n\nError adding item to cart");
		}
	}

	@PutMapping("/update")
	public ResponseEntity<CartItem> updateCartItem(@RequestBody CartItem cartItem) {
		CartItem updatedCartItem = cartService.updateCartItem(cartItem);
		return ResponseEntity.ok(updatedCartItem);
	}

	@DeleteMapping("/remove/{id}")
	public ResponseEntity<Void> removeCartItem(@PathVariable Long id) {
		try {
			logger.info("\n\nAttempting to delete cart item with id: " + id);
			cartService.removeCartItem(id);
			logger.info("\n\nCart item with id: " + id + " deleted successfully.");
			return ResponseEntity.noContent().build();
		} catch (Exception e) {
			logger.error("\n\nError while deleting cart item with id: " + id, e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}
}