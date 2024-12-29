package com.example.loginregister;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"com.example.loginregister", "com.example.loginregister.Service"})

public class LoginregisterApplication {
    public static void main(String[] args) {
        SpringApplication.run(LoginregisterApplication.class, args);
    }
}
