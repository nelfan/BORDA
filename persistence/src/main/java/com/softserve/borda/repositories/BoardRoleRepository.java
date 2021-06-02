package com.softserve.borda.repositories;

import com.softserve.borda.entities.UserBoardRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BoardRoleRepository extends JpaRepository<UserBoardRole, Long> {
    Optional<UserBoardRole> findByName(String name);
}
