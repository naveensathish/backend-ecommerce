package com.example.loginregister.dto;

public class UserRegistrationRequestDto {
	private String username;
	private String email;
	private String password;
	private String phone;
 
	public UserRegistrationRequestDto() {
	}

	public UserRegistrationRequestDto(String username, String email, String password) {
		this.username = username;
		this.email = email;
		this.password = password;
		this.phone = phone;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
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

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}
}
