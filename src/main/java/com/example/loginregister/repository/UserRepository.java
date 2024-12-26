package com.example.loginregister.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.loginregister.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {
	User findByUsername(String username);

	User findByEmail(String email);

	User findByPhone(String phone);

	boolean existsByEmail(String email);
}
