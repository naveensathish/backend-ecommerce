package com.example.loginregister.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.example.loginregister.entity.WishlistItem;
import com.example.loginregister.repository.WishlistItemRepository;
import com.example.loginregister.service.WishlistService;
import jakarta.persistence.EntityNotFoundException;

@RestController
@RequestMapping("/api/wishlist")
@CrossOrigin(origins = { "http://localhost:5000", "http://localhost:5001", "http://localhost:3000", "http://localhost:3001" })
public class WishlistController {

	private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

	@Autowired
	private WishlistService wishlistService;

	@Autowired
	private WishlistItemRepository wishlistItemRepository;

	@Autowired
	private KafkaTemplate<String, String> kafkaTemplate;

	@GetMapping("/user/{userId}")
	public ResponseEntity<List<WishlistItem>> getWishlistItems(@PathVariable String userId) {
		return ResponseEntity.ok(wishlistService.getWishlistItemsByUserId(userId));
	}

	@PostMapping("/add")
	public ResponseEntity<?> addWishlistItem(@RequestBody WishlistItem wishlistItem) {
		logger.info("\n\nEntered add Wishlist method with userId: {}");

		try {
			WishlistItem savedWishlistItem = wishlistService.addWishlistItem(wishlistItem);

			if (savedWishlistItem == null) {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Item already exists in the wishlist.");
			}

			String message = "Item added to wishlist: " + savedWishlistItem.getProductName();
			kafkaTemplate.send("wishlist-items-topic", message);

			Map<String, Object> response = new HashMap<>(); 
			response.put("message", "Item added to wishlist");
			response.put("wishlistId", savedWishlistItem.getId());
			response.put("productId", savedWishlistItem.getProductId());
			response.put("productName", savedWishlistItem.getProductName());
			return ResponseEntity.status(HttpStatus.CREATED).body(response);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error adding item to wishlist");
		}
	}

	@GetMapping("get/{userId}/{foodid}")
	public ResponseEntity<List<Map<String, String>>> getWishlist(@PathVariable String userId) {
		logger.info("\n\nEntered get Wishlist method with userId: {}", userId);

		try {
			List<WishlistItem> wishlistItems = wishlistService.getWishlistItemsByUserId(userId);

			List<Map<String, String>> wishlistProductDetails = wishlistItems.stream().map(item -> {
				Map<String, String> itemDetails = new HashMap<>();
				itemDetails.put("userId", userId); // Add userId
				itemDetails.put("foodid", item.getProductId()); 
				itemDetails.put("productName", item.getProductName()); 
				return itemDetails;
			}).collect(Collectors.toList());

			if (wishlistProductDetails.isEmpty()) {
				logger.info("\nNo items found in wishlist for userId: {}", userId);
			} else {
				logger.info("\n\nSuccessfully fetched {} items for userId: {}", wishlistProductDetails.size(), userId);
			}

			return ResponseEntity.ok(wishlistProductDetails);
		} catch (Exception e) {
			logger.error("\n\nError occurred while fetching wishlist for userId: {}", userId, e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
		}
	}

	@DeleteMapping("/remove/{userId}/{foodid}")
	public ResponseEntity<Map<String, String>> removeWishlistItem(@PathVariable String userId,
			@PathVariable String foodid) {
		logger.info("\n\nEntered remove Wishlist method with userId: {}", userId);

		try {
			System.out
					.println("\n\nReceived request to remove item with User ID: " + userId + " and Food ID: " + foodid);

			WishlistItem wishlistItem = wishlistItemRepository.findByUserIdAndProductId(userId, foodid)
					.orElseThrow(() -> new EntityNotFoundException(
							"Wishlist item not found for User ID: " + userId + " and Food ID: " + foodid));

			wishlistService.removeWishlistItem(foodid, userId);

			Map<String, String> response = new HashMap<>();
			response.put("message", "Item successfully removed from wishlist");

			return ResponseEntity.ok(response);
		} catch (EntityNotFoundException e) {
			System.err.println("\nEntity not found: " + e.getMessage());
			return ResponseEntity.status(404).build();
		} catch (Exception e) {
			System.err.println("\nError: " + e.getMessage());
			return ResponseEntity.status(500).build();
		}
	}
}