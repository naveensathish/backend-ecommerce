package com.example.loginregister.dto;

public class LoginDto {
    private String email;
    private String password;
    private String username;
    private String phone;
 
    public LoginDto() {
    }

    public LoginDto(String email, String password, String username, String phone) {
        this.email = email;
        this.password = password;
        this.username =username;
        this.phone =phone;
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

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}
	
}
