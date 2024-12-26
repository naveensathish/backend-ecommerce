package com.example.loginregister.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.loginregister.service.PredictionService;
import com.example.loginregister.service.SellerService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/api/prices")
public class PricePredictionController {
	
	private static final Logger logger = LoggerFactory.getLogger(SellerService.class);

	@Autowired
	private PredictionService predictionService;

	@PostMapping("/predict")
	@PreAuthorize("hasRole('SUPERADMIN')")
	public ResponseEntity<Object> predictPrice(@RequestBody PredictionRequest request) {
		logger.info("\n\n Predicting buddy!!!!!!!!!!!!@\n\n");

		try {
			double predictedPrice = predictionService.predictPrice(request.getStocks(), request.isBestSeller());
			return ResponseEntity.ok().body(predictedPrice);
		} catch (AccessDeniedException e) {
			throw new AccessDeniedException("\n\nYou do not have permission to access this resource\n");
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
					"An error occurred while processing the prediction,You do not have permission to access this resource.");
		}
	}

	public static class PredictionRequest {

		private int stocks;
		private boolean isBestSeller;

		public int getStocks() {
			return stocks;
		}

		public void setStocks(int stocks) {
			this.stocks = stocks;
		}

		public boolean isBestSeller() {
			return isBestSeller;
		}

		public void setBestSeller(boolean bestSeller) {
			isBestSeller = bestSeller;
		}
	}
}
