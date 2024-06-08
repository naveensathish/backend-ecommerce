package com.example.loginregister.EmployeeController;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.loginregister.Entity.User;
import com.example.loginregister.Service.AuthService;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:4200")
public class AuthController {
    
    @Autowired
    private AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<User> register(@RequestBody User user) {
//    	User registeredUser = authService.registerUser(user); 
//    	return ResponseEntity.ok(registeredUser);
        return ResponseEntity.ok(authService.registerUser(user));
    }

    @PostMapping("/login")
    public ResponseEntity<User> login(@RequestBody User user) {
        User authenticatedUser = authService.authenticateUser(user.getEmail(), user.getPassword());
        System.out.println(user.toString());
        String em= user.getEmail();
        String pass = user. getPassword();
        System.out.println("login id  :"+em+"password:  "+pass);
        System.out.println(authenticatedUser);
        
        if (authenticatedUser != null) {
            return ResponseEntity.ok(authenticatedUser);
        }
        return ResponseEntity.status(401).build();
    }
    
    @GetMapping("/check-email/{email}")
    public ResponseEntity<Boolean> checkEmailExists(@PathVariable String email) {
        boolean exists = authService.emailExists(email);
        return ResponseEntity.ok(exists);
    }
}





//package com.example.loginregister.EmployeeController;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//import com.example.loginregister.Entity.User;
//import com.example.loginregister.Service.AuthService;
//import com.example.loginregister.Exceptions.UserNotFoundException;
//import com.example.loginregister.Exceptions.InvalidCredentialsException;
//
//@RestController
//@RequestMapping("/api/auth")
//@CrossOrigin(origins = "http://localhost:4200")
//public class AuthController {
//    
//    @Autowired
//    private AuthService authService;
//
//    @PostMapping("/register")
//    public ResponseEntity<?> register(@RequestBody User user) {
//        try {
//            User registeredUser = authService.registerUser(user);
//            return ResponseEntity.ok(registeredUser);
//        } catch (IllegalArgumentException e) {
//            return ResponseEntity.badRequest().body(e.getMessage());
//        }
//    }
//
//    @PostMapping("/login")
//    public ResponseEntity<?> login(@RequestBody User user) {
//        try {
//            User authenticatedUser = authService.authenticateUser(user.getEmail(), user.getPassword());
//            return ResponseEntity.ok(authenticatedUser);
//        } catch (UserNotFoundException | InvalidCredentialsException e) {
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
//        }
//    }
//}





//package com.example.loginregister.EmployeeController;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//import com.example.loginregister.Entity.User;
//import com.example.loginregister.Service.AuthService;
//
//@RestController
//@RequestMapping("/api/auth")
//@CrossOrigin(origins = "http://localhost:4200")
//public class AuthController {
//    
//    @Autowired
//    private AuthService authService;
//
//    @PostMapping("/register")
//    public ResponseEntity<User> register(@RequestBody User user) {
//        return ResponseEntity.ok(authService.registerUser(user));
//    }
//
//    @PostMapping("/login")
//    public ResponseEntity<User> login(@RequestBody User user) {
//        User authenticatedUser = authService.authenticateUser(user.getEmail(), user.getPassword());
//        if (authenticatedUser != null) {
//            return ResponseEntity.ok(authenticatedUser);
//        }
//        return ResponseEntity.status(401).build();
//    }
//}
