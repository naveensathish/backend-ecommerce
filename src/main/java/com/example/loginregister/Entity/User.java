package com.example.loginregister.Entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "app_user") 
public class User {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "email", nullable = false, unique = true) // Map 'email' field to database column
    private String email;

    @Column(name = "password", nullable = false) // Map 'password' field to database column
    private String password;
    
    @Column(name = "lock_status", nullable = false) // Map 'password' field to database column
    private String lock_status;
    
    @Column(name = "name", nullable = true) // Map 'name' field to database column
    private String name;

    @Column(name = "address", nullable = true) // Map 'address' field to database column
    private String address;

    @Column(name = "phone", nullable = true) // Map 'phone' field to database column
    private String phone;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getLock_status() {
		return lock_status;
	}

	public void setLock_status(String lock_status) {
		this.lock_status = lock_status;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}


}