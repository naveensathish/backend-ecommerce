package com.example.loginregister.repository;

import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.repository.CrudRepository;

import com.example.loginregister.entity.User;

@RedisHash("users")
public interface UserRedisRepository extends CrudRepository<User, String> {

}
