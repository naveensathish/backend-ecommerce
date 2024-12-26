package com.example.loginregister.service;

import java.util.List;

import com.example.loginregister.entity.CartItem;

public interface CartService {
	List<CartItem> getCartItemsByUserId(String userId);

	CartItem addCartItem(CartItem cartItem);

	void removeCartItem(Long id);

	public CartItem updateCartItem(CartItem cartItem);

	CartItem updateCartItem(String userId, String productId, int quantity);

	CartItem addOrUpdateCartItem(String userId, String productId, int quantity);

	public void addItemToCartInBag(String userId, String productId);

	public void removeItemFromCart(String userId, String productId);

	public void removeAllItemsFromCart(String userId, String productId);

}