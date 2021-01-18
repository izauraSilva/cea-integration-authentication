package com.cea.jwt.repository;

import com.cea.jwt.model.User;

import java.util.Map;

public interface UserRepo {

	void save(User user);

	User findById(String id);

	Map<String, User> findAll();

}
