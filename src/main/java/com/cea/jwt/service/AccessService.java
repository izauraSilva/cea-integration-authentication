package com.cea.jwt.service;

import com.cea.jwt.model.Access;
import java.util.Optional;

public interface AccessService {
    Optional<Access> getUser(String username, String password);
}
