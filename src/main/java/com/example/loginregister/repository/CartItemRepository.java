package com.example.loginregister.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.loginregister.entity.CartItem;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
	List<CartItem> findByUserId(String userId);

	void deleteByUserIdAndProductId(String userId, String productId);

	Optional<CartItem> findByUserIdAndProductId(String userId, String productId);

	List<CartItem> findAllByUserIdAndProductId(String userId, String productId);
}
