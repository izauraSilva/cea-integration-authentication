package com.cea.jwt.repository;

import com.cea.jwt.model.Access;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccessRepo extends JpaRepository<Access, Long> {
    Optional<Access> findByUsernameAndPassword(String username, String password);
}
