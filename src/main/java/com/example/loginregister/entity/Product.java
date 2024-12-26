package com.example.loginregister.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Product {

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

	@Column(name = "description", nullable = true)
	private String description;

	@Column(name = "price", nullable = true)
	private double price;

	@Column(name = "product", nullable = true)
	private String product;

	@Column(name = "sub_product", nullable = true)
	private String subProduct;

	@Column(name = "isBestSeller", nullable = true)
	@JsonProperty(defaultValue = "false")
	private Boolean isBestSeller;

	@Column(name = "stocks", nullable = false)
	private Long stocks;

	@Column(name = "user_id", nullable = false)
	private Long userId;

	@Column(name = "seller_id", nullable = true)
	private Long sellerId;

	public Product() {
	}

	public Product(String name, String categoryName, int price) {
		this.name = name;
		this.categoryName = categoryName;
		this.price = price;
	}

	public Product(Long id, String product, String subProduct, String categoryName, String name, String img,
			String videos, String description, Double price, String options, Long stocks, Long userId, Long sellerId,
			Boolean isBestSeller) {
		this.id = id;
		this.product = product;
		this.subProduct = subProduct;
		this.categoryName = categoryName;
		this.name = name;
		this.img = img;
		this.videos = videos;
		this.description = description;
		this.price = price;
		this.options = options;
		this.stocks = stocks;
		this.userId = userId;
		this.sellerId = sellerId;
		this.isBestSeller = isBestSeller;
	}

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

	public Boolean isBestSeller() {
		return isBestSeller;
	}

	public void setIsBestSeller(Boolean isBestSeller) {
		this.isBestSeller = isBestSeller;
	}

	public Long getSellerId() {
		return sellerId;
	}

	public void setSellerId(Long sellerId) {
		this.sellerId = sellerId;
	}

	public Long getStocks() {
		return stocks;
	}

	public void setStocks(Long stocks) {
		this.stocks = stocks;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Boolean getIsBestSeller() {
		return isBestSeller;
	}

	@Override
	public String toString() {
		return "Product [id=" + id + ", categoryName=" + categoryName + ", name=" + name + ", img=" + img + ", videos="
				+ videos + ", options=" + options + ", description=" + description + ", price=" + price + ", product="
				+ product + ", subProduct=" + subProduct + ", isBestSeller=" + isBestSeller + ", stocks=" + stocks
				+ ", userId=" + userId + ", sellerId=" + sellerId + ", getId()=" + getId() + ", getCategoryName()="
				+ getCategoryName() + ", getName()=" + getName() + ", getImg()=" + getImg() + ", getVideos()="
				+ getVideos() + ", getOptions()=" + getOptions() + ", getDescription()=" + getDescription()
				+ ", getPrice()=" + getPrice() + ", getProduct()=" + getProduct() + ", getSubProduct()="
				+ getSubProduct() + ", isBestSeller()=" + isBestSeller() + ", getSellerId()=" + getSellerId()
				+ ", getStocks()=" + getStocks() + ", getUserId()=" + getUserId() + ", getIsBestSeller()="
				+ getIsBestSeller() + ", getClass()=" + getClass() + ", hashCode()=" + hashCode() + ", toString()="
				+ super.toString() + "]";
	}

}
