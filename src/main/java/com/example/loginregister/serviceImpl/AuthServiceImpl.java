package com.example.loginregister.serviceImpl;

import org.slf4j.Logger;
import com.example.loginregister.exception.UserLockedException;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.loginregister.Entity.User;
import com.example.loginregister.Repo.UserRepository;
import com.example.loginregister.Service.AuthService;

@Service
public class AuthServiceImpl implements AuthService {

    private static final Logger logger = LoggerFactory.getLogger(AuthServiceImpl.class);

    @Autowired
    private UserRepository userRepository;
    
    @Override
    public boolean emailExists(String email) { 
        // Check if the email already exists in the database
    	// Check if the email already exists in the database
        return userRepository.findByEmail(email) != null;  
    }

    @Override
    public User registerUser(User user) {
    	
    	 // Set lock_status to 'Y' before saving the user
        user.setLock_status("N");
        
        // Proceed with user registration
    	User savedUser = userRepository.save(user);
        logger.info("User registered successfully: {}", savedUser.getEmail());
        return savedUser;
    }


    @Override 
    public User authenticateUser(String email, String password) {
        User user = userRepository.findByEmail(email);
        if (user != null && user.getPassword().equals(password)) {
        	
        	 if ("Y".equals(user.getLock_status())) {
                 // Handle locked user case if necessary
                 return user;
             }
        	 else {
        		 throw new UserLockedException("User account is locked.");
        	 }
//            return user;
        }
        return null;
    }
}
