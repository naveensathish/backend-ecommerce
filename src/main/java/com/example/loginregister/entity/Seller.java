package com.example.loginregister.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "sellers")
public class Seller {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "username", nullable = false)
	private String username;

	@Column(name = "email", nullable = false)
	private String email;

	@Column(name = "password", nullable = false)
	private String password;

	@Column(name = "role", nullable = false)
	@JsonProperty("role")
	private String role;

	@Column(name = "gstin", nullable = true)
	private String gstin;

	@Column(name = "pancard", nullable = true)
	private String pancard;

	@Column(name = "aadhaarcard", nullable = true)
	private String aadhaarcard;

	@Column(name = "voterid", nullable = true)
	private String voterid;

	@Column(name = "gstin_validated", nullable = false, columnDefinition = "boolean default false")
	private boolean gstinValidated;

	@Column(name = "pancard_validated", nullable = false, columnDefinition = "boolean default false")
	private boolean pancardValidated;

	@Column(name = "aadhaarcard_validated", nullable = false, columnDefinition = "boolean default false")
	private boolean aadhaarcardValidated;

	@Column(name = "voterid_validated", nullable = false, columnDefinition = "boolean default false")
	private boolean voteridValidated;

	@Column(name = "phone", nullable = true)
	private String phone;

	private String sellerRolefromuser;

	@Column(name = "created_at", nullable = false)
	private String createdAt;

	public String getSellerRolefromuser() {
		return sellerRolefromuser;
	}

	@Column(name = "is_active", nullable = true)
	private boolean isActive;

	@Column(name = "user_id", nullable = false)
	@JsonProperty("user_id")
	private Long userId;

	public Seller() {

	}

	public Seller(Long id, String username, String email, String phone, String password, String role,
			String sellerRolefromuser, boolean isActive, Long userId, String gstin, String pancard, String aadhaarcard, String voterid,
            boolean gstinValidated, boolean pancardValidated, boolean aadhaarcardValidated, boolean voteridValidated) {
		this.id = id;
		this.username = username;
		this.email = email;
		this.password = password;
		this.role = role;
		this.phone = phone;
		this.sellerRolefromuser = sellerRolefromuser;
		this.isActive = isActive;
		this.userId = userId;
		this.gstin = gstin;
        this.pancard = pancard;
        this.aadhaarcard = aadhaarcard;
        this.voterid = voterid;
        this.gstinValidated = gstinValidated;
        this.pancardValidated = pancardValidated;
        this.aadhaarcardValidated = aadhaarcardValidated;
        this.voteridValidated = voteridValidated;
	}

	public String getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(String createdAt) {
		this.createdAt = createdAt;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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

	public boolean isActive() {
		return isActive;
	}

	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}

	public void setSellerRolefromuser(String sellerRolefromuser) {
		this.sellerRolefromuser = sellerRolefromuser;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getRole() {
		return role;
	}

	public String getGstin() {
		return gstin;
	}

	public void setGstin(String gstin) {
		this.gstin = gstin;
	}

	public String getPancard() {
		return pancard;
	}

	public void setPancard(String pancard) {
		this.pancard = pancard;
	}

	public String getAadhaarcard() {
		return aadhaarcard;
	}

	public void setAadhaarcard(String aadhaarcard) {
		this.aadhaarcard = aadhaarcard;
	}

	public String getVoterid() {
		return voterid;
	}

	public void setVoterid(String voterid) {
		this.voterid = voterid;
	}

	public boolean isGstinValidated() {
		return gstinValidated;
	}

	public void setGstinValidated(boolean gstinValidated) {
		this.gstinValidated = gstinValidated;
	}

	public boolean isPancardValidated() {
		return pancardValidated;
	}

	public void setPancardValidated(boolean pancardValidated) {
		this.pancardValidated = pancardValidated;
	}

	public boolean isAadhaarcardValidated() {
		return aadhaarcardValidated;
	}

	public void setAadhaarcardValidated(boolean aadhaarcardValidated) {
		this.aadhaarcardValidated = aadhaarcardValidated;
	}

	public boolean isVoteridValidated() {
		return voteridValidated;
	}

	public void setVoteridValidated(boolean voteridValidated) {
		this.voteridValidated = voteridValidated;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	@Override
	public String toString() {
		return "Seller [id=" + id + ", username=" + username + ", email=" + email + ", password=" + password + ", role="
				+ role + ", gstin=" + gstin + ", pancard=" + pancard + ", aadhaarcard=" + aadhaarcard + ", voterid="
				+ voterid + ", gstinValidated=" + gstinValidated + ", pancardValidated=" + pancardValidated
				+ ", aadhaarcardValidated=" + aadhaarcardValidated + ", voteridValidated=" + voteridValidated
				+ ", phone=" + phone + ", sellerRolefromuser=" + sellerRolefromuser + ", createdAt=" + createdAt
				+ ", isActive=" + isActive + ", userId=" + userId + ", getSellerRolefromuser()="
				+ getSellerRolefromuser() + ", getCreatedAt()=" + getCreatedAt() + ", getId()=" + getId()
				+ ", getUsername()=" + getUsername() + ", getEmail()=" + getEmail() + ", getPassword()=" + getPassword()
				+ ", isActive()=" + isActive() + ", getUserId()=" + getUserId() + ", getRole()=" + getRole()
				+ ", getGstin()=" + getGstin() + ", getPancard()=" + getPancard() + ", getAadhaarcard()="
				+ getAadhaarcard() + ", getVoterid()=" + getVoterid() + ", isGstinValidated()=" + isGstinValidated()
				+ ", isPancardValidated()=" + isPancardValidated() + ", isAadhaarcardValidated()="
				+ isAadhaarcardValidated() + ", isVoteridValidated()=" + isVoteridValidated() + ", getPhone()="
				+ getPhone() + ", getClass()=" + getClass() + ", hashCode()=" + hashCode() + ", toString()="
				+ super.toString() + "]";
	}

}
