package com.example.loginregister.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "stocks_available")
public class StoredImage {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "category_name", nullable = false)
	private String categoryName;

	@Column(name = "name", nullable = false)
	private String name;

	@Column(name = "img", nullable = false, length = 50000)
	private String img;
	
	@Column(name = "videos", nullable = false, length = 50000)
	private String videos;

	@Column(name = "options", nullable = true)
	private String options;
	
	@Column(name = "stocks", nullable = false)
	private Long stocks;
	
	@Column(name = "description", nullable = true, length = 500000) 
	private String description;

	@Column(name = "price", nullable = true)
	private double price;

	@Column(name = "product", nullable = true)
	private String product;

	@Column(name = "sub_product", nullable = true)
	private String subProduct;

	@Column(name = "isBestSeller", nullable = true)
	private boolean isBestSeller;

	@Column(name = "user_id", nullable = true)
	private Long userId;

	@Column(name = "seller_id", nullable = true)
	private Long sellerId;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCategoryName() {
		return categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getImg() {
		return img;
	}

	public void setImg(String img) {
		this.img = img;
	}

	public String getVideos() {
		return videos;
	}

	public void setVideos(String videos) {
		this.videos = videos;
	}

	public String getOptions() {
		return options;
	}

	public void setOptions(String options) {
		this.options = options;
	}

	public Long getStocks() {
		return stocks;
	}

	public void setStocks(Long stocks) {
		this.stocks = stocks;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public String getProduct() {
		return product;
	}

	public void setProduct(String product) {
		this.product = product;
	}

	public String getSubProduct() {
		return subProduct;
	}

	public void setSubProduct(String subProduct) {
		this.subProduct = subProduct;
	}

	public boolean isBestSeller() {
		return isBestSeller;
	}

	public void setBestSeller(boolean isBestSeller) {
		this.isBestSeller = isBestSeller;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Long getSellerId() {
		return sellerId;
	}

	public void setSellerId(Long sellerId) {
		this.sellerId = sellerId;
	}

	@Override
	public String toString() {
		return "StoredImage [id=" + id + ", categoryName=" + categoryName + ", name=" + name + ", img=" + img
				+ ", videos=" + videos + ", options=" + options + ", stocks=" + stocks + ", description=" + description
				+ ", price=" + price + ", product=" + product + ", subProduct=" + subProduct + ", isBestSeller="
				+ isBestSeller + ", userId=" + userId + ", sellerId=" + sellerId + ", getId()=" + getId()
				+ ", getCategoryName()=" + getCategoryName() + ", getName()=" + getName() + ", getImg()=" + getImg()
				+ ", getVideos()=" + getVideos() + ", getOptions()=" + getOptions() + ", getStocks()=" + getStocks()
				+ ", getDescription()=" + getDescription() + ", getPrice()=" + getPrice() + ", getProduct()="
				+ getProduct() + ", getSubProduct()=" + getSubProduct() + ", isBestSeller()=" + isBestSeller()
				+ ", getUserId()=" + getUserId() + ", getSellerId()=" + getSellerId() + ", getClass()=" + getClass()
				+ ", hashCode()=" + hashCode() + ", toString()=" + super.toString() + "]";
	}

}