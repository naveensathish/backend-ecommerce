package com.example.loginregister.service;

import org.springframework.stereotype.Service;

@Service
public class PredictionService {

	public double predictPrice(int stocks, boolean isBestSeller) {
		double predictedPrice = (stocks * 10.0) + (isBestSeller ? 20.0 : 0); // Dummy logic
		return predictedPrice;
	}
}
