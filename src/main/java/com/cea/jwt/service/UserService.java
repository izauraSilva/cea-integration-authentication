package com.cea.jwt.service;

import com.cea.jwt.dao.UserRepo;

import com.cea.jwt.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Map;

@Service
public class UserService implements UserRepo {

	private final String USER_CACHE = "USER";

	@Autowired
	RedisTemplate<String, Object> redisTemplate;

	private HashOperations<String, String, User> hashOperations;

	@PostConstruct
	private void intializeHashOperations() {
		hashOperations = redisTemplate.opsForHash();
	}

	@Override
	public void save(final User user) {
		hashOperations.put(USER_CACHE, user.getLogin(), user);
	}

	@Override
	public Map<String, User> findAll() {
		return hashOperations.entries(USER_CACHE);
	}

}
