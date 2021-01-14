package com.cea.jwt.dao;

import com.cea.jwt.model.User;

import java.util.Map;

public interface UserRepo {
	void save(User user);
	Map<String, User> findAll();
}
