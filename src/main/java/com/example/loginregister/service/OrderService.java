package com.example.loginregister.service;

import java.util.List;

import com.example.loginregister.entity.Order;
import com.example.loginregister.entity.OrderRequest;

public interface OrderService {
	void createOrder(OrderRequest orderRequest);

	List<Order> getOrderItemsByUserId(String userId, String email);

	public void cancelOrder(String orderId);
}
