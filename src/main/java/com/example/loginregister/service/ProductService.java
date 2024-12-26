package com.example.loginregister.service;

import java.util.List;
import java.util.Optional;

import com.example.loginregister.entity.Product;
import com.example.loginregister.entity.StoredImage;

public interface ProductService {
	Optional<StoredImage> getProductById(Long id);
	boolean addProductToSellerTable(Long sellerId, Product product);

	List<Product> getAllProducts();

	List<Product> getAllProductsRedis();
	
}
