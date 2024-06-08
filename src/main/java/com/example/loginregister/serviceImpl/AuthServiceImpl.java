package com.example.loginregister.serviceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.example.loginregister.Entity.User;
import com.example.loginregister.Repo.UserRepository;
import com.example.loginregister.Service.AuthService;

@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder; // Autowire the PasswordEncoder bean

    @Override
    public boolean emailExists(String email) {
        // Check if the email already exists in the database
        return userRepository.findByEmail(email) != null;
    }

    @Override
    public User registerUser(User user) {
        // Check if the email already exists
        if (emailExists(user.getEmail())) {
            // Email already exists, return an appropriate error response
            throw new IllegalArgumentException("User with this email already exists.");
        }
        // Email is unique, proceed with user registration
        user.setPassword(passwordEncoder.encode(user.getPassword())); // Encrypt the password
        return userRepository.save(user);
    }

    @Override
    public User authenticateUser(String email, String password) {
        User user = userRepository.findByEmail(email);
        if (user != null && passwordEncoder.matches(password, user.getPassword())) {
            return user;
        }
        return null;
    }
}
