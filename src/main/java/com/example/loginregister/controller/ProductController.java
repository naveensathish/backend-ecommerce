package com.example.loginregister.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.loginregister.entity.Product;
import com.example.loginregister.entity.Seller;
import com.example.loginregister.entity.StoredImage;
import com.example.loginregister.service.ProductService;

@RestController
@RequestMapping("/api/products")
@CrossOrigin(origins = {"http://localhost:5000", "http://localhost:5001", "http://localhost:3000", "http://localhost:3001"})
public class ProductController {

    @Autowired
    private ProductService productService;

    @GetMapping("/productview")
    public ResponseEntity<StoredImage> getProductById(@RequestParam Long id) {
        Optional<StoredImage> product = productService.getProductById(id);
        return product.map(ResponseEntity::ok)
                      .orElseGet(() -> ResponseEntity.notFound().build());
    }    
    
    @GetMapping("/productssget")
	public ResponseEntity<List<Product>> getAllProducts() {
		List<Product> product = productService.getAllProducts();
	    if (product.isEmpty()) { 
	        return ResponseEntity.noContent().build();
	    }
	    return ResponseEntity.ok(product);
	}
    
}
