package com.example.loginregister.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.loginregister.entity.AdminUser;

public interface AdminUserRepository extends JpaRepository<AdminUser, Long> {
	AdminUser findByUsername(String username);

	AdminUser findByEmail(String email);

	AdminUser findByPhone(String phone);

	boolean existsByEmail(String email);

}
