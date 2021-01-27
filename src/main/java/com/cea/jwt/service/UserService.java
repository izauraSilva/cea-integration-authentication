package com.cea.jwt.service;

import com.cea.jwt.model.User;

public interface UserService {
   void save(User user);
   Iterable<User> getAll();
}
