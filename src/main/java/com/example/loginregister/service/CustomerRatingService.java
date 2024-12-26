package com.example.loginregister.service;

import java.util.List;
import com.example.loginregister.entity.CustomerRating;

public interface CustomerRatingService {

    CustomerRating addRating(String userId, String username, int rating, String review, String productId);

	List<CustomerRating> getRatingsByProductId(String productId);
    	
}
