package com.example.loginregister.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.loginregister.service.RedisExampleService;

@RestController
public class RedisController {

    @Autowired
    private RedisExampleService redisExampleService;

    @PostMapping("/save")
    public String saveData(@RequestParam String key, @RequestParam String value) {
        redisExampleService.saveData(key, value);
        return "Data saved!";
    }

    @GetMapping("/retrieve") 
    public String retrieveData(@RequestParam String key) {
		return redisExampleService.retrieveData(key);
	}
}