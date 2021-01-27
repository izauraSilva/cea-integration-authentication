package com.cea.jwt.service.impl;

import com.cea.jwt.model.User;
import com.cea.jwt.repository.UserRepo;
import com.cea.jwt.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepo userRepo;

    @Override
    public void save(User user) {
        userRepo.save(user);
    }

    @Override
    public Iterable<User> getAll() {
        return userRepo.findAll();
    }
}
