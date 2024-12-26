package com.example.loginregister.entity;

import jakarta.persistence.*;
import java.util.Date;

@Entity
@Table(name = "orders")
public class Order {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", nullable = false)
	private Long id;

	@Column(name = "order_id", nullable = false)
	private String orderId;

	@Column(name = "user_id", nullable = false)
	private String userId;

	@Column(name = "order_date", nullable = true)
	private String orderDate;

	@Column(name = "total_amount", nullable = false)
	private double totalAmount;

	@Column(name = "status", nullable = false)
	private String status;
	
	@Column(name = "last_updated_on", nullable = false)
	private String lastUpdatedOn;

	@Column(name = "shipping_address", nullable = false)
	private String shippingAddress;

	@Column(name = "product_id", nullable = false)
	private String product_id;

	@Column(name = "email", nullable = false)
	private String email;

	@Column(name = "username", nullable = false)
	private String username;

	@Column(name = "product_name", nullable = false)
	private String productName;
	
	@Column(name = "seller_id", nullable = true)
	private Long sellerId;

	@Column(name = "qty", nullable = false)
	private int qty;

	@Column(name = "pincode", nullable = true)
	private String pincode;

	@Column(name = "phonenumber", nullable = true)
	private String phonenumber;

	public Order() {
	}

	public Order(Long id, String orderId, String userId, String orderDate, String lastUpdatedOn, double totalAmount, String productName,
			String status, String shippingAddress, String product_id, String email, String username, Long sellerId, int qty,
			String pincode, String phonenumber) {

		this.id = id;
		this.orderId = orderId;
		this.userId = userId;
		this.orderDate = orderDate;
		this.totalAmount = totalAmount;
		this.status = status;
		this.lastUpdatedOn = lastUpdatedOn;
		this.shippingAddress = shippingAddress;
		this.product_id = product_id;
		this.email = email;
		this.username = username;
		this.productName = productName;
		this.qty = qty;
		this.sellerId= sellerId;
		this.pincode = pincode;
		this.phonenumber = phonenumber;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getOrderDate() {
		return orderDate;
	}

	public void setOrderDate(String formattedDate) {
		this.orderDate = formattedDate;
	}

	public double getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(double totalAmount) {
		this.totalAmount = totalAmount;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
	public String getLastUpdatedOn() {
		return lastUpdatedOn;
	}

	public void setLastUpdatedOn(String lastUpdatedOn) {
		this.lastUpdatedOn = lastUpdatedOn;
	}

	public String getShippingAddress() {
		return shippingAddress;
	}

	public void setShippingAddress(String shippingAddress) {
		this.shippingAddress = shippingAddress;
	}

	public String getProduct_id() {
		return product_id;
	}

	public void setProduct_id(String product_id) {
		this.product_id = product_id;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public int getQty() {
		return qty;
	}

	public void setQty(int qty) {
		this.qty = qty;
	}

	public Long getSellerId() {
		return sellerId;
	}

	public void setSellerId(Long sellerId) {
		this.sellerId = sellerId;
	}

	public String getPincode() {
		return pincode;
	}

	public void setPincode(String pincode) {
		this.pincode = pincode;
	}

	public String getPhonenumber() {
		return phonenumber;
	}

	public void setPhonenumber(String phonenumber) {
		this.phonenumber = phonenumber;
	}

	@Override
	public String toString() {
		return "Order [id=" + id + ", orderId=" + orderId + ", userId=" + userId + ", orderDate=" + orderDate
				+ ", totalAmount=" + totalAmount + ", status=" + status + ", lastUpdatedOn=" + lastUpdatedOn
				+ ", shippingAddress=" + shippingAddress + ", product_id=" + product_id + ", email=" + email
				+ ", username=" + username + ", productName=" + productName + ", sellerId=" + sellerId + ", qty=" + qty
				+ ", pincode=" + pincode + ", phonenumber=" + phonenumber + ", getId()=" + getId() + ", getOrderId()="
				+ getOrderId() + ", getUserId()=" + getUserId() + ", getOrderDate()=" + getOrderDate()
				+ ", getTotalAmount()=" + getTotalAmount() + ", getStatus()=" + getStatus() + ", getLastUpdatedOn()="
				+ getLastUpdatedOn() + ", getShippingAddress()=" + getShippingAddress() + ", getProduct_id()="
				+ getProduct_id() + ", getEmail()=" + getEmail() + ", getUsername()=" + getUsername()
				+ ", getProductName()=" + getProductName() + ", getQty()=" + getQty() + ", getSellerId()="
				+ getSellerId() + ", getPincode()=" + getPincode() + ", getPhonenumber()=" + getPhonenumber()
				+ ", getClass()=" + getClass() + ", hashCode()=" + hashCode() + ", toString()=" + super.toString()
				+ "]";
	}

}
