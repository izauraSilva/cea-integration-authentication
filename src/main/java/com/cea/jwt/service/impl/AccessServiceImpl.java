package com.cea.jwt.service.impl;

import com.cea.jwt.model.Access;
import com.cea.jwt.repository.AccessRepo;
import com.cea.jwt.service.AccessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AccessServiceImpl implements AccessService {

    @Autowired
    private AccessRepo accessRepo;

    @Override
    public Optional<Access> getUser(String username, String password) {
        return accessRepo.findByUsernameAndPassword(username,password);
    }
}
