package com.softserve.borda.repositories;

import com.softserve.borda.entities.Comment;
import com.softserve.borda.entities.JWTUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JWTUserRepository extends JpaRepository<JWTUser, String> {
}
