package com.example.loginregister.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.loginregister.entity.WishlistItem;

public interface WishlistItemRepository extends JpaRepository<WishlistItem, Long> {
	List<WishlistItem> findByUserId(String userId);

	Optional<WishlistItem> findByUserIdAndProductId(String userId, String productId);

	Optional<WishlistItem> findByProductIdAndUserId(String productId, String userId);

	void deleteByProductId(String productId);

	@Query("SELECT MAX(w.id) FROM WishlistItem w")
	Long findMaxId();
}