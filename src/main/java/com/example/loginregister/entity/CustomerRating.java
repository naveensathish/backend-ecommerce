package com.example.loginregister.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "customer_ratings")
public class CustomerRating {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "rating_id")
	private Long ratingId;

	@Column(name = "user_id", nullable = false)
	private String userId;

	@Column(name = "product_id", nullable = false)
	private String productId;
	
	@Column(name = "username", nullable = false)
	private String username;

	@Column(name = "rating")
	private int rating;

	@Column(name = "review", length = 50000)
	private String review;
	
    @Column(name = "is_top_review")
    private String isTopReview;

    @Column(name = "is_top_product")
    private String isTopProduct;

    @Column(name = "is_verified_purchase", nullable = false)
    private Boolean isVerifiedPurchase = false;

	@Column(name = "created_at", nullable = false)
	private LocalDateTime createdAt;

	@Column(name = "updated_at", nullable = false)
	private LocalDateTime updatedAt;

	public Long getRatingId() {
		return ratingId;
	}

	public void setRatingId(Long ratingId) {
		this.ratingId = ratingId;
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

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public void setRating(int rating) {
		this.rating = rating;
	}

	public Integer getRating() {
		return rating;
	}

	public void setRating(Integer rating) {
		this.rating = rating;
	}

	public String getReview() {
		return review;
	}

	public void setReview(String review) {
		this.review = review;
	}

	public String getIsTopReview() {
		return isTopReview;
	}

	public void setIsTopReview(String isTopReview) {
		this.isTopReview = isTopReview;
	}

	public String getIsTopProduct() {
		return isTopProduct;
	}

	public void setIsTopProduct(String isTopProduct) {
		this.isTopProduct = isTopProduct;
	}

	public Boolean getIsVerifiedPurchase() {
		return isVerifiedPurchase;
	}

	public void setIsVerifiedPurchase(Boolean isVerifiedPurchase) {
		this.isVerifiedPurchase = isVerifiedPurchase;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}

	public LocalDateTime getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(LocalDateTime updatedAt) {
		this.updatedAt = updatedAt;
	}

	@Override
	public String toString() {
		return "CustomerRating [ratingId=" + ratingId + ", userId=" + userId + ", productId=" + productId
				+ ", username=" + username + ", rating=" + rating + ", review=" + review + ", isTopReview="
				+ isTopReview + ", isTopProduct=" + isTopProduct + ", isVerifiedPurchase=" + isVerifiedPurchase
				+ ", createdAt=" + createdAt + ", updatedAt=" + updatedAt + ", getRatingId()=" + getRatingId()
				+ ", getUserId()=" + getUserId() + ", getProductId()=" + getProductId() + ", getUsername()="
				+ getUsername() + ", getRating()=" + getRating() + ", getReview()=" + getReview()
				+ ", getIsTopReview()=" + getIsTopReview() + ", getIsTopProduct()=" + getIsTopProduct()
				+ ", getIsVerifiedPurchase()=" + getIsVerifiedPurchase() + ", getCreatedAt()=" + getCreatedAt()
				+ ", getUpdatedAt()=" + getUpdatedAt() + ", getClass()=" + getClass() + ", hashCode()=" + hashCode()
				+ ", toString()=" + super.toString() + "]";
	}
	
}
