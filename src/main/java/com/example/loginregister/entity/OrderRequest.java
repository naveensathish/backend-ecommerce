package com.example.loginregister.entity;

import java.util.List;

public class OrderRequest {

    private String userId;
    private String email;
    private String productId;
    private String username;
    private String productName;
    private String pincode;
    private String phonenumber;
    private String deliveryAddress;
    private List<OrderItem> data; 
    private String productIds;     
    private List<String> productNames;     
    
    public String getProductIds() {
		return productIds;
	}

	public void setProductIds(String productIds) {
		this.productIds = productIds;
	}
	
	public List<String> getProductNames() {
		return productNames;
	}

	public void setProductNames(List<String> productNames) {
		this.productNames = productNames;
	}

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
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

	public String getProductId() {
		return productId;
	}

	public void setProductId(String productId) {
		this.productId = productId;
	}

	public String getDeliveryAddress() {
        return deliveryAddress;
    }

    public void setDeliveryAddress(String deliveryAddress) {
        this.deliveryAddress = deliveryAddress;
    }

    public List<OrderItem> getData() {
        return data;
    }

    public void setData(List<OrderItem> data) {
        this.data = data;
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
		return "OrderRequest [userId=" + userId + ", email=" + email + ", productId=" + productId + ", username="
				+ username + ", productName=" + productName + ", pincode=" + pincode + ", phonenumber=" + phonenumber
				+ ", deliveryAddress=" + deliveryAddress + ", data=" + data + ", productIds=" + productIds
				+ ", productNames=" + productNames + ", getProductIds()=" + getProductIds() + ", getProductNames()="
				+ getProductNames() + ", getUserId()=" + getUserId() + ", getEmail()=" + getEmail() + ", getUsername()="
				+ getUsername() + ", getProductName()=" + getProductName() + ", getProductId()=" + getProductId()
				+ ", getDeliveryAddress()=" + getDeliveryAddress() + ", getData()=" + getData() + ", getPincode()="
				+ getPincode() + ", getPhonenumber()=" + getPhonenumber() + ", getClass()=" + getClass()
				+ ", hashCode()=" + hashCode() + ", toString()=" + super.toString() + "]";
	}

}
