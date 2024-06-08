package com.example.loginregister.Service;

import com.example.loginregister.Entity.User;

public interface AuthService {
    User registerUser(User user);
    User authenticateUser(String email, String password);
}
