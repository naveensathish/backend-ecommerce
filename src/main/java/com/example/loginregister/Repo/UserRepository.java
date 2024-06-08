package com.example.loginregister.Repo;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.loginregister.Entity.User;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByEmail(String email);

    boolean existsByEmail(String email);
} 
