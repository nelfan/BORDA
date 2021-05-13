package com.softserve.borda.repositories;

import com.softserve.borda.entities.BoardRole;
import com.softserve.borda.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.Optional;

@RepositoryRestResource
public interface BoardRoleRepository extends JpaRepository<BoardRole, Long> {
    Optional<BoardRole> findByName(String name);
}
