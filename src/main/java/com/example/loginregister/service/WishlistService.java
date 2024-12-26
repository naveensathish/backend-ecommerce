package com.example.loginregister.service;

import java.util.List;

import com.example.loginregister.entity.WishlistItem;

public interface WishlistService {
	List<WishlistItem> getWishlistItemsByUserId(String userId);

	WishlistItem addWishlistItem(WishlistItem wishlistItem);
	
	public void removeWishlistItem(String productId, String userId) ;
    

}