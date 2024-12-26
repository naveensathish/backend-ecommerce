package com.example.loginregister.entity;

import java.util.List;
import java.util.Collection;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonIgnore;

import org.springframework.security.core.GrantedAuthority;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Version;

@Entity
@Table(name = "app_user")
public class User implements UserDetails {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "email", nullable = false, unique = true)
	private String email;

	@Column(name = "password", nullable = false)
	private String password;

	@Column(name = "decrypt_password", nullable = false)
	private String decryptPassword;

	@Column(name = "lock_status", nullable = false)
	private String lockStatus;

	@Column(name = "username", nullable = true)
	private String username;

	@Column(name = "address", nullable = true)
	private String address;

	@Column(name = "phone", nullable = true)
	private String phone;

	@Column(name = "system_received_date_time", nullable = false)
	private String systemReceivedDateTime;

	@Column(name = "is_admin", nullable = false)
	private boolean isAdmin;

	@Column(name = "is_superadmin", nullable = false)
	private boolean isSuperAdmin;

	@Column(name = "role", nullable = false)
	private String role_appuser;

	@Column(name = "role_id", nullable = true)
	private Long roleId;

	@ManyToOne
	@JoinColumn(name = "role_id", insertable = false, updatable = false)
	private Role role;

	public String getRole_appuser() {
		return role_appuser;
	}

	public void setRole_appuser(String role_appuser) {
		this.role_appuser = role_appuser;
	}

	@Version
	private Long version;

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

	public String getDecryptPassword() {
		return decryptPassword;
	}

	public void setDecryptPassword(String decryptPassword) {
		this.decryptPassword = decryptPassword;
	}

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	public Long getRoleId() {
		return roleId;
	}

	public void setRoleId(Long roleId) {
		this.roleId = roleId;
	}

	public String getLockStatus() {
		return lockStatus;
	}

	public void setLockStatus(String lockStatus) {
		this.lockStatus = lockStatus;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

//	@Override
//	public Collection<? extends GrantedAuthority> getAuthorities() {
//		return List.of(new SimpleGrantedAuthority(role_appuser));
//	} 
	
//	@Override
//    public Collection<? extends GrantedAuthority> getAuthorities() {
//        return List.of(new SimpleGrantedAuthority(role != null ? role.getRoleName() : ""));
//    } 
	
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
	    // Ensure the role is not null before accessing getRoleName
	    if (role != null && role.getRoleName() != null) {
	        return List.of(new SimpleGrantedAuthority(role.getRoleName()));
	    }
	    return List.of();  // or return a default role if required
	}
	
//	@JsonIgnore
//	@Override
//	public Collection<? extends GrantedAuthority> getAuthorities() {
//	    return List.of(new SimpleGrantedAuthority(role != null ? role.getRoleName() : ""));
//	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getSystemReceivedDateTime() {
		return systemReceivedDateTime;
	}

	public void setSystemReceivedDateTime(String systemReceivedDateTime) {
		this.systemReceivedDateTime = systemReceivedDateTime;
	}

	public boolean isAdmin() {
		return isAdmin;
	}

	public void setAdmin(boolean isAdmin) {
		this.isAdmin = isAdmin;
	}

	public boolean isSuperAdmin() {
		return isSuperAdmin;
	}

	public void setSuperAdmin(boolean isSuperAdmin) {
		this.isSuperAdmin = isSuperAdmin;
	}

	public Long getVersion() {
		return version;
	}

	public void setVersion(Long version) {
		this.version = version;
	}

	@Override
	public String toString() {
		return "User [id=" + id + ", email=" + email + ", password=" + password + ", decryptPassword=" + decryptPassword
				+ ", lockStatus=" + lockStatus + ", username=" + username + ", address=" + address + ", phone=" + phone
				+ ", systemReceivedDateTime=" + systemReceivedDateTime + ", isAdmin=" + isAdmin + ", isSuperAdmin="
				+ isSuperAdmin + ", role_appuser=" + role_appuser + ", roleId=" + roleId + ", version=" + version
				+ ", getRole_appuser()=" + getRole_appuser() + ", getId()=" + getId() + ", getEmail()=" + getEmail()
				+ ", getPassword()=" + getPassword() + ", getDecryptPassword()=" + getDecryptPassword()
				+ ", getRoleId()=" + getRoleId() + ", getLockStatus()=" + getLockStatus() + ", getUsername()="
				+ getUsername() + ", getAddress()=" + getAddress() + ", getAuthorities()=" + getAuthorities()
				+ ", getPhone()=" + getPhone() + ", getSystemReceivedDateTime()=" + getSystemReceivedDateTime()
				+ ", isAdmin()=" + isAdmin() + ", isSuperAdmin()=" + isSuperAdmin() + ", getVersion()=" + getVersion()
				+ ", getClass()=" + getClass() + ", hashCode()=" + hashCode() + ", toString()=" + super.toString()
				+ "]";
	}

}
