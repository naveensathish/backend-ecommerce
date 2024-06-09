package com.example.loginregister.Service;

import org.springframework.stereotype.Service;

import com.example.loginregister.Entity.User;
@Service
	
public interface AuthService {
    User registerUser(User user);
    User authenticateUser(String email, String password, String name);
    boolean emailExists(String email);
}
