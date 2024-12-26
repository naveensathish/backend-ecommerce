package com.example.loginregister.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;

@Configuration 
@EnableJpaRepositories(basePackages = "com.example.loginregister.repository")
@EnableRedisRepositories(basePackages = "com.example.loginregister.repository")
public class RepositoryConfig {
}
