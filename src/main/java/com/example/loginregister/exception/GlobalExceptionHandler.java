package com.example.loginregister.exception;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
	
    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(UserLockedException.class)
    public ResponseEntity<String> handleUserLockedException(UserLockedException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleGenericException(Exception ex) {
        logger.error("An error occurred: {}", ex.getMessage(), ex); 
        return new ResponseEntity<>("Authentication failed. Invalid email or password.", HttpStatus.UNAUTHORIZED);
    }
    
    @ExceptionHandler(DuplicateCartItemException.class)
    public ResponseEntity<String> handleDuplicateCartItemException(DuplicateCartItemException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ResponseEntity<Map<String, String>> handleAccessDeniedException(AccessDeniedException e) {
        logger.error("\n\nAccess Denied you dont have user permission to access this from globalexception handler bro: ", e);
        
        Map<String, String> response = new HashMap<>();
        response.put("message", "Access denied. you dont have access for it!. user permission does not exxist.");

        StringWriter sw = new StringWriter();    
        PrintWriter pw = new PrintWriter(sw);
        e.printStackTrace(pw);
        String stackTrace = sw.toString();
        
        logger.error("Stack Trace: \n" + stackTrace);

        return new ResponseEntity<>(response, HttpStatus.FORBIDDEN); 
    }
    
    @ExceptionHandler(UnauthorizedAccessException.class)
    public ResponseEntity<String> handleUnauthorizedAccess(UnauthorizedAccessException ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                             .body("Unauthorized Access: " + ex.getMessage());
    }
}
