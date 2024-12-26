package com.example.loginregister.serviceImpl;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.loginregister.entity.CustomerRating;
import com.example.loginregister.repository.CustomerRatingRepository;
import com.example.loginregister.repository.ProductRepository;
import com.example.loginregister.service.CustomerRatingService;

@Service
public class CustomerRatingServiceImpl implements CustomerRatingService {

    private static final Logger logger = LoggerFactory.getLogger(CustomerRatingServiceImpl.class);

    @Autowired
    private CustomerRatingRepository customerRatingRepository;

    @Override
    public CustomerRating addRating(String userId, String username, int rating, String review, String productId) {
        if (rating < 1 || rating > 5) {
            throw new IllegalArgumentException("Rating must be between 1 and 5.");
        }

        CustomerRating customerRating = new CustomerRating();
        customerRating.setUserId(userId);
        customerRating.setRating(rating);
        customerRating.setReview(review);
        customerRating.setUsername(username);  
        customerRating.setProductId(productId);
        customerRating.setCreatedAt(LocalDateTime.now());
        customerRating.setUpdatedAt(LocalDateTime.now());
        return customerRatingRepository.save(customerRating);
    }
    
    @Override
    public List<CustomerRating> getRatingsByProductId(String productId) {
        logger.info("\n\nFetching ratings for productId: {}", productId);

        List<CustomerRating> customerRatings = customerRatingRepository.findByProductId(productId);

        if (customerRatings == null || customerRatings.isEmpty()) {
            logger.info("\n\nNo ratings found for productId: {}", productId);
            return Collections.emptyList(); 
        } else { 
            logger.info("\n\nFetcheeeeed {} ratings for productId: {}", customerRatings.size(), productId);
        }

        return customerRatings;
    }
    
}
