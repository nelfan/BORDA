package com.softserve.borda.repositories;

import com.softserve.borda.entities.BoardRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BoardRoleRepository extends JpaRepository<BoardRole, Long> {
    Optional<BoardRole> findByName(String name);
}
