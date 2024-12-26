package com.example.loginregister.controller;

import com.example.loginregister.entity.CustomerRating;
import com.example.loginregister.service.CustomerRatingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/ratings")
@CrossOrigin(origins = { "http://localhost:5000", "http://localhost:5001", "http://localhost:3000",
		"http://localhost:3001" })
public class CustomerRatingController {

	private static final Logger logger = LoggerFactory.getLogger(CustomerRatingController.class);

	@Autowired
	private CustomerRatingService customerRatingService;

	@PostMapping("/add/{userId}")
	public ResponseEntity<Object> addRating(@PathVariable String userId, @RequestBody CustomerRating ratingRequest) {
		try {
			int rating = ratingRequest.getRating();
			String review = ratingRequest.getReview();
			String productId = ratingRequest.getProductId();
			String username = ratingRequest.getUsername();
			CustomerRating customerRating = customerRatingService.addRating(userId, username, rating, review,
					productId);
			return ResponseEntity.ok().body(customerRating);
		} catch (Exception e) {
			logger.error("\n\nError adding rating", e);
			return ResponseEntity.status(500).body("Error adding rating");
		}
	}

	@GetMapping("/getratings/{productId}")
	public ResponseEntity<Object> getRatings(@PathVariable String productId) {
		try {
			List<CustomerRating> customerRatings = customerRatingService.getRatingsByProductId(productId);
			if (customerRatings == null || customerRatings.isEmpty()) {
				logger.info("No ratings available for productId: {}", productId);
				return ResponseEntity.ok(Collections.singletonMap("message", "No ratings available for this product."));
			}

			List<Map<String, Object>> ratingsResponse = new ArrayList<>();
			for (CustomerRating rating : customerRatings) {
				Map<String, Object> ratingMap = new HashMap<>();
				ratingMap.put("rating", rating.getRating());
				ratingMap.put("review", rating.getReview());
				ratingMap.put("productId", rating.getProductId());
				ratingMap.put("username", rating.getUsername());
				ratingsResponse.add(ratingMap);
			}

			return ResponseEntity.ok(ratingsResponse);

		} catch (Exception e) {
			logger.error("\n\nError while fetching ratings for productId: {}", productId, e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(Collections.singletonMap("message", "An error occurred while fetching ratings."));
		}
	}

}
