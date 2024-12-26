package com.example.loginregister.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "cart_items")
public class CartItem {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", nullable = false)
	private Long id;

	@JsonProperty("user_id")
	@Column(name = "user_id", nullable = false)
	private String userId;

	@JsonProperty("product_id")
	@Column(name = "product_id", nullable = false)
	private String productId;

	@JsonProperty("product_name")
	@Column(name = "product_name", nullable = false)
	private String productName;

	@Column(name = "quantity", nullable = false)
	private Integer quantity;

	@JsonProperty("price")
	@Column(name = "price", nullable = false)
	private double price;

	@JsonProperty("created_at")
	@Column(name = "created_at", nullable = true)
	private String createdAt;

	@JsonProperty("updated_at")
	@Column(name = "updated_at", nullable = true)
	private String updatedAt;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getProductId() {
		return productId;
	}

	public void setProductId(String productId) {
		this.productId = productId;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public String getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(String createdAt) {
		this.createdAt = createdAt;
	}

	public String getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(String updatedAt) {
		this.updatedAt = updatedAt;
	}

	@Override
	public String toString() {
		return "CartItem [id=" + id + ", userId=" + userId + ", productId=" + productId + ", productName=" + productName
				+ ", quantity=" + quantity + ", price=" + price + ", createdAt=" + createdAt + ", updatedAt="
				+ updatedAt + ", getId()=" + getId() + ", getUserId()=" + getUserId() + ", getProductId()="
				+ getProductId() + ", getProductName()=" + getProductName() + ", getQuantity()=" + getQuantity()
				+ ", getPrice()=" + getPrice() + ", getCreatedAt()=" + getCreatedAt() + ", getUpdatedAt()="
				+ getUpdatedAt() + ", getClass()=" + getClass() + ", hashCode()=" + hashCode() + ", toString()="
				+ super.toString() + "]";
	}
	
}