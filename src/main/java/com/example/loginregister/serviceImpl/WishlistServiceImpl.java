package com.example.loginregister.serviceImpl;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.loginregister.entity.WishlistItem;
import com.example.loginregister.repository.WishlistItemRepository;
import com.example.loginregister.service.WishlistService;

import jakarta.persistence.EntityNotFoundException;

@Service
public class WishlistServiceImpl implements WishlistService {
	private static final Logger logger = LoggerFactory.getLogger(CartServiceImpl.class);

	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
	String formattedDate = LocalDateTime.now().format(formatter);

	@Autowired
	private WishlistItemRepository wishlistItemRepository;

	@Override
	public List<WishlistItem> getWishlistItemsByUserId(String userId) {
		return wishlistItemRepository.findByUserId(userId);
	}

	@Override
	public WishlistItem addWishlistItem(WishlistItem wishlistItem) {
		logger.info("Entering addWishlistItem method");
		logger.debug("Wishlist item to add: {}", wishlistItem);

		logger.info("Entering addWishlistItem method");
		logger.debug("Wishlist item to add: {}", wishlistItem);

		Optional<WishlistItem> existingItem = wishlistItemRepository.findByUserIdAndProductId(wishlistItem.getUserId(),
				wishlistItem.getProductId());

		if (existingItem.isPresent()) {
			logger.info("\n\nItem already exists in wishlist for userId: {} and productId: {}",
					wishlistItem.getUserId(), wishlistItem.getProductId());
			return null;
		}

		Long largestId = wishlistItemRepository.findMaxId();

		Long nextId = (largestId != null) ? largestId + 1 : 1;

		wishlistItem.setId(nextId);

		logger.info("\n\nwishlistItem " + wishlistItem);

		wishlistItem.setCreatedAt(formattedDate);
		wishlistItem.setUpdatedAt(formattedDate);

		WishlistItem savedWishlistItem = wishlistItemRepository.save(wishlistItem);

		printLargestWishlistItemId();

		logger.info("\n\nSuccessfully added item to wishlist\n\n: {}", savedWishlistItem);
		return savedWishlistItem;
	}

	public void printLargestWishlistItemId() {
		Long largestId = wishlistItemRepository.findMaxId();
		if (largestId != null) {
			logger.info("\n\nThe largest ID in the wishlist_item table is: {}", largestId);
		} else {
			logger.info("\n\nNo items found in the wishlist_item table.");
		}
	}

	@Override
	public void removeWishlistItem(String productId, String userId) {
		WishlistItem wishlistItem = wishlistItemRepository.findByProductIdAndUserId(productId, userId)
				.orElseThrow(() -> new EntityNotFoundException(
						"Wishlist item not found with productId: " + productId + " and userId: " + userId));

		wishlistItemRepository.delete(wishlistItem);
	}
}